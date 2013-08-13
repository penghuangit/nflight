package com.abreqadhabra.nflight.application.service.rmi;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.rmi.activatable.ActivatableRMIServantImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public abstract class AbstractRMIServant implements RMIServant, Runnable {
	private static Class<ActivatableRMIServantImpl> THIS_CLAZZ = ActivatableRMIServantImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	protected Configure configure;
	public InetAddress addr;
	public int port;

	protected Registry registry;
	protected String boundName;
	protected String serviceName;

	public AbstractRMIServant() {
	}

	public AbstractRMIServant(Configure configure, InetAddress addr, int port)
			throws Exception {
		this.configure = configure;
		this.addr = addr;
		this.port = port;
		this.registry = RMIServiceHelper.getRegistry(
				this.addr.getHostAddress(), this.port);
	}

	@Override
	public String sayHello() throws RemoteException {
		return "Hello, world!";
	}
}
