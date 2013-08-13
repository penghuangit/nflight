package com.abreqadhabra.nflight.application.server.concurrent.executors;

import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.Globals.SERVICE_NAME;
import com.abreqadhabra.nflight.application.server.concurrent.ServiceForkJoinTask;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ServiceExecutorForkJoin {
	private static Class<ServiceExecutorForkJoin> THIS_CLAZZ = ServiceExecutorForkJoin.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	ForkJoinPool pool;

	public ServiceExecutorForkJoin() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				LoggingHelper.describe(THIS_CLAZZ));
	}

	public void execute() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// Check the number of available processors
		int processors = Runtime.getRuntime().availableProcessors();
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"No of processors: " + processors);

		pool = new ForkJoinPool(processors);

		for (SERVICE_NAME serviceName : SERVICE_NAME.values()) {
			pool.invoke(new ServiceForkJoinTask(serviceName.toString()));

		}
		
		System.out.println("ActiveThreadCount" + pool.getActiveThreadCount());
		System.out.println("PoolSize" + pool.getPoolSize());
	}
}