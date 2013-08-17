package com.abreqadhabra.nflight.application.service.socket;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.concurrent.AbstractRunnable;
import com.abreqadhabra.nflight.application.common.concurrent.thread.ThreadHelper;
import com.abreqadhabra.nflight.application.common.launcher.Configure;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public abstract class AbstractSocketService extends AbstractRunnable
		implements
			SocketService {
	private static Class<AbstractSocketService> THIS_CLAZZ = AbstractSocketService.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	protected boolean isRunning;
	protected Configure configure;
	
	public AbstractSocketService(boolean isRunning, Configure configure) {
		this.isRunning = isRunning;
		this.configure = configure;
		this.setShutdownHook();
	}

	@Override
	protected void setShutdownHook() {
		super.setShutdownHookThread(new Thread(this
				.getShutdownHook(this.isRunning)));
	}

	protected ThreadPoolExecutor getThreadPoolExecutor(
			String threadPoolNameKey,
			String threadPoolMonitoringDelaySecondsKey,
			String isThreadPoolMonitoringKey) {
		String threadPoolName = this.configure.get(threadPoolNameKey);
		int threadPoolMonitoringDelaySeconds = this.configure
				.getInt(threadPoolMonitoringDelaySecondsKey);
		boolean isThreadPoolMonitoring = this.configure
				.getBoolean(threadPoolMonitoringDelaySecondsKey);
		return ThreadHelper.getThreadPoolExecutor(threadPoolName,
				isThreadPoolMonitoring, threadPoolMonitoringDelaySeconds);
	}

	private Runnable getShutdownHook(boolean isRunning) {
		return new Runnable() {
			public Runnable init(boolean isRunning) {
				return (this);
			}

			@Override
			public void run() {
				final Thread CURRENT_THREAD = Thread.currentThread();
				final String METHOD_NAME = CURRENT_THREAD.getStackTrace()[1]
						.getMethodName();
				LOGGER.logp(
						Level.FINER,
						CLAZZ_NAME,
						METHOD_NAME,
						"current thread is "
								+ ThreadHelper.getThreadName(CURRENT_THREAD));
				try {
					LOGGER.logp(Level.SEVERE, CLAZZ_NAME, METHOD_NAME,
							"Stopping...");
					AbstractSocketService.this.stop();
					LOGGER.logp(Level.SEVERE, CLAZZ_NAME, METHOD_NAME,
							"Stopped");
				} catch (Exception e) {
					StackTraceElement[] current = e.getStackTrace();
					if (e instanceof NFlightException) {
						NFlightException ne = (NFlightException) e;
						LOGGER.logp(Level.SEVERE, current[0].getClassName(),
								current[0].getMethodName(), "\n"
										+ NFlightException.getStackTrace(ne));
						ThreadHelper.interrupt(Thread.currentThread());
					} else {
						e.printStackTrace();
						ThreadHelper.shutdown();
					}
				}
			}
		}.init(isRunning);
	}
}