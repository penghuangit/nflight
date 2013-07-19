package com.abreqadhabra.nflight.service.core.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IService extends Remote {

	
	public String getServiceName() throws RemoteException;

	public boolean isRunning() throws RemoteException;

	public String sayHello() throws RemoteException;

}
