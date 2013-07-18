package com.abreqadhabra.nflight.service.core.server;

import java.rmi.Remote;
import java.rmi.RemoteException;



public interface IService extends Remote{

	public boolean isRunning() throws RemoteException;
    String sayHello() throws RemoteException;

}
