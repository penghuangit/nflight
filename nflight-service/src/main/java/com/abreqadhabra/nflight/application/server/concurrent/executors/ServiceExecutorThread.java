package com.abreqadhabra.nflight.application.server.concurrent.executors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.Globals.SERVICE_NAME;
import com.abreqadhabra.nflight.application.server.concurrent.runnable.MultiRunnable;
import com.abreqadhabra.nflight.application.server.concurrent.runnable.ServiceRunnable;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

class RejectedExecutionHandelerImpl implements RejectedExecutionHandler {
	private static final Class<RejectedExecutionHandelerImpl> THIS_CLAZZ = RejectedExecutionHandelerImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public RejectedExecutionHandelerImpl() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				LoggingHelper.describe(THIS_CLAZZ));
	}

	@Override
	public void rejectedExecution(final Runnable runnable,
			final ThreadPoolExecutor executor) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				runnable.toString() + " : I've been rejected ! ");
	}
}

public class ServiceExecutorThread {
	private static final Class<ServiceExecutorThread> THIS_CLAZZ = ServiceExecutorThread.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public ServiceExecutorThread() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				LoggingHelper.describe(THIS_CLAZZ));
	}

	public void execute() {
		final BlockingQueue<Runnable> worksQueue = new ArrayBlockingQueue<Runnable>(
				2);
		final RejectedExecutionHandler executionHandler = new RejectedExecutionHandelerImpl();
		final ThreadPoolExecutor executor = new ThreadPoolExecutor(12, 24, 10,
				TimeUnit.SECONDS, worksQueue, executionHandler);
		executor.allowCoreThreadTimeOut(true);

		final List<Runnable> taskGroup = new ArrayList<Runnable>();

		for (SERVICE_NAME serviceName : SERVICE_NAME.values()) {
			taskGroup.add(new ServiceRunnable(serviceName.toString()));
		}

		executor.execute(new MultiRunnable(taskGroup));
	}
}