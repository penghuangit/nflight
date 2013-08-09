package com.abreqadhabra.nflight.application.server.concurrent.runnable;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ServiceRunnable implements Runnable {
	private static final Class<ServiceRunnable> THIS_CLAZZ = ServiceRunnable.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	String serviceName;

	public ServiceRunnable(String serviceName) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		this.serviceName = serviceName;

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				LoggingHelper.describe(THIS_CLAZZ));
	}

	@Override
	public void run() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();


		final Thread t = Thread.currentThread();
		final String name = t.getName();
	
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"Executing Runnable");
		
		try {
			for (int i = 0; i < 2; i++) {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "\n"+serviceName + "\t[" + i + "]\t"+ name +"\t"+ this);
				
			}
			Thread.sleep(2000);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}
}