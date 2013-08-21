package com.abreqadhabra.nflight.application.service.network.rmi;

import java.io.IOException;

import com.abreqadhabra.nflight.application.service.ServiceDescriptor;
import com.abreqadhabra.nflight.application.service.conf.ServiceConfig.ENUM_SERVICE_TYPE;
import com.abreqadhabra.nflight.common.exception.NFlightException;

public class RMIServiceDescriptor extends ServiceDescriptor {

	private String hostAddress;
	private int port;
	
	public RMIServiceDescriptor(ENUM_SERVICE_TYPE serviceType)
			throws NFlightException, IOException {
		super(serviceType);
		this.hostAddress = endpoint.getAddress().getHostAddress();
		this.port = endpoint.getPort();
	}

	public String getHostAddress() {
		return this.hostAddress;
	}

	public int getPort() {
		return this.port;
	}
	
}