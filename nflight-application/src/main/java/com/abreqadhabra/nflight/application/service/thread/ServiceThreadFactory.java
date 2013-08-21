package com.abreqadhabra.nflight.application.service.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ServiceThreadFactory implements ThreadFactory {
	private static Class<ServiceThreadFactory> THIS_CLAZZ = ServiceThreadFactory.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private String serviceName;
	private final AtomicInteger sequence = new AtomicInteger(1);

	public ServiceThreadFactory(String serviceName) {
		this.serviceName = serviceName.toLowerCase().replace(".", "-");
	}

	@Override
	public Thread newThread(Runnable target) {
		final Thread CURRENT_THREAD = Thread.currentThread();
		final String METHOD_NAME = CURRENT_THREAD.getStackTrace()[1]
				.getMethodName();

		ThreadGroup group = new ThreadGroup(serviceName);
		String name = "nflight-" + serviceName + "-" + sequence.getAndIncrement();

		LOGGER.logp(Level.FINER, CLAZZ_NAME, METHOD_NAME, "Thread name : "
				+ name);

		return new ServiceThread(group, target, name);
	}

}
