package com.abreqadhabra.nflight.application.server.net.tcp.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.application.server.net.tcp.Session;
import com.abreqadhabra.nflight.application.server.net.tcp.SocketServer;
import com.abreqadhabra.nflight.application.server.net.tcp.aio.logic.BusinessLogicHandler;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class SocketServerImpl implements SocketServer {
	private static final Class<SocketServerImpl> THIS_CLAZZ = SocketServerImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private InetSocketAddress socketAddress;
	private MessageDTO messageDTO;
	private BusinessLogicHandler logicHandler;
	private AsynchronousChannelGroup asynchronousChannelGroup;
	AsynchronousServerSocketChannel asyncServerSocketChannel;
	private ConcurrentHashMap<Long, Session> sessionMap = new ConcurrentHashMap<Long, Session>();
	private Configure configure;
	private boolean isRunning = false;
	private Future<AsynchronousSocketChannel> future;
	private AtomicLong sessionId = new AtomicLong(0);

	public SocketServerImpl(InetSocketAddress socketAddress,
			ThreadPoolExecutor threadPoolExecutor, MessageDTO messageDTO,
			BusinessLogicHandler logicHandler) throws Exception {

		this.configure = new ConfigureImpl(
				Configure.FILE_SOCKET_SERVER_PROPERTIES);
		this.socketAddress = socketAddress;
		this.asynchronousChannelGroup = AsynchronousChannelGroup
				.withCachedThreadPool(threadPoolExecutor, Integer
						.parseInt(this.configure.get(
								"nflight.socketserver.threadpool.initialsize")
								.trim()));
		this.messageDTO = messageDTO;
		this.logicHandler = logicHandler;
		this.logicHandler.setServer(this);
		this.startup();
	}

	// public Runnable getSocketServerTask() throws Exception {
	// Thread.currentThread().getStackTrace()[1].getMethodName();
	//
	// this.asyncServerSocketChannel = AsynchronousServerSocketChannel.open(
	// this.asynchronousChannelGroup).bind(this.socketAddress);
	//
	// return new SocketServerTask(this.asyncServerSocketChannel,
	// this.sessionMap, this.configure);
	// }

	@Override
	public void startup() throws Exception {

		asyncServerSocketChannel = AsynchronousServerSocketChannel
				.open(asynchronousChannelGroup);

		setChannelOption(asyncServerSocketChannel);

		// maximum number of pending connections
		asyncServerSocketChannel.bind(socketAddress, Integer
				.parseInt(this.configure.get("nflight.socketserver.backlog"
						.trim())));

		while (isRunning = true) {
			acceptPendingConnections();
		}
	}

	private void acceptPendingConnections() {
		if (isRunning && asyncServerSocketChannel.isOpen()) {
			future = asyncServerSocketChannel.accept();
			try {
				AsynchronousSocketChannel asynchronousSocketChannel = future.get();
				SocketServerSession socketServerSession = new SocketServerSession(
						sessionId.incrementAndGet(), asynchronousSocketChannel, messageDTO,
						logicHandler);
				sessionMap.put(socketServerSession.getSessionId(), socketServerSession);
				socketServerSession.init(configure);
				socketServerSession.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalStateException("Controller has been closed");
		}
	}

	private void setChannelOption(
			AsynchronousServerSocketChannel asyncServerSocketChannel) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {

			if (LOGGER.isLoggable(Level.FINER)) {
				Set<SocketOption<?>> options = asyncServerSocketChannel
						.supportedOptions();
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, "서버 소켓 채널의 지원 옵션:  " + options);
			}

			asyncServerSocketChannel
					.setOption(StandardSocketOptions.SO_REUSEADDR, Boolean
							.parseBoolean(configure.get(
									"nflight.socketserver.option.so_reuseaddr")
									.trim()));
			asyncServerSocketChannel.setOption(
					StandardSocketOptions.SO_RCVBUF,
					Integer.parseInt(configure.get(
							"nflight.socketserver.option.so_rcvbuf").trim()));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Session getSession(final long sessionId) {
		return this.sessionMap.get(sessionId);
	}

//	@Override
//	public void setSessionMap(ConcurrentHashMap<Long, Session> sessionMap) {
//		this.sessionMap = sessionMap;
//	}
	@Override
	public ConcurrentHashMap<Long, Session> getSessionMap() {
		return sessionMap;
	}
	
}
