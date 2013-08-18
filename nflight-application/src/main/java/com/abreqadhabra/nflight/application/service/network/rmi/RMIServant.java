package com.abreqadhabra.nflight.application.service.network.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServant extends Remote {

	String sayHello() throws RemoteException;

}
