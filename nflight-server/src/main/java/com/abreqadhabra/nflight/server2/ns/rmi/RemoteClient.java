package com.abreqadhabra.nflight.server2.ns.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteClient extends Remote {
	public void serverShuttingDown() throws RemoteException;
}
