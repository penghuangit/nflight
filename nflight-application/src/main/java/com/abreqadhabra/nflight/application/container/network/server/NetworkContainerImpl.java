package com.abreqadhabra.nflight.application.container.network.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.concurrent.thread.ThreadHelper;
import com.abreqadhabra.nflight.application.common.launcher.Configure;
import com.abreqadhabra.nflight.application.common.launcher.Configure.SERVICE_TYPE;
import com.abreqadhabra.nflight.application.common.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.application.common.launcher.LauncherHelper;
import com.abreqadhabra.nflight.application.container.Container;
import com.abreqadhabra.nflight.application.service.ServiceFactory;
import com.abreqadhabra.nflight.application.service.NetworkServiceFactory;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class NetworkContainerImpl implements Container {
	private static Class<NetworkContainerImpl> THIS_CLAZZ = NetworkContainerImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private HashMap<String, Runnable> services = new HashMap<String, Runnable>();
	boolean isRunning;
	private Configure configure;

	public NetworkContainerImpl(Configure configure) throws NFlightException,
			NFlightRemoteException {
		this.isRunning = true;
		this.configure = configure;
		this.init(this.isRunning, configure);

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

	private void init(boolean isRunning, Configure configure)
			throws NFlightException, NFlightRemoteException {
		this.addServices(Configure.SERVICE_TYPE.network_blocking);
		this.addServices(Configure.SERVICE_TYPE.network_nonblocking);
		this.addServices(Configure.SERVICE_TYPE.network_async);
		this.addServices(Configure.SERVICE_TYPE.network_unicast);
		this.addServices(Configure.SERVICE_TYPE.network_multicast);
		this.addServices(Configure.SERVICE_TYPE.rmi_unicast);
		this.addServices(Configure.SERVICE_TYPE.rmi_activation);
	}

	private void addServices(SERVICE_TYPE serviceType) throws NFlightException,
			NFlightRemoteException {
		Runnable service = null;
		int port = 0;
		switch (serviceType) {
			case network_blocking :
				service = NetworkServiceFactory.getServiceFactory(serviceType,
						configure).createService();
				break;
			case network_nonblocking :
				service = NetworkServiceFactory.getServiceFactory(serviceType,
						configure).createService();
				break;
			case network_async :
				service = NetworkServiceFactory.getServiceFactory(serviceType,
						configure).createService();
				break;
			case network_unicast :
				service = NetworkServiceFactory.getServiceFactory(serviceType,
						configure).createService();
				break;
			case network_multicast :
				service = NetworkServiceFactory.getServiceFactory(serviceType,
						configure).createService();
				break;
			case rmi_unicast :
				service = NetworkServiceFactory.getServiceFactory(serviceType,
						configure).createService();
				break;
			case rmi_activation :
				service = NetworkServiceFactory.getServiceFactory(serviceType,
						configure).createService();
				break;
			default :
				break;
		}
		this.services.put(serviceType.toString(), service);
	}
	
	private InetSocketAddress getEndpoint(int port) {
		return this.getEndpoint(null, port);
	}

	private InetSocketAddress getEndpoint(InetAddress addr, int port) {
		try {
			if (addr == null) {
				addr = InetAddress.getLocalHost();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return new InetSocketAddress(addr, port);
	}

	public static void main(String[] args) {
		try {
			Configure configure = new ConfigureImpl(THIS_CLAZZ,
					Configure.FILE_SERVICE_PROPERTIES);
			NetworkContainerImpl container = new NetworkContainerImpl(configure);
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