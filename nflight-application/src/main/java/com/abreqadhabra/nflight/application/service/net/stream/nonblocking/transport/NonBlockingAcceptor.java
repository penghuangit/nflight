package com.abreqadhabra.nflight.application.service.net.stream.nonblocking.transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.Acceptor;
import com.abreqadhabra.nflight.application.service.net.NetworkServiceHelper;
import com.abreqadhabra.nflight.application.service.net.ServerChannelFactory;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class NonBlockingAcceptor implements Runnable, Acceptor {
	private static Class<NonBlockingAcceptor> THIS_CLAZZ = NonBlockingAcceptor.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private boolean isRunning;
	private Configure configure;
	private ServerSocketChannel channel;
	private Selector selector;

	public NonBlockingAcceptor(boolean isRunning, InetSocketAddress endpoint,
	/* ThreadPoolExecutor threadPool, */Configure configure)
			throws IOException, InterruptedException, ExecutionException {
		this.isRunning = isRunning;
		this.configure = configure;
		this.init(endpoint);
	}

	@Override
	public void init(InetSocketAddress endpoint) throws IOException,
			InterruptedException, ExecutionException {

		// create a new server-socket channel & selector
		this.selector = Selector.open();

		int backlog = this.configure.getInt(Configure.NONBLOCKING_BIND_BACKLOG);
		// create a new server-socket channel
		this.channel = this.createServerChannelFactory()
				.createNonBlockingServerSocketChannel(endpoint, backlog);
		// check that both of them were successfully opened
		if (this.selector.isOpen() && this.channel.isOpen()) {
			// Register the server socket channel, indicating an interest in
			// accepting new connections
			this.channel.register(this.selector, SelectionKey.OP_ACCEPT);

		} else {
			throw new IllegalStateException("서버 소켓 채널 또는 셀렉터가 열려있지 않습니다.");
		}
	}

	@Override
	public void run() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {

			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getSimpleName(),
					METHOD_NAME,
					"current thread is "
							+ LoggingHelper.getThreadName(Thread
									.currentThread()));
			while (this.isRunning) {
				// wait for incoming an event one of the registered channels
				// 등록된 서버 소켓 채널에 대한 이벤트 발생을 대기
				long timeout = 1000;
				this.selector.select(timeout);

				// there is something to process on selected keys
				Iterator<SelectionKey> iterator = this.selector.selectedKeys()
						.iterator();

				while (iterator.hasNext()) {
					SelectionKey selectionKey = iterator.next();
					// 취득한 키를 키 집합에서 제거
					// prevent the same key from coming up again
					iterator.remove();
					if (!selectionKey.isValid()) {
						continue;
					}

					LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
							METHOD_NAME, NetworkServiceHelper
									.getReadySetString(selectionKey));

					// 이벤트가 사용할 수 있는지 확인하고 처리
					if (selectionKey.isAcceptable()) {
						this.accept(this.selector, selectionKey);
					} else if (selectionKey.isReadable()) {
						SocketChannel socket = (SocketChannel) selectionKey
								.attachment();
						this.receive(socket);
					} else if (selectionKey.isWritable()) {
						SocketChannel socket = (SocketChannel) selectionKey
								.attachment();
						this.send(socket);
					} else {
						LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
								METHOD_NAME, "Unexpected ops in select "
										+ selectionKey.readyOps());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ServerChannelFactory createServerChannelFactory() {
		return new ServerChannelFactory();
	}

	private void accept(Selector selector, SelectionKey selectionKey) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			SocketChannel socket = ((ServerSocketChannel) selectionKey
					.channel()).accept();
			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getSimpleName(),
					METHOD_NAME,
					"Accepted socket connection from "
							+ socket.getRemoteAddress());
			socket.configureBlocking(false);
			SelectionKey readyKey = socket.register(selector,
					SelectionKey.OP_READ);
			readyKey.attach(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(SocketChannel socket) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				socket.toString());

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"send");
	}

	public void receive(SocketChannel socket) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			int capacity = this.configure
					.getInt(Configure.NONBLOCKING_INCOMING_BUFFER_CAPACITY);
			ByteBuffer incomingByteBuffer = NetworkServiceHelper
					.getByteBuffer(capacity);
			int numRead = socket.read(incomingByteBuffer);
			incomingByteBuffer.flip();
			if (incomingByteBuffer.hasRemaining()) {
				incomingByteBuffer.compact();
			} else {
				incomingByteBuffer.clear();
			}

			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getSimpleName(),
					METHOD_NAME,
					new String(incomingByteBuffer.array(), "UTF-8") + " ["
							+ numRead + " bytes] from "
							+ socket.getRemoteAddress());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
				LOGGER.logp(
						Level.FINER,
						THIS_CLAZZ.getSimpleName(),
						METHOD_NAME,
						"isOpen=" + Boolean.toString(socket.isOpen())
								+ ", isConnected="
								+ Boolean.toString(socket.isConnected()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
