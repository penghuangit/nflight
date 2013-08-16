package com.abreqadhabra.nflight.application.transport.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServant extends Remote {

	String sayHello() throws RemoteException;

}
