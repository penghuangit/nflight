package com.abreqadhabra.nflight.application.service.thread;

import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ServiceThreadFactory implements ThreadFactory {
	private static Class<ServiceThreadFactory> THIS_CLAZZ = ServiceThreadFactory.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	
	@Override
	public Thread newThread(Runnable r) {
		final Thread CURRENT_THREAD = Thread.currentThread();
		final String METHOD_NAME = CURRENT_THREAD.getStackTrace()[1]
				.getMethodName();
		
		LOGGER.logp(Level.FINER, CLAZZ_NAME, METHOD_NAME, "---------------------->Runnable is: "
				+ r.getClass().getName());
		
		return new ServiceThread(r);
	}

}
