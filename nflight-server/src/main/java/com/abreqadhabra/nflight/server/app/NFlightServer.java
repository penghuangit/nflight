package com.abreqadhabra.nflight.server.app;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NFlightServer extends Remote{
	
	public static final String NFLIGHT_SERVER_PROPERTY_KEY = "nflight.server";
	public static final String NFLIGHT_SERVER_LOCALHOST = "localhost";

	public abstract void startup() throws RemoteException;

	public abstract void shutdown() throws RemoteException;

	public abstract boolean checkHealth()  throws RemoteException;

}
