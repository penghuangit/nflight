package com.abreqadhabra.nflight.application.service.network.rmi.impl;

import java.io.IOException;
import java.rmi.MarshalledObject;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationID;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.Config;
import com.abreqadhabra.nflight.application.service.network.rmi.AbstractRMIServant;
import com.abreqadhabra.nflight.application.service.network.rmi.RMIServiceDescriptor;
import com.abreqadhabra.nflight.application.service.network.rmi.conf.RMIServantConfig;
import com.abreqadhabra.nflight.application.service.network.rmi.exception.RMIServantException;
import com.abreqadhabra.nflight.application.service.network.rmi.helper.ActivatableRMIServantHelper;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.exception.UnexpectedRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ActivatableRMIServantImpl extends AbstractRMIServant {
	private static Class<ActivatableRMIServantImpl> THIS_CLAZZ = ActivatableRMIServantImpl.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private ActivationID activationID;

	public ActivatableRMIServantImpl() {
	}

	public ActivatableRMIServantImpl(ActivationID id, MarshalledObject<?> data)
			throws RemoteException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		this.activationID = id;
		// Register the object with the activation system
		// then export it on an anonymous port
		Remote stub = Activatable.exportObject(this, id, 0);
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"stub for the activatable remote object: " + stub.toString());
	}

	public ActivatableRMIServantImpl(RMIServiceDescriptor serviceDescriptor)
			throws NFlightRemoteException {
		super(Config
				.getBoolean(RMIServantConfig.KEY_BOO_RMI_ACTIVATABLE_RUNNING),
				serviceDescriptor.getHostAddress(), serviceDescriptor.getPort(),
				Config.get(RMIServantConfig.KEY_STR_RMI_ACTIVATABLE_BOUND_NAME));
	}

	@Override
	protected void start() throws NFlightRemoteException {
		if (this.isRunning) {
			ActivatableRMIServantHelper.checkActivationSystem();
			bind();
		}
	}

	private void bind() throws NFlightRemoteException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {

			Remote stub = exportObjectRemoteObject();

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Activation descriptor registered : " + stub);
			this.registry.rebind(this.boundName, stub);
			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getName(),
					METHOD_NAME,
					stub + " Stub bound in registry."
							+ Arrays.toString(this.registry.list()));
			// display a waiting message while ... waiting clients
			LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(),
					METHOD_NAME, "Waiting for connections ..."
							+ this.boundName);
		} catch (IOException e) {
			throw new RMIServantException(e);
		} catch (Exception e) {
			throw new UnexpectedRemoteException(e);
		}

	}

	private Remote exportObjectRemoteObject() throws IOException {
		String className = THIS_CLAZZ.getName();
		String codebase = RMIServantConfig.STR_PREFIX_RMI_ACTIVATABLE_CODEBASE
				+ RMIServantConfig.PATH_APPLICATION_CODEBASE.toString();
		String policyfile = RMIServantConfig.PATH_RMI_ACTIVATABLE_POLICY
				.toString();
		MarshalledObject<Object> data = new MarshalledObject<Object>(
				RMIServantConfig.PATH_RMI_ACTIVATABLE_MARSHALLED_OBJECT
						.toString());
		return ActivatableRMIServantHelper.install(className, codebase,
				policyfile, data);
	}

	@Override
	protected void stop() throws NFlightException, NFlightRemoteException {
		try {
			this.registry.unbind(this.boundName);
			ActivatableRMIServantHelper.uninstall(this.activationID);
			ActivatableRMIServantHelper.stopActivationSystem();
			this.interrupt();
		} catch (RemoteException | NotBoundException e) {
			throw new RMIServantException(e);
		} catch (Exception e) {
			throw new UnexpectedRemoteException(e);
		}
	}

}
