package com.abreqadhabra.nflight.application.service.network.socket;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.concurrent.AbstractServiceCallable;
import com.abreqadhabra.nflight.application.common.launcher.concurrent.thread.ThreadHelper;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public abstract class AbstractSocketServiceTask extends AbstractServiceCallable {
	private static Class<AbstractSocketServiceTask> THIS_CLAZZ = AbstractSocketServiceTask.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	protected boolean isRunning;

	public AbstractSocketServiceTask(boolean isRunning) {
		this.isRunning = isRunning;
		this.setShutdownHook();
	}

	@Override
	protected void setShutdownHook() {
		super.setShutdownHookThread(new Thread(this
				.getShutdownHook(this.isRunning)));
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
					AbstractSocketServiceTask.this.shutdown();
					LOGGER.logp(Level.SEVERE, CLAZZ_NAME, METHOD_NAME,
							"Stopped");
				} catch (Exception e) {
					StackTraceElement[] current = e.getStackTrace();
					if (e instanceof NFlightException) {
						NFlightException ne = (NFlightException) e;
						LOGGER.logp(Level.SEVERE, current[0].getClassName(),
								current[0].getMethodName(), "\n"
										+ NFlightException.getStackTrace(ne));
						ThreadHelper.interrupt(CLAZZ_NAME, Thread.currentThread());
					} else {
						e.printStackTrace();
						ThreadHelper.shutdown();
					}
				}
			}
		}.init(isRunning);
	}

}
