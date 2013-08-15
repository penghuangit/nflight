package com.abreqadhabra.nflight.common.concurrency.thread;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.Env;
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
}
