package com.abreqadhabra.nflight.application.service.rmi;

import java.net.InetAddress;
import java.rmi.registry.Registry;

import com.abreqadhabra.nflight.application.launcher.Configure;

public abstract class AbstractRMIServant implements RMIServant, Runnable {

	protected Configure configure;
	protected Registry registry;
	protected String boundName;
	public InetAddress addr;
	public int port;
	
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

}
