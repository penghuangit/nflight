package com.abreqadhabra.nflight.application.service.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServant extends Remote {

	String sayHello() throws RemoteException;

}
