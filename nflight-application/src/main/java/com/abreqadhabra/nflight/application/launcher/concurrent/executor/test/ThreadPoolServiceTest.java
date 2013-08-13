package com.abreqadhabra.nflight.application.launcher.concurrent.executor.test;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.concurrent.executor.ThreadPoolExecutorService;
import com.abreqadhabra.nflight.application.launcher.concurrent.executor.monitor.ThreadPoolMonitorService;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;



public class ThreadPoolServiceTest {
	private static Class<ThreadPoolServiceTest> THIS_CLAZZ = ThreadPoolServiceTest.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	ThreadPoolMonitorService threadPoolMonitorService;
	ThreadPoolExecutorService threadPoolExecutorService;

	public ThreadPoolServiceTest() {
	//	threadPoolMonitorService = new ThreadPoolMonitorServiceImpl(3);
	//	threadPoolExecutorService = new ThreadPoolExecutorServiceImpl();
	}

	public static void main(String[] args) {
		ThreadPoolServiceTest test = new ThreadPoolServiceTest();
		test.start();
	}

	private void start() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// A new thread pool is created...
		ThreadPoolExecutor executor = threadPoolExecutorService
				.createNewThreadPool();
		executor.allowCoreThreadTimeOut(true);

		// Created executor is set to ThreadPoolMonitorService...
		threadPoolMonitorService.setExecutor(executor);

		// ThreadPoolMonitorService is started...
		Thread monitor = new Thread(threadPoolMonitorService);
		monitor.start();

		// New tasks are executed...
		for (int i = 1; i < 10; i++) {
			executor.execute(new TestTask("Task" + i));
		}

		try {
			Thread.sleep(40000);
		} catch (Exception e) {
			LOGGER.logp(Level.SEVERE, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					e.getMessage());
		}

		for (int i = 10; i < 19; i++) {
			executor.execute(new TestTask("Task" + i));
		}

		// executor is shutdown...
		executor.shutdown();
	}

	public ThreadPoolMonitorService getThreadPoolMonitorService() {
		return threadPoolMonitorService;
	}

	public void setThreadPoolMonitorService(
			ThreadPoolMonitorService threadPoolMonitorService) {
		this.threadPoolMonitorService = threadPoolMonitorService;
	}

	public ThreadPoolExecutorService getThreadPoolExecutorService() {
		return threadPoolExecutorService;
	}

	public void setThreadPoolExecutorService(
			ThreadPoolExecutorService threadPoolExecutorService) {
		this.threadPoolExecutorService = threadPoolExecutorService;
	}

}
