package com.abreqadhabra.nflight.application.server.net.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
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
import com.abreqadhabra.nflight.application.server.net.socket.logic.IBusinessLogicHandler;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class AsyncStreamServerImpl extends AbstractSocketServerImpl {
	private static final Class<AsyncStreamServerImpl> THIS_CLAZZ = AsyncStreamServerImpl.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private final InetSocketAddress socketAddress;
	private final MessageDTO messageDTO;
	private final IBusinessLogicHandler logicHandler;
	private AsynchronousChannelGroup asynchronousChannelGroup;
	AsynchronousServerSocketChannel asyncServerSocketChannel;
	private final ConcurrentHashMap<Long, ISocketAcceptor> sessionMap = new ConcurrentHashMap<Long, ISocketAcceptor>();
	private boolean isRunning = false;
	private Future<AsynchronousSocketChannel> future;
	private final AtomicLong sessionId = new AtomicLong(0);

	public AsyncStreamServerImpl(final Configure configure,
			final ThreadPoolExecutor threadPoolExecutor,
			final InetSocketAddress socketAddress, final MessageDTO messageDTO,
			final IBusinessLogicHandler logicHandler) throws Exception {
		super(configure, threadPoolExecutor);
		this.socketAddress = socketAddress;
		this.messageDTO = messageDTO;
		this.logicHandler = logicHandler;
		this.logicHandler.setServer(this);
	}

	@Override
	public void init() {
		try {
			this.asynchronousChannelGroup = AsynchronousChannelGroup
					.withCachedThreadPool(
							super.threadPoolExecutor,
							Integer.parseInt(super.configure
									.get("nflight.socketserver.threadpool.initialsize")
									.trim()));
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void open() {
		try {
			this.asyncServerSocketChannel = AsynchronousServerSocketChannel
					.open(this.asynchronousChannelGroup);
			SocketChannelHelper
					.setSocketChannelOption(this.asyncServerSocketChannel);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void bind() {
		try {
			// maximum number of pending connections
			this.asyncServerSocketChannel.bind(this.socketAddress, Integer
					.parseInt(super.configure
							.get("nflight.socketserver.backlog".trim())));
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void accept() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		while (this.isRunning = true) {
			if (this.isRunning && this.asyncServerSocketChannel.isOpen()) {
				this.future = this.asyncServerSocketChannel.accept();
				try {
					final AsynchronousSocketChannel asyncSocketChannel = this.future
							.get();
					final AsyncSocketServerAcceptor acceptor = new AsyncSocketServerAcceptor(
							super.configure, this.sessionId.incrementAndGet(),
							asyncSocketChannel, this.messageDTO,
							this.logicHandler);
					this.sessionMap.put(acceptor.getSessionId(), acceptor);
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
							METHOD_NAME,
							this.sessionMap.get(acceptor.getSessionId())
									+ "----------> accepting connection: "
									+ asyncSocketChannel.getRemoteAddress());
					acceptor.start();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			} else {
				throw new IllegalStateException("Channel has been closed");
			}
		}
	}

	@Override
	public ConcurrentHashMap<Long, ISocketAcceptor> getSessionMap() {
		return this.sessionMap;
	}

}
