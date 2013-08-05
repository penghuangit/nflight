package com.abreqadhabra.nflight.application.server.net.async;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.server.net.async.handler.AsyncServerAcceptHandler;
import com.abreqadhabra.nflight.application.server.net.socket.AbstractSocketServerImpl;
import com.abreqadhabra.nflight.application.server.net.socket.ISocketAcceptor;
import com.abreqadhabra.nflight.application.server.net.socket.NetworkChannelHelper;
import com.abreqadhabra.nflight.application.server.net.socket.logic.IBusinessLogicHandler;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class AsyncServerImpl extends AbstractSocketServerImpl {
	private static final Class<AsyncServerImpl> THIS_CLAZZ = AsyncServerImpl.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private final InetSocketAddress socketAddress;
	private final IBusinessLogicHandler logicHandler;

	private AsynchronousChannelGroup threadGroup;
	private AsynchronousServerSocketChannel asyncServerSocketChannel;


	
	private final ConcurrentHashMap<Long, ISocketAcceptor> sessionMap = new ConcurrentHashMap<Long, ISocketAcceptor>();
	private boolean isRunning = false;
	private Future<AsynchronousSocketChannel> future;
	private final AtomicLong sessionId = new AtomicLong(0);

	public AsyncServerImpl(final Configure configure,
			final ThreadPoolExecutor threadPoolExecutor,
			final InetSocketAddress socketAddress,
			final IBusinessLogicHandler logicHandler) throws Exception {
		super(configure, threadPoolExecutor);
		this.socketAddress = socketAddress;
		this.logicHandler = logicHandler;
		this.logicHandler.setServer(this);
	}

	@Override
	public void init() {
		try {
			int initialSize = super.configure.getInt(
					Configure.ASYNC_THREADPOOL_INITIALSIZE);
			this.threadGroup = AsynchronousChannelGroup.withCachedThreadPool(
					super.threadPoolExecutor, initialSize);
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean open() throws IOException {
		// create asynchronous server-socket channel bound to the thread Group
		this.asyncServerSocketChannel = AsynchronousServerSocketChannel
				.open(this.threadGroup);

		return this.asyncServerSocketChannel.isOpen();
	}

	@Override
	public void bind() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// set Network Channel options
		NetworkChannelHelper.setChannelOption(this.asyncServerSocketChannel);
		try {
			SocketAddress local = this.socketAddress;
			// maximum number of pending connections
			int backlog = super.configure.getInt(
					Configure.ASYNC_BIND_BACKLOG);
			// bind the server-socket channel to local address
			this.asyncServerSocketChannel.bind(local, backlog);
			// display a waiting message while ... waiting clients
			LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					asyncServerSocketChannel.getLocalAddress()
							+ ": Waiting for connections ...");
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void accept() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		this.asyncServerSocketChannel.accept(null,
				new AsyncServerAcceptHandler(super.configure,
						asyncServerSocketChannel, this));
		

		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// @Override
	// public void accept() {
	// final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
	// .getMethodName();
	//
	// while (this.isRunning = true) {
	// if (this.isRunning && this.asyncServerSocketChannel.isOpen()) {
	// this.future = this.asyncServerSocketChannel.accept();
	// try {
	// final AsynchronousSocketChannel asyncSocketChannel = this.future
	// .get();
	// final AsyncSocketServerAcceptor acceptor = new AsyncSocketServerAcceptor(
	// super.configure, this.sessionId.incrementAndGet(),
	// asyncSocketChannel, this.messageDTO,
	// this.logicHandler);
	// this.sessionMap.put(acceptor.getSessionId(), acceptor);
	// LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
	// METHOD_NAME,
	// this.sessionMap.get(acceptor.getSessionId())
	// + "----------> accepting connection: "
	// + asyncSocketChannel.getRemoteAddress());
	// acceptor.start();
	// } catch (final Exception e) {
	// e.printStackTrace();
	// }
	// } else {
	// throw new IllegalStateException("Channel has been closed");
	// }
	// }
	// }

	@Override
	public ConcurrentHashMap<Long, ISocketAcceptor> getSessionMap() {
		return this.sessionMap;
	}

}
