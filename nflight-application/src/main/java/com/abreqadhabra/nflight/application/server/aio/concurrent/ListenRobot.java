package com.abreqadhabra.nflight.application.server.aio.concurrent;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.aio.Configure;
import com.abreqadhabra.nflight.application.server.aio.Session;
import com.abreqadhabra.nflight.application.server.aio.SocketServerSession;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ListenRobot implements Runnable {
	private static final Class<ListenRobot> THIS_CLAZZ = ListenRobot.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private final AsynchronousServerSocketChannel asyncServerSocketChannel;
	private final boolean running = false;
	private final AtomicLong sessionId = new AtomicLong(0);

	private final ConcurrentHashMap<Long, Session> sessionMap;
	private final Configure configure;

	public ListenRobot(
			final AsynchronousServerSocketChannel asynchronousServerSocketChannel,
			final ConcurrentHashMap<Long, Session> sessionMap,
			final Configure configure) {
		this.asyncServerSocketChannel = asynchronousServerSocketChannel;
		this.sessionMap = sessionMap;
		this.configure = configure;
	}

	@Override
	public void run() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// 현재 실행 중인 쓰레드명을 변경 설정
		final String threadName = Thread.currentThread().getName();
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, "[Thread] "
				+ threadName);

		while (!running) {
			final Future<AsynchronousSocketChannel> future = asyncServerSocketChannel
					.accept();
			try {

				final AsynchronousSocketChannel asyncSocketChannel = future
						.get();

				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, threadName +"\t" +
						asyncSocketChannel.getLocalAddress().toString());

				final Session session = new SocketServerSession(
						sessionId.incrementAndGet(), asyncSocketChannel);
				sessionMap.put(session.getSessionId(), session);
				session.init(configure);
				session.open();
				session.read();

				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						session.toString() + "\n" + sessionMap);

			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}
}