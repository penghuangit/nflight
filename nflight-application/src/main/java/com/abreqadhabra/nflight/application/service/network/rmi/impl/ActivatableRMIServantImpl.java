package com.abreqadhabra.nflight.application.service.network.rmi.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
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
import java.rmi.activation.ActivationSystem;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.Configure;
import com.abreqadhabra.nflight.application.service.network.rmi.AbstractRMIServant;
import com.abreqadhabra.nflight.application.service.network.rmi.RMIServantException;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.exception.UnexpectedRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ActivatableRMIServantImpl extends AbstractRMIServant {
	private static Class<ActivatableRMIServantImpl> THIS_CLAZZ = ActivatableRMIServantImpl.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
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

	public ActivatableRMIServantImpl(Configure configure,
			InetSocketAddress endpoint) throws NFlightRemoteException {
		super(configure.getBoolean(Configure.UNICAST_RMI_RUNNING), configure,
				endpoint.getAddress(), endpoint.getPort(), configure
						.get(Configure.ACTIVATABLE_RMI_BOUND_NAME));
	}

	@Override
	protected void start() throws NFlightRemoteException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			if (isRunning) {
				this.executeRMID(configure);
			}
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

	private void executeRMID(Configure configure) throws NFlightRemoteException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			// 액티베이션 시스템이 기동되어 있을 경우 RMID를 기동을 스킵
			ActivationSystem activationSystem = ActivationGroup.getSystem();
			LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getName(), METHOD_NAME,
					"액티베이션 시스템이 기동되어 있을 경우 RMID를 기동을 스킵 " + activationSystem);
		} catch (ActivationException ae) {
			try {
				// Port already in use: 1098 Address already in use: JVM_Bind
				// 액티베이션 시스템이 기동되어 있지 않을 경우 RMID 시작
				new Thread(new RMIDRunnableImpl(configure)).start();
				// 콜러블로 기동결과에 대한 값이 있을때까지 대기
				Thread.sleep(configure
						.getInt(Configure.ACTIVATABLE_RMI_RMID_DELAY_SECONDS));
			} catch (InterruptedException e) {
				throw new RMIServantException(e);
			}
			LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getName(), METHOD_NAME,
					"액티베이션 시스템이 기동되어 있지 않을 경우 RMID 시작 ");
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
