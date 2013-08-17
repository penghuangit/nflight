package com.abreqadhabra.nflight.application.trash_dispatcher;

public abstract class ServiceDescriptor {

	String name;
	String host;
	int port;
	public ServiceDescriptor(String name, String host, int port) {
		this.name = name;
		this.host = host;
		this.port = port;
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHost() {
		return this.host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return this.port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "ServiceDescriptor [name=" + this.name + ", host=" + this.host
				+ ", port=" + this.port + ", getName()=" + this.getName()
				+ ", getHost()=" + this.getHost() + ", getPort()="
				+ this.getPort() + ", getClass()=" + this.getClass()
				+ ", hashCode()=" + this.hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	
	
}
