package com.abreqadhabra.nflight.service.socket.server;

import java.rmi.RemoteException;

import com.abreqadhabra.nflight.service.core.server.IService;

public class NFServiceSocketImpl implements IService {

	@Override
	public boolean isRunning() throws RemoteException {
		return true;
	}

	@Override
	public String sayHello() throws RemoteException {
		return "Hello, world!";
	}

}
