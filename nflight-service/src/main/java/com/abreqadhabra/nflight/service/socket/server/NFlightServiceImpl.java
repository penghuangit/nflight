package com.abreqadhabra.nflight.service.socket.server;

import java.rmi.RemoteException;

import com.abreqadhabra.nflight.service.core.NFlightService;

public class NFlightServiceImpl implements NFlightService {

	@Override
	public boolean isRunning() throws RemoteException {
		return true;
	}

	@Override
	public String sayHello() throws RemoteException {
		return "Hello, world!";
	}

}
