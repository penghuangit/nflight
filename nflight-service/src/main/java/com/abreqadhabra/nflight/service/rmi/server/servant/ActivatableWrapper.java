package com.abreqadhabra.nflight.service.rmi.server.servant;

import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationID;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.rmi.server.scoket.SecureSocketFactory;
import com.abreqadhabra.nflight.service.rmi.server.servant.activation.setup.ActivatableTest;

public class ActivatableWrapper extends Activatable implements Runnable {

	private static final long serialVersionUID = 1L;

	private static final Class<ActivatableTest> THIS_CLAZZ = ActivatableTest.class;
	//private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	//private static final String BASE_LOCATION = THIS_CLAZZ.getProtectionDomain().getCodeSource().getLocation().getFile();


	public ActivatableWrapper(ActivationID id, int port) throws RemoteException {
		super(id, port, new SecureSocketFactory(), new SecureSocketFactory());
	}

	public void setup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
}
