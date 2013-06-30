package com.abreqadhabra.nflight.app.server.rmi.remote.dao;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import com.abreqadhabra.nflight.app.server.rmi.remote.client.RemoteClient;

/**
 * RemoteDataDAO represents the handle for a remote data. 
 * <p>
 * 
 * @author Dongsup Kim
 * @version 0.8 10-Jan-2007
 */
public interface RemoteDataDAO extends Remote {

    public void register(RemoteClient remoteClient) throws RemoteException;

    public void unregister(RemoteClient remoteClient) throws RemoteException;

    public Vector getClients() throws RemoteException;
}