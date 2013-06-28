package com.abreqadhabra.nflight.app.server.rmi.remote.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteClient extends Remote {
	public void serverShuttingDown() throws RemoteException;
}
