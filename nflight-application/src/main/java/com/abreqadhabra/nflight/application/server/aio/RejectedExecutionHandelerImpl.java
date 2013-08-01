package com.abreqadhabra.nflight.application.server.aio;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class RejectedExecutionHandelerImpl implements RejectedExecutionHandler {
	private static final Class<RejectedExecutionHandelerImpl> THIS_CLAZZ = RejectedExecutionHandelerImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	@Override
	public void rejectedExecution(final Runnable runnable,
			final ThreadPoolExecutor executor) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				runnable.toString() + " : I've been rejected ! ");
	}
}