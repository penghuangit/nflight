package com.abreqadhabra.nflight.application.service.rmi.impl;

import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.service.rmi.AbstractRMIServant;
import com.abreqadhabra.nflight.application.service.rmi.RMIServantException;
import com.abreqadhabra.nflight.application.service.rmi.RMIServantHelper;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.exception.UnexpectedRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class UnicastRMIServantImpl extends AbstractRMIServant
{
	private static Class<UnicastRMIServantImpl> THIS_CLAZZ = UnicastRMIServantImpl.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public UnicastRMIServantImpl(InetAddress addr, int port, String serviceName)
			throws NFlightRemoteException {
		super(addr, port, serviceName);
	}

	@Override
	protected void start() throws NFlightRemoteException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			if (RMIServantHelper.isActivatedRegistry(this.registry,
					this.boundName)) {
				throw new RMIServantException(this.boundName
						+ "가 레지스트리에 이미 등록되어 있습니다.");
			} else {
				Remote stub = UnicastRemoteObject.exportObject(this, 0);
				RMIServantHelper.rebind(this.registry, this.boundName, stub);
				LOGGER.logp(
						Level.FINER,
						THIS_CLAZZ.getName(),
						METHOD_NAME,
						stub + " Stub bound in registry."
								+ Arrays.toString(this.registry.list()));
			}
		} catch (RemoteException e) {
			throw new RMIServantException(e);
		} catch (Exception e) {
			throw new UnexpectedRemoteException(e);
		}
	}

	@Override
	protected void stop() throws NFlightException, NFlightRemoteException {
		try {
			this.registry.unbind(this.boundName);
			this.interrupt();
		} catch (RemoteException | NotBoundException e) {
			throw new RMIServantException(e);
		} catch (Exception e) {
			throw new UnexpectedRemoteException(e);
		}
	}

}