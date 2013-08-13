package com.abreqadhabra.nflight.app.server.service;

public class ServiceDescriptor {

	String serviceName;
	String host;
	String address;
	int port;
	String codeBase;

	public String getAddress() {
		return this.address;
	}

	public String getCodeBase() {
		return this.codeBase;
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCodeBase(String codeBase) {
		this.codeBase = codeBase;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
