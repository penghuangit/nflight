package com.abreqadhabra.nflight.service.rmi.server.activatable;

import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.Profile;
import com.abreqadhabra.nflight.service.rmi.server.AbstractRMIService;
import com.abreqadhabra.nflight.service.rmi.server.unicast.UnicastRMIServiceImpl;

public class ActivatableRMIServiceImpl extends AbstractRMIService {

	private static final Class<ActivatableRMIServiceImpl> THIS_CLAZZ = ActivatableRMIServiceImpl.class;
	private Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	
	public ActivatableRMIServiceImpl(ActivationID id, MarshalledObject<?> data)
			throws RemoteException {

		// Register the object with the activation system
		// then export it on an anonymous port
		//
		Activatable.exportObject(this, id, 0);
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Instantiating a " + THIS_CLAZZ.getSimpleName() + " Class ");
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
