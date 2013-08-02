package com.abreqadhabra.nflight.application.launcher.concurrent.executor.monitor;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ThreadPoolMonitorServiceImpl implements ThreadPoolMonitorService {
	private static final Class<ThreadPoolMonitorServiceImpl> THIS_CLAZZ = ThreadPoolMonitorServiceImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	ThreadPoolExecutor executor;

	private int delaySeconds;

	public ThreadPoolMonitorServiceImpl(int delay) {
		this.delaySeconds = delay;
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

		try {
			while (true) {
				this.monitorThreadPool();
				Thread.sleep(this.delaySeconds * 1000);
			}
		} catch (Exception e) {
			LOGGER.logp(Level.SEVERE, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					e.getMessage());
		}
	}

	@Override
	public void monitorThreadPool() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		String result = String
				.format("[ThreadPool] [%d/%d] TotalTaskCount: %d [Active: %d, Completed: %d] isShutdown: %s , isTerminated: %s",
						this.executor.getPoolSize(),
						this.executor.getCorePoolSize(),
						this.executor.getTaskCount(),
						this.executor.getActiveCount(),
						this.executor.getCompletedTaskCount(),
						this.executor.isShutdown(),
						this.executor.isTerminated());

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				result);

	}
	@Override
	public ThreadPoolExecutor getExecutor() {
		return this.executor;
	}

	@Override
	public void setExecutor(ThreadPoolExecutor executor) {
		this.executor = executor;
	}

	public long getDelaySeconds() {
		return this.delaySeconds;
	}

	public void setDelaySeconds(int delayseconds) {
		this.delaySeconds = delayseconds;
	}
}