package com.abreqadhabra.nflight.server;

import java.rmi.Remote;

public interface NFlightServer extends Remote{
	
	public static final String NFLIGHT_SERVER_PROPERTY_KEY = "nflight.server";
	public static final String NFLIGHT_SERVER_LOCALHOST = "localhost";

	void startup() throws Exception;
	public abstract void shutdown() throws Exception;
	public boolean checkStatus() throws Exception;
	public abstract boolean checkHealth()  throws Exception;


}
