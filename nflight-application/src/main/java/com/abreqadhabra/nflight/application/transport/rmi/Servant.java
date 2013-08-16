package com.abreqadhabra.nflight.application.transport.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.abreqadhabra.nflight.common.exception.NFlightException;

public interface Servant extends Remote {

	String sayHello() throws RemoteException;

	void stop() throws NFlightException;

}
