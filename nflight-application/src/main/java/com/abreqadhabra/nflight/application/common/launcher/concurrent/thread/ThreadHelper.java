package com.abreqadhabra.nflight.application.common.launcher.concurrent.thread;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.application.common.launcher.concurrent.executor.ThreadPoolExecutorServiceImpl;
import com.abreqadhabra.nflight.application.common.launcher.concurrent.executor.monitor.ThreadPoolMonitorServiceImpl;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ThreadHelper {
	private static Class<ThreadHelper> THIS_CLAZZ = ThreadHelper.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static String getThreadName(Thread currentThread) {
		return currentThread.getName();
	}

	/**
	 * stop execution of the task
	 * 
	 * @param thread
	 */
	public static void interrupt(Thread thread) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.SEVERE, THIS_CLAZZ.getName(), METHOD_NAME,
				thread.getName() + ": Thread was interrupted");
		thread.interrupt();
	}

	public static void shutdown() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		// n초간 대기후 종료합니다.
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(Env.THREADHELPER_MILLIS);
				} catch (InterruptedException e) {
					// 이 예외는 발생하지 않습니다.
					LOGGER.logp(Level.SEVERE, THIS_CLAZZ.getName(),
							METHOD_NAME,
							"thread was interrupted" + e.getLocalizedMessage());
				}
				LOGGER.logp(Level.SEVERE, THIS_CLAZZ.getName(), METHOD_NAME,
						"exit");
				System.gc();
				System.runFinalization();
				System.exit(0);
			}
		}).start();

		return;
	}

	public static ThreadPoolExecutor getThreadPoolExecutor(String poolName,
			boolean isThreadPoolMonitoring, int MonitoringDelaySeconds)
			throws NFlightException {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutorServiceImpl()
				.createNewThreadPool();
		threadPoolExecutor.allowCoreThreadTimeOut(true);

		if (isThreadPoolMonitoring) {

			Thread t = Thread.currentThread();
			ThreadGroup threadGroup = t.getThreadGroup();

			// Created executor is set to ThreadPoolMonitorService...
			ThreadPoolMonitorServiceImpl tpms = new ThreadPoolMonitorServiceImpl(
					MonitoringDelaySeconds, threadGroup, poolName);
			tpms.setExecutor(threadPoolExecutor);

			// ThreadPoolMonitorService is started...
			Thread monitor = new Thread(tpms);
			monitor.start();
		}
		return threadPoolExecutor;
	}
}
