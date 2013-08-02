package com.abreqadhabra.nflight.application.server.net.tcp.aio.concurrent;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.server.net.tcp.Session;
import com.abreqadhabra.nflight.application.server.net.tcp.aio.SocketServerSession;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class SocketServerTask implements Runnable {
	private static final Class<SocketServerTask> THIS_CLAZZ = SocketServerTask.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private final AsynchronousServerSocketChannel asyncServerSocketChannel;
	private final boolean isRunning = false;
	private final AtomicLong sessionId = new AtomicLong(0);

	private final ConcurrentHashMap<Long, Session> sessionMap;
	private final Configure configure;

	public SocketServerTask(
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

		Future<AsynchronousSocketChannel> future = null;
		AsynchronousSocketChannel asyncSocketChannel = null;
		Session session = null;

		while (!isRunning) {
			future = asyncServerSocketChannel.accept();
			try {
				asyncSocketChannel = future.get();

				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						threadName
								+ "\t비동기 서버 소켓 채널 바인딩 주소 : "
								+ asyncSocketChannel.getLocalAddress()
										.toString());

				session = new SocketServerSession(sessionId.incrementAndGet(),
						asyncSocketChannel);
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