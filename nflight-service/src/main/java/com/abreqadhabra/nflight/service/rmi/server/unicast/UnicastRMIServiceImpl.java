package com.abreqadhabra.nflight.service.rmi.server.unicast;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.Profile;
import com.abreqadhabra.nflight.service.rmi.server.AbstractRMIService;

public class UnicastRMIServiceImpl extends AbstractRMIService {

	private static final Class<UnicastRMIServiceImpl> THIS_CLAZZ = UnicastRMIServiceImpl.class;
	private Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public UnicastRMIServiceImpl() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Instantiating a " + THIS_CLAZZ.getSimpleName() + " Class ");
	}

	@Override
	public String getServiceName() throws RemoteException {
		return Profile.RMI_SERVICE.unicast.toString();
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