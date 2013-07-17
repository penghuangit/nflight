package com.abreqadhabra.nflight.service.server;

import java.rmi.Remote;
import java.rmi.RemoteException;



public interface NFService extends Remote{

	public boolean isRunning() throws RemoteException;
    String sayHello() throws RemoteException;

}