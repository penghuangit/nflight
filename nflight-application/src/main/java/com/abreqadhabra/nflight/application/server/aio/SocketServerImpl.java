package com.abreqadhabra.nflight.application.server.aio;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.aio.concurrent.ListenRobot;
import com.abreqadhabra.nflight.application.server.aio.concurrent.ThreadPoolMonitor;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class SocketServerImpl implements SocketServer {
	private static final Class<SocketServerImpl> THIS_CLAZZ = SocketServerImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private final ConcurrentHashMap<Long, Session> sessionMap = new ConcurrentHashMap<Long, Session>();
	private AsynchronousServerSocketChannel asyncServerSocketChannel;
	private final Configure configure;
	private final ThreadPoolExecutor threadPoolExecutor;

	private InetSocketAddress socketAddress;

	public SocketServerImpl() {
		configure = new SocketServerConfiguraImpl();

		threadPoolExecutor = new ThreadPoolExecutor(Integer.parseInt(configure
				.get("min_pool_size").trim()), Integer.parseInt(configure.get(
				"max_pool_size").trim()), Long.parseLong(configure.get(
				"keep_alive_time").trim()), TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(),
				new RejectedExecutionHandelerImpl());

		// start the monitoring thread
		startMonitoringThread(threadPoolExecutor, 30);

	}

	private void startMonitoringThread(ThreadPoolExecutor threadPoolExecutor,
			int delay) {
		ThreadPoolMonitor monitor = new ThreadPoolMonitor(threadPoolExecutor,
				delay);
		Thread monitorThread = new Thread(monitor);
		monitorThread.start();
		// /monitor.shutdown();
	}

	public SocketServerImpl(final InetSocketAddress socketAddress) {
		this();
		this.socketAddress = socketAddress;
	}

	@Override
	public void startup() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		asyncServerSocketChannel = AsynchronousServerSocketChannel.open();
		asyncServerSocketChannel.bind(socketAddress);

		LOGGER.logp(
				Level.FINER,
				THIS_CLAZZ.getSimpleName(),
				METHOD_NAME,
				"바인딩된 서버 소켓 채널 주소 :"
						+ asyncServerSocketChannel.getLocalAddress());

		threadPoolExecutor.submit(new ListenRobot(asyncServerSocketChannel,
				sessionMap, configure));
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Session getSession(final long sessionId) {
		return sessionMap.get(sessionId);
	}

}
