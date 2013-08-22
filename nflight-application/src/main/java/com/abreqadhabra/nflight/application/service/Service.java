package com.abreqadhabra.nflight.application.service;

import java.rmi.RemoteException;

import com.abreqadhabra.nflight.common.exception.NFlightException;

public interface Service {
	public void behavior()  throws  RemoteException ;

	public boolean status()  throws  RemoteException ;

	public void shutdown() throws  RemoteException, NFlightException ;
	
	String sayHello() throws  RemoteException;
}
