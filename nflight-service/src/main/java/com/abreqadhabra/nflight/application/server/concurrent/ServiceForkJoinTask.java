package com.abreqadhabra.nflight.application.server.concurrent;

import java.util.concurrent.RecursiveTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ServiceForkJoinTask extends RecursiveTask<Object> {
	private static Class<ServiceForkJoinTask> THIS_CLAZZ = ServiceForkJoinTask.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private static long serialVersionUID = 1L;

	String serviceName;

	public ServiceForkJoinTask(String serviceName) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		this.serviceName = serviceName;
//
//		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
//				LoggingHelper.describe(THIS_CLAZZ));
//		

		
	}

	@Override
	protected Object compute() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		
		Thread t = Thread.currentThread();
		String name = t.getName();

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					  name + "\t" + this.serviceName + "\t" 
							+ this);


		return null;

	}

}
