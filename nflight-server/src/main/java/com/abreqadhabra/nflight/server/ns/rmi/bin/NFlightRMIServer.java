package com.abreqadhabra.nflight.server.ns.rmi.bin;

import java.rmi.RemoteException;

import com.abreqadhabra.nflight.server.ns.rmi.INFlightRMIServer;


public class NFlightRMIServer implements INFlightRMIServer, Runnable{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		System.out.println("Hello World!");

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void check() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean checkStatus() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

}
