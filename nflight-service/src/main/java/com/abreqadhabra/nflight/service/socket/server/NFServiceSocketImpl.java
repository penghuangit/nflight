package com.abreqadhabra.nflight.service.socket.server;

import java.rmi.RemoteException;

import com.abreqadhabra.nflight.service.core.NFService;

public class NFServiceSocketImpl implements NFService {

	@Override
	public boolean isRunning() throws RemoteException {
		return true;
	}

	@Override
	public String sayHello() throws RemoteException {
		return "Hello, world!";
	}

}
