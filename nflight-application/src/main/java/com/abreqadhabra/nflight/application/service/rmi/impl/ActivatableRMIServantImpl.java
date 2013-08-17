package com.abreqadhabra.nflight.application.service.rmi.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.MarshalledObject;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupDesc;
import java.rmi.activation.ActivationGroupID;
import java.rmi.activation.ActivationID;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.Configure;
import com.abreqadhabra.nflight.application.service.rmi.AbstractRMIServant;
import com.abreqadhabra.nflight.application.service.rmi.RMIServantException;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.exception.UnexpectedRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ActivatableRMIServantImpl extends AbstractRMIServant {
	private static Class<ActivatableRMIServantImpl> THIS_CLAZZ = ActivatableRMIServantImpl.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private ActivationID activationID;

	public ActivatableRMIServantImpl(InetAddress addr, int port,
			String serviceName) throws NFlightRemoteException {
		super(addr, port, serviceName);
	}

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
	
	@Override
	protected void start() throws NFlightRemoteException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			String className = THIS_CLAZZ.getName();
			String codebase = Configure.PREFIX_FILE_ACTIVATABLE
					+ Configure.CODE_BASE_PATH.toString();
			String policyfile = Configure.FILE_ACTIVATABLE_POLICY.toString();
			MarshalledObject<Object> data = new MarshalledObject<Object>(
					Configure.FILE_ACTIVATION.toString());
			Remote stub = this.install(className, codebase, policyfile, data);
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
			this.uninstall();
			this.interrupt();
		} catch (RemoteException | NotBoundException e) {
			throw new RMIServantException(e);
		} catch (Exception e) {
			throw new UnexpectedRemoteException(e);
		}
	}

	private Remote install(String className, String codebase,
			String policyFile, MarshalledObject<Object> data)
			throws NFlightRemoteException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			Properties props = new Properties();
			props.put("java.security.policy", policyFile);

			// Create a new activation group descriptor
			ActivationGroupDesc groupDesc = new ActivationGroupDesc(props, null);

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"groupDesc : " + groupDesc);

			// Once the ActivationGroupDesc has been created,
			// register it with the activation system to
			// obtain its ID
			ActivationGroupID groupID = ActivationGroup.getSystem()
					.registerGroup(groupDesc);

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Activation group descriptor registered.");

			// Now we need to explicitly create the group
			ActivationGroup.createGroup(groupID, groupDesc, 0);

			// Register the rmi object with rmid Auto restart
			ActivationDesc desc = new ActivationDesc(groupID, className,
					codebase, data, true);

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"groupID:" + groupID + "\nclassName : " + className
							+ "\ncodebase : " + codebase + "\ndata : " + data);

			// return a stub.
			return Activatable.register(desc);

		} catch (ActivationException | RemoteException e) {
			throw new RMIServantException(e);
		} catch (Exception e) {
			throw new UnexpectedRemoteException(e);
		}
	}

	// The ActivatableShutdown interface implementation
	private boolean uninstall() throws NFlightRemoteException {
		return this.deactivate(true);
	}

	private boolean deactivate(boolean forever) throws NFlightRemoteException {
		try {
			if (forever) {
				Activatable.unregister(this.activationID);
				return true;
			} else {
				return Activatable.inactive(this.activationID);
			}
		} catch (RemoteException | ActivationException e) {
			throw new RMIServantException(e);
		} catch (Exception e) {
			throw new UnexpectedRemoteException(e);
		}
	}
}
