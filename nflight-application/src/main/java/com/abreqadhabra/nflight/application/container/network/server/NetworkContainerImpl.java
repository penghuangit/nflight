package com.abreqadhabra.nflight.application.container.network.server;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.Config;
import com.abreqadhabra.nflight.application.common.launcher.LauncherHelper;
import com.abreqadhabra.nflight.application.common.launcher.concurrent.thread.ThreadHelper;
import com.abreqadhabra.nflight.application.container.Container;
import com.abreqadhabra.nflight.application.service.ServiceFactory;
import com.abreqadhabra.nflight.application.service.conf.ServiceConfiguration;
import com.abreqadhabra.nflight.application.service.conf.ServiceConfiguration.SERVICE_TYPE;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class NetworkContainerImpl implements Container {
	private static Class<NetworkContainerImpl> THIS_CLAZZ = NetworkContainerImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private HashMap<String, Runnable> services = new HashMap<String, Runnable>();
	boolean isRunning;

	public NetworkContainerImpl() throws NFlightException,
			NFlightRemoteException {

		// 시스템프로터피 등록
		Config.load(THIS_CLAZZ, ServiceConfiguration.FILE_SERVICE_PROPERTIES);

		this.isRunning = true;
		this.init();

		LauncherHelper.setSecurityManager();// 추후 삭제
	}

	@Override
	public void startup() {
		ThreadGroup serviceThreadGroup = new ThreadGroup(
				"NF-Service-ThreadGroup");
		for (String serviceType : this.services.keySet()) {
			new Thread(serviceThreadGroup, this.services.get(serviceType),
					serviceType).start();
		}
	}

	private void init() throws NFlightException,
			NFlightRemoteException {
		Runnable service = null;
		for (SERVICE_TYPE serviceType : SERVICE_TYPE.values()) {
			service = ServiceFactory.getServiceFactory(serviceType)
					.createService();
			this.services.put(serviceType.toString(), service);
		}
	}

	public static void main(String[] args) {
		try {
			NetworkContainerImpl container = new NetworkContainerImpl();
			container.startup();
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

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}
}