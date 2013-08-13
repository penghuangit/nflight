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



public class ServiceExecutorThread {
	private static Class<ServiceExecutorThread> THIS_CLAZZ = ServiceExecutorThread.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public ServiceExecutorThread() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				LoggingHelper.describe(THIS_CLAZZ));
	}

	public void execute() {
		BlockingQueue<Runnable> worksQueue = new ArrayBlockingQueue<Runnable>(
				2);
		RejectedExecutionHandler executionHandler = new RejectedExecutionHandelerImpl();
		ThreadPoolExecutor executor = new ThreadPoolExecutor(12, 24, 10,
				TimeUnit.SECONDS, worksQueue, executionHandler);
		executor.allowCoreThreadTimeOut(true);

		List<Runnable> taskGroup = new ArrayList<Runnable>();

		for (SERVICE_NAME serviceName : SERVICE_NAME.values()) {
			taskGroup.add(new ServiceRunnable(serviceName.toString()));
		}

		executor.execute(new MultiRunnable(taskGroup));
	}
}