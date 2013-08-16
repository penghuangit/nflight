package com.abreqadhabra.nflight.application.service.temp.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.transport.network.Service;
import com.abreqadhabra.nflight.common.concurrency.thread.ThreadHelper;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.launcher.Configure;
import com.abreqadhabra.nflight.common.launcher.Configure.SERVICE_TYPE;
import com.abreqadhabra.nflight.common.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ServiceContainer implements INetworkService {
	private static Class<ServiceContainer> THIS_CLAZZ = ServiceContainer.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private ServiceFactory serviceFactory = new ServiceFactory();
	private HashMap<String, Runnable> services = new HashMap<String, Runnable>();
	boolean isRunning;
	private Configure configure;

	public ServiceContainer(Configure configure) throws NFlightException {
		this.isRunning = true;
		this.configure = configure;
		this.init(this.isRunning, configure);
	}

	private void init(boolean isRunning, Configure configure)
			throws NFlightException {
		this.addServices(Configure.SERVICE_TYPE.network_blocking);
		this.addServices(Configure.SERVICE_TYPE.network_nonblocking);
		this.addServices(Configure.SERVICE_TYPE.network_async);
		this.addServices(Configure.SERVICE_TYPE.network_unicast);
		this.addServices(Configure.SERVICE_TYPE.network_multicast);
		this.addServices(Configure.SERVICE_TYPE.rmi_unicast);
		this.addServices(Configure.SERVICE_TYPE.rmi_activation);
	}

	private void addServices(SERVICE_TYPE serviceType) throws NFlightException {
		Runnable service = null;
		int port = 0;
		switch (serviceType) {
			case network_blocking :
				port = this.configure.getInt(Configure.BLOCKING_DEFAULT_PORT);
				service = this.getAcceptorFactory().createBlockingService(
						this.isRunning, this.configure, this.getEndpoint(port));
				break;
			case network_nonblocking :
				port = this.configure
						.getInt(Configure.NONBLOCKING_DEFAULT_PORT);
				service = this.getAcceptorFactory().createNonBlockingService(
						this.isRunning, this.configure, this.getEndpoint(port));
				break;
			case network_async :
				port = this.configure.getInt(Configure.ASYNC_DEFAULT_PORT);
				service = this.getAcceptorFactory().createAsyncService(
						this.isRunning, this.configure, this.getEndpoint(port));
				break;
			case network_unicast :
				port = this.configure.getInt(Configure.UNICAST_DEFAULT_PORT);
				service = this.getAcceptorFactory().createUnicastService(
						this.isRunning, this.configure, this.getEndpoint(port));
				break;
			case network_multicast :
				port = this.configure.getInt(Configure.MULTICAST_DEFAULT_PORT);
				service = this.getAcceptorFactory().createMulticastService(
						this.isRunning, this.configure, this.getEndpoint(port));
				break;
			case rmi_unicast :
				port = this.configure
						.getInt(Configure.RMI_DEFAULT_PORT);
				service = this.getAcceptorFactory().createRMIUnicastService(
						this.configure, this.getEndpoint(port));
				break;
			case rmi_activation :
				port = this.configure
						.getInt(Configure.RMI_DEFAULT_PORT);
				service = this.getAcceptorFactory().createRMIMulticastService(
						this.configure, this.getEndpoint(port));

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

	@Override
	public void startup() {
		ThreadGroup serviceThreadGroup = new ThreadGroup(
				"NF-Service-ThreadGroup");
		for (String serviceType : this.services.keySet()) {
			new Thread(serviceThreadGroup,
					(Runnable) this.services.get(serviceType), serviceType)
					.start();
		}
	}

	public ServiceFactory getAcceptorFactory() {
		return this.serviceFactory;
	}

	public void setAcceptorFactory(ServiceFactory acceptorFactory) {
		this.serviceFactory = acceptorFactory;
	}

	public static void main(String[] args) {
		try {
			Configure configure = new ConfigureImpl(THIS_CLAZZ,
					Configure.FILE_SERVICE_PROPERTIES);
			ServiceContainer container = new ServiceContainer(configure);
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
