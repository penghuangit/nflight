package com.abreqadhabra.nflight.application.server.concurrent.runnable;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ServiceRunnable implements Runnable {
	private static Class<ServiceRunnable> THIS_CLAZZ = ServiceRunnable.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	String serviceName;

	public ServiceRunnable(String serviceName) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		this.serviceName = serviceName;

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				LoggingHelper.describe(THIS_CLAZZ));
	}

	@Override
	public void run() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();


		Thread t = Thread.currentThread();
		String name = t.getName();
	
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"Executing Runnable");
		
		try {
			for (int i = 0; i < 2; i++) {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "\n"+serviceName + "\t[" + i + "]\t"+ name +"\t"+ this);
				
			}
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}