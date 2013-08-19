package com.abreqadhabra.nflight.application.service.network.rmi.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
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
import com.abreqadhabra.nflight.application.service.network.rmi.conf.RMIServantConfiguration;
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

	public ActivatableRMIServantImpl(InetSocketAddress endpoint)
			throws NFlightRemoteException {
		super(Config.getBoolean(RMIServantConfiguration.STR_UNICAST_RMI_RUNNING), endpoint
				.getAddress(), endpoint.getPort(), Config
				.get(RMIServantConfiguration.STR_ACTIVATABLE_RMI_BOUND_NAME));
	}

	@Override
	protected void start() throws NFlightRemoteException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			if (this.isRunning) {
				ActivatableRMIServantHelper
						.checkActivationSystem();
			}
			String className = THIS_CLAZZ.getName();
			String codebase = RMIServantConfiguration.PREFIX_ACTIVATABLE_CODEBASE
					+ RMIServantConfiguration.PATH_APPLICATION_CODEBASE.toString();
			String policyfile = RMIServantConfiguration.FILE_ACTIVATABLE_POLICY.toString();
			MarshalledObject<Object> data = new MarshalledObject<Object>(
					RMIServantConfiguration.FILE_ACTIVATABLE_MARSHALLED_OBJECT.toString());
			Remote stub = ActivatableRMIServantHelper.install(className,
					codebase, policyfile, data);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Activation descriptor registered : " + stub);
			this.registry.rebind(this.boundName, stub);
			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getName(),
					METHOD_NAME,
					stub + " Stub bound in registry."
							+ Arrays.toString(this.registry.list()));
		} catch (IOException e) {
			throw new RMIServantException(e);
		} catch (Exception e) {
			throw new UnexpectedRemoteException(e);
		}
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
