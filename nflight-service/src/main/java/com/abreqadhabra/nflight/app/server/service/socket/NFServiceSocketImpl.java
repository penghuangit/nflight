package com.abreqadhabra.nflight.app.server.service.socket;

import java.rmi.RemoteException;

import com.abreqadhabra.nflight.app.server.service.IService;

public class NFServiceSocketImpl implements IService {


	@Override
	public boolean isRunning() throws RemoteException {
		return true;
	}

	@Override
	public String sayHello() throws RemoteException {
		return "Hello, world!";
	}

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startup() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean status() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void shutdown() throws Exception {
		// TODO Auto-generated method stub
		
	}


}
