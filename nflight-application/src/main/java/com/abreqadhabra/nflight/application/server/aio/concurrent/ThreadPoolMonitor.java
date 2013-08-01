package com.abreqadhabra.nflight.application.server.aio.concurrent;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ThreadPoolMonitor implements Runnable {
	private static final Class<ThreadPoolMonitor> THIS_CLAZZ = ThreadPoolMonitor.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private ThreadPoolExecutor executor;

	private int seconds;

	private boolean run = true;

	public ThreadPoolMonitor(ThreadPoolExecutor executor, int delay) {
		this.executor = executor;
		this.seconds = delay;
	}

	public void shutdown() {
		this.run = false;
	}

	@Override
	public void run() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// 현재 실행 중인 쓰레드명을 변경 설정
		final String threadName = Thread.currentThread().getName();
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, "[Thread] "
				+ threadName);
		// Thread.currentThread().setName("nflight-" + threadName + "-" +
		// THIS_CLAZZ.getSimpleName());

		while (run) {
			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getName(),
					METHOD_NAME,
					String.format(
							"[ThreadPool] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
							this.executor.getPoolSize(),
							this.executor.getCorePoolSize(),
							this.executor.getActiveCount(),
							this.executor.getCompletedTaskCount(),
							this.executor.getTaskCount(),
							this.executor.isShutdown(),
							this.executor.isTerminated()));
			try {
				Thread.sleep(seconds * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}