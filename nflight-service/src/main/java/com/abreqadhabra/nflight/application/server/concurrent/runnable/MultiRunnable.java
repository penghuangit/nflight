package com.abreqadhabra.nflight.application.server.concurrent.runnable;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class MultiRunnable implements Runnable {
	private static final Class<MultiRunnable> THIS_CLAZZ = MultiRunnable.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	
	private final List<Runnable> runnables;

	public MultiRunnable(final List<Runnable> runnables) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				LoggingHelper.describe(THIS_CLAZZ));
		
		this.runnables = runnables;
	}

	@Override
	public void run() {
		for (final Runnable runnable : runnables) {
			runnable.run();
		}
	}
}