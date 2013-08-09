package com.abreqadhabra.nflight.application.server.service.socket.udp.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.service.socket.SocketAcceptor;
import com.abreqadhabra.nflight.application.server.service.socket.tcp.common.ChangeRequest;
import com.abreqadhabra.nflight.application.server.service.socket.tcp.common.ResponseHandler;
import com.abreqadhabra.nflight.application.server.service.socket.tcp.impl.AcceptorWorker;
import com.abreqadhabra.nflight.application.server.service.socket.tcp.impl.DataEvent;
import com.abreqadhabra.nflight.application.server.service.socket.udp.common.Attachment;
import com.abreqadhabra.nflight.common.exception.WrapperException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public abstract class AbstractDatagramAcceptor implements SocketAcceptor,
		Runnable {
	private static final Class<AbstractDatagramAcceptor> THIS_CLAZZ = AbstractDatagramAcceptor.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	// The host:port combination to listen on
	private final InetAddress hostAddress;
	private final int port;

	// The channel on which we'll accept connections
	protected DatagramChannel datagramChannel;
	// The selector we'll be monitoring
	protected Selector selector;

	private final AcceptorWorker worker;

	// The buffer into which we'll read data when it's available
	private final ByteBuffer readBuffer = ByteBuffer.allocate(1024*1024);

	// Maps a SocketChannel to a list of ByteBuffer instances
	private Map<SocketChannel, List<ByteBuffer>> pendingData = new HashMap<SocketChannel, List<ByteBuffer>>();

	// A list of PendingChange instances
	protected List<ChangeRequest> pendingChanges = new LinkedList<ChangeRequest>();

	public AbstractDatagramAcceptor(final InetAddress hostAddress,
			final int port, final AcceptorWorker worker) throws IOException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		this.hostAddress = hostAddress;
		this.port = port;
		this.worker = worker;
		this.bind();
	}

	@Override
	public void bind() throws IOException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// 새로운 데이터그램 채널 생성
		this.datagramChannel  = DatagramChannel
				.open(StandardProtocolFamily.INET);

		// 새로운 셀렉터 생성
		this.selector = SelectorProvider.provider().openSelector();

		// 생성된 데이터그램 채널과 셀렉터가 성공적으로 열려있는지를 확인
		if ((this.selector.isOpen()) && (this.datagramChannel.isOpen())) {

			// 블로킹 메커니즘을 논블로킹 모드로 설정
			this.datagramChannel.configureBlocking(false);

			if (LOGGER.isLoggable(Level.FINER)) {
				Set<SocketOption<?>> options = datagramChannel.supportedOptions();
				StringBuffer sb = new StringBuffer(
						"데이터그램 채널의 지원 옵션:  ");

				for (SocketOption<?> option : options) {
					sb.append(option.name());
					sb.append(" ");
				}
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, sb.toString());
			}

			// 데이터그램 채널 옵션 설정 
			datagramChannel
			.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024)
			.setOption(StandardSocketOptions.SO_SNDBUF, 4 * 1024);

			// 네트워크 주소와 포트 번호를 지정하여 InetSocketAddress 생성
			final InetSocketAddress isa = new InetSocketAddress(
					hostAddress, port);
			
			// 지정된 네트워크 주소로 소켓을 바인딩
			this.datagramChannel.bind(isa);
		
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
					METHOD_NAME, "바인딩된 데이터그램 채널 주소 :"+ datagramChannel.getLocalAddress() );

			// 데이터그램 채널을 셀렉터에 OP_ACCEPT 키로 등록
			this.datagramChannel.register(this.selector, SelectionKey.OP_READ);
			
		} else {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					" 데이터그램 채널 또는 셀렉터가 열려있지 않습니다.");
		}
		
	}

	@Override
	public void accept(final SelectionKey key) throws IOException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

//		// For an accept to be pending the channel must be a acceptor socket
//		// channel.
//		final DatagramChannel datagramChannel = (DatagramChannel) key
//				.channel();
//
//		// Accept the connection and make it non-blocking
//		final SocketChannel socketChannel = datagramChannel.accept();
//		socketChannel.socket();
//		socketChannel.configureBlocking(false);
//
//		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
//				"Incoming connection from: " + socketChannel.getRemoteAddress());
//
//		// Register the new SocketChannel with our Selector, indicating
//		// we'd like to be notified when there's data waiting to be read
//		socketChannel.register(this.selector, SelectionKey.OP_READ);
	}

	@Override
	public void read(final SelectionKey key) throws IOException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		final DatagramChannel datagramChannel = (DatagramChannel) key.channel();


		// Clear out our read buffer so it's ready for new data
		this.readBuffer.clear();

		// Attempt to read off the channel
		int numRead = -1;
		try {
			numRead = datagramChannel.read(this.readBuffer);
		} catch (final IOException e) {
			// The remote forcibly closed the connection, cancel
			// the selection key and close the channel.
			key.cancel();
			datagramChannel.close();
			return;
		}
		


		if (numRead == -1) {
			// Remote entity shut the socket down cleanly. Do the
			// same from our end and cancel the channel.
			key.channel().close();
			key.cancel();
			return;
		}

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				numRead + " bytes from " + datagramChannel.getRemoteAddress());

		// Hand the data off to our worker thread
		this.worker.processData(this, datagramChannel, this.readBuffer.array(),
				numRead);		



	}

	@Override
	public void write(final SelectionKey key) throws IOException {
		final DatagramChannel datagramChannel = (DatagramChannel) key.channel();

		synchronized (this.pendingData) {
			final List<?> queue = this.pendingData.get(datagramChannel);

			// Write until there's not more data ...
			while (!queue.isEmpty()) {
				final ByteBuffer buf = (ByteBuffer) queue.get(0);
				datagramChannel.write(buf);
				if (buf.remaining() > 0) {
					// ... or the socket's buffer fills up
					break;
				}
				queue.remove(0);
			}

			if (queue.isEmpty()) {
				// We wrote away all data, so we're no longer interested
				// in writing on this socket. Switch back to waiting for
				// data.
				key.interestOps(SelectionKey.OP_READ);
			}
		}

	}

	@Override
	public void send(SocketChannel socket, ByteBuffer data) {
		Thread.currentThread().getStackTrace()[1].getMethodName();

		synchronized (this.pendingChanges) {
			// Indicate we want the interest ops set changed
			this.pendingChanges.add(new ChangeRequest(socket,
					ChangeRequest.CHANGE_OPS, SelectionKey.OP_WRITE));

			// And queue the data we want written
			synchronized (this.pendingData) {
				List<ByteBuffer> queue = this.pendingData.get(socket);
				if (queue == null) {
					queue = new ArrayList<ByteBuffer>();
					this.pendingData.put(socket, queue);
				}
				queue.add(data);
			}
		}

		// Finally, wake up our selecting thread so it can make the required
		// changes
		this.selector.wakeup();
	}

	@Override
	public void execute(final DataEvent dataEvent) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Object object = ResponseHandler.deserializeObject(dataEvent.data);
		if (object == null) {
			object = new String(dataEvent.data);
		}

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				object.toString());
		
		if("shutdown".equals(object.toString())){
			exit();
		}

		final String response = "acceptor";

		
		final ByteBuffer data = ResponseHandler.serializeObject(response);

		this.send(dataEvent.socket, data);

	}
	
	public static void exit() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// 3초간 대기후 어플리케이션을 종료합니다.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (final InterruptedException ie) {
					// 이 예외는 발생하지 않습니다.
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(),
							METHOD_NAME, "Thread was interrupted\n"
									+ WrapperException.getStackTrace(ie));
				}
				System.gc();
				System.runFinalization();
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(),
						METHOD_NAME, "system exit");
				System.exit(0);
			}
		}).start();
	}

}
