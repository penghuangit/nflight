package com.abreqadhabra.nflight.application.service.network.rmi;

import java.rmi.Remote;

import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;

public interface RMIServant extends Remote{

	public void startup() throws NFlightRemoteException ;

	public boolean status()  throws  NFlightRemoteException ;

	public void shutdown() throws  NFlightRemoteException ;
	
	String sayHello() throws  NFlightRemoteException;

}
