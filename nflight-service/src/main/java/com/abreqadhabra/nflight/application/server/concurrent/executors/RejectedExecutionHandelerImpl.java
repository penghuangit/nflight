package com.abreqadhabra.nflight.application.server.concurrent.executors;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class RejectedExecutionHandelerImpl implements RejectedExecutionHandler {
	private static Class<RejectedExecutionHandelerImpl> THIS_CLAZZ = RejectedExecutionHandelerImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public RejectedExecutionHandelerImpl() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				LoggingHelper.describe(THIS_CLAZZ));
	}

	@Override
	public void rejectedExecution(Runnable runnable,
			ThreadPoolExecutor executor) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				runnable.toString() + " : I've been rejected ! ");
	}
}