package com.abreqadhabra.nflight.application.service.thread;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ServiceThread extends Thread {
	private static Class<ServiceThread> THIS_CLAZZ = ServiceThread.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public ServiceThread(Runnable target) {
		super(target);
		final Thread CURRENT_THREAD = Thread.currentThread();
		final String METHOD_NAME = CURRENT_THREAD.getStackTrace()[1]
				.getMethodName();
		LOGGER.logp(Level.FINER, CLAZZ_NAME, METHOD_NAME, "---------------------->Runnable is: "
				+ target.getClass().getName());
	}

}
