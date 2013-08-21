package com.abreqadhabra.nflight.application.service.container.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.Config;
import com.abreqadhabra.nflight.application.common.launcher.LauncherHelper;
import com.abreqadhabra.nflight.application.common.launcher.concurrent.thread.ThreadHelper;
import com.abreqadhabra.nflight.application.service.ServiceFactory;
import com.abreqadhabra.nflight.application.service.conf.ServiceConfig;
import com.abreqadhabra.nflight.application.service.conf.ServiceConfig.ENUM_SERVICE_TYPE;
import com.abreqadhabra.nflight.application.service.container.Container;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class NetworkContainerImpl implements Container {
	private static Class<NetworkContainerImpl> THIS_CLAZZ = NetworkContainerImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private HashMap<String, Runnable> services = new HashMap<String, Runnable>();
	boolean isRunning;

	public NetworkContainerImpl() throws NFlightException,
			IOException {

		// 시스템프로퍼티 등록
		Config.load(THIS_CLAZZ, ServiceConfig.PATH_SERVICE_PROPERTIES);

		this.isRunning = true;
		this.init();

		LauncherHelper.setSecurityManager();// 추후 삭제
	}

	@Override
	public void startupService(ENUM_SERVICE_TYPE serviceType) {
		
	}
	@Override
	public void startupAllService() {
		ThreadGroup serviceThreadGroup = new ThreadGroup(
				"NF-Service-ThreadGroup");
		for (String serviceType : this.services.keySet()) {
			new Thread(serviceThreadGroup, this.services.get(serviceType),
					serviceType).start();
		}
	}
	
	@Override
	public void shutdown(ENUM_SERVICE_TYPE serviceType) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void shutdownAllService() {
		for (String serviceType : this.services.keySet()) {
			new Thread(this.services.get(serviceType),
					serviceType).start();
		}
	}

	private void init() throws NFlightException, IOException {
		Runnable serviceRunnable = null;
		for (ENUM_SERVICE_TYPE serviceType : ENUM_SERVICE_TYPE.values()) {
			serviceRunnable = (Runnable) ServiceFactory.createtServiceTask(serviceType);
			this.services.put(serviceType.toString(), serviceRunnable);
		}
	}

	public static void main(String[] args) {
		try {
			NetworkContainerImpl container = new NetworkContainerImpl();
			
			container.startupAllService();
			
			container.shutdownAllService();
			
		} catch (Exception e) {
			StackTraceElement[] current = e.getStackTrace();
			if (e instanceof NFlightException) {
				NFlightException ne = (NFlightException) e;
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(),
						"\n" + NFlightException.getStackTrace(ne));
				ThreadHelper.interrupt(Thread.currentThread());
			} else {
				e.printStackTrace();
				ThreadHelper.shutdown();
			}
		}
	}




}