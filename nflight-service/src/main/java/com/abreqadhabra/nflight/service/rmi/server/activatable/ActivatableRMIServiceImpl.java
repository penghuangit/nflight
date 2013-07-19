package com.abreqadhabra.nflight.service.rmi.server.activatable;

import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationID;

import com.abreqadhabra.nflight.service.core.Profile;
import com.abreqadhabra.nflight.service.rmi.server.AbstractRMIService;

public class ActivatableRMIServiceImpl extends AbstractRMIService {

	public ActivatableRMIServiceImpl(ActivationID id, MarshalledObject<?> data)
			throws RemoteException {

		// Register the object with the activation system
		// then export it on an anonymous port
		//
		Activatable.exportObject(this, id, 0);

	}

	public ActivatableRMIServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getServiceName() throws RemoteException {
		return Profile.RMI_SERVICE.activatable.toString();
	}

	@Override
	public boolean isRunning() throws RemoteException {
		return true;
	}

	@Override
	public String sayHello() throws RemoteException {
		return "Hello, world!";
	}

}
