package com.abreqadhabra.nflight.service.core;

import java.rmi.Remote;
import java.rmi.RemoteException;



public interface NFlightService extends Remote{

	public boolean isRunning() throws RemoteException;
    String sayHello() throws RemoteException;

}
