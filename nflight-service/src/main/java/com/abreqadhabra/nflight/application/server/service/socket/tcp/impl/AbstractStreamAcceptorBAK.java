package com.abreqadhabra.nflight.application.server.service.socket.tcp.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
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
import com.abreqadhabra.nflight.common.exception.WrapperException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public abstract class AbstractStreamAcceptorBAK implements SocketAcceptor, Runnable {
    private static Class<AbstractStreamAcceptorBAK> THIS_CLAZZ = AbstractStreamAcceptorBAK.class;
    private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

    // The host:port combination to listen on
    private InetAddress hostAddress;
    private int port;

    // The channel on which we'll accept connections
    private ServerSocketChannel serverChannel;
    // The selector we'll be monitoring
    protected Selector selector;

    private AcceptorWorker worker;

    // The buffer into which we'll read data when it's available
    private ByteBuffer readBuffer = ByteBuffer.allocate(8192);

    // Maps a SocketChannel to a list of ByteBuffer instances
    private Map<SocketChannel, List<ByteBuffer>> pendingData = new HashMap<SocketChannel, List<ByteBuffer>>();

    // A list of PendingChange instances
    protected List<ChangeRequest> pendingChanges = new LinkedList<ChangeRequest>();

    public AbstractStreamAcceptorBAK(InetAddress hostAddress, int port, AcceptorWorker worker) throws IOException {
	this.hostAddress = hostAddress;
	this.port = port;
	this.worker = worker;
	this.bind();
    }

    @Override
    public void bind() throws IOException {
	String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

	// 새로운 서버 소켓 채널 생성
	this.serverChannel = ServerSocketChannel.open();

	// 새로운 셀렉터 생성
	this.selector = SelectorProvider.provider().openSelector();

	// 생성된 서버 소켓 채널과 셀렉터가 성공적으로 열려있는지를 확인
	if ((this.selector.isOpen()) && (this.serverChannel.isOpen())) {

	    // 블로킹 메커니즘을 논블로킹 모드로 설정
	    this.serverChannel.configureBlocking(false);

	    if (LOGGER.isLoggable(Level.FINER)) {
		Set<SocketOption<?>> options = serverChannel.supportedOptions();
		StringBuffer sb = new StringBuffer("서버 소켓 채널의 지원 옵션:  ");

		for (SocketOption<?> option : options) {
		    sb.append(option.name());
		    sb.append(" ");
		}
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, sb.toString());
	    }

	    // 서버 소켓 채널 옵션 설정 - 소켓 전송 버퍼 크기(바이트)를 지정
	    this.serverChannel.setOption(StandardSocketOptions.SO_RCVBUF, 256 * 1024);
	    // 서버 소켓 채널 옵션 설정 - 주소의 재사용 여부를 활성화
	    this.serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

	    // 네트워크 주소와 포트 번호를 지정하여 InetSocketAddress 생성
	    InetSocketAddress isa = new InetSocketAddress(hostAddress, port);

	    // 지정된 네트워크 주소로 소켓을 바인딩
	    this.serverChannel.socket().bind(isa);

	    LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "바인딩된 서버 소켓 채널 주소 :" + serverChannel.getLocalAddress());

	    // 서버 소켓 채널을 셀렉터에 OP_ACCEPT 키로 등록
	    this.serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);

	} else {
	    LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, " 서버 소켓 채널 또는 셀렉터가 열려있지 않습니다.");
	}
    }

    @Override
    public void accept(SelectionKey key) throws IOException {
	String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

	// For an accept to be pending the channel must be a server socket
	// channel.
	ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
	SocketChannel socketChannel = serverChannel.accept();
	socketChannel.configureBlocking(false);
	
	// write an welcome message
			send(socketChannel, ByteBuffer.wrap("Hello!\n".getBytes("UTF-8")));
			
	if (LOGGER.isLoggable(Level.FINER)) {
	    LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "Incoming connection from: " + socketChannel.getRemoteAddress());
	}
	// Register the new SocketChannel with our Selector, indicating
	// we'd like to be notified when there's data waiting to be read
	socketChannel.register(this.selector, SelectionKey.OP_READ);

    }

    @Override
    public void read(SelectionKey key) throws IOException {
	String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

	LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "a channel is ready for reading");

	SocketChannel socketChannel = (SocketChannel) key.channel();

	// Clear out our read buffer so it's ready for new data
	this.readBuffer.clear();

	// Attempt to read off the channel
	int numRead = -1;
	try {
	    numRead = socketChannel.read(this.readBuffer);
	} catch (IOException e) {
	    // The remote forcibly closed the connection, cancel
	    // the selection key and close the channel.
	    key.cancel();
	    socketChannel.close();
	    return;
	}

	if (numRead == -1) {
	    // Remote entity shut the socket down cleanly. Do the
	    // same from our end and cancel the channel.
	    key.channel().close();
	    key.cancel();
	    return;
	}

	LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, numRead + " bytes from " + socketChannel.getRemoteAddress());

	// Hand the data off to our worker thread
	this.worker.processData(this, socketChannel, this.readBuffer.array(), numRead);
    }

    @Override
    public void write(SelectionKey key) throws IOException {
	String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

	LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "a channel is ready for writing");

	SocketChannel socketChannel = (SocketChannel) key.channel();

	synchronized (this.pendingData) {
	    List<?> queue = this.pendingData.get(socketChannel);

	    // Write until there's not more data ...
	    while (!queue.isEmpty()) {
		ByteBuffer buf = (ByteBuffer) queue.get(0);
		socketChannel.write(buf);
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
	    this.pendingChanges.add(new ChangeRequest(socket, ChangeRequest.CHANGE_OPS, SelectionKey.OP_WRITE));

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
    public void execute(DataEvent dataEvent) {
	String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

	Object object = ResponseHandler.deserializeObject(dataEvent.data);
	if (object == null) {
	    object = new String(dataEvent.data);
	}

	LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, object.toString());

	String response = "acceptor";

	ByteBuffer data = ResponseHandler.serializeObject(response);

	this.send(dataEvent.socket, data);

    }

    public static void exit() {
	String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

	// 3초간 대기후 어플리케이션을 종료합니다.
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		try {
		    Thread.sleep(3000);
		} catch (InterruptedException ie) {
		    // 이 예외는 발생하지 않습니다.
		    LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, "Thread was interrupted\n" + WrapperException.getStackTrace(ie));
		}
		System.gc();
		System.runFinalization();
		LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME, "system exit");
		System.exit(0);
	    }
	}).start();
    }
}
