package com.abreqadhabra.nflight.common.concurrency.executor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {
	private static Class<RejectedExecutionHandlerImpl> THIS_CLAZZ = RejectedExecutionHandlerImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	@Override
	public void rejectedExecution(Runnable runnable,
			ThreadPoolExecutor executor) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				runnable.toString() + " : has been rejected");
	}
}