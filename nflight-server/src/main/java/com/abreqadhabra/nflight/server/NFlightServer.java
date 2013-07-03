package com.abreqadhabra.nflight.server;

import java.rmi.Remote;

public interface NFlightServer extends Remote{
	
	public static final String NFLIGHT_SERVER_PROPERTY_KEY = "nflight.server";
	public static final String NFLIGHT_SERVER_LOCALHOST = "localhost";

	public abstract void startup() throws Exception;

	public abstract void shutdown() throws Exception;

	public abstract boolean checkHealth()  throws Exception;

	public abstract void exec() throws Exception;

	public abstract void exit() throws Exception;

}
