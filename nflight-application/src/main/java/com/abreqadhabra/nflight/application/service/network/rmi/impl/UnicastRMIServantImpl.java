package com.abreqadhabra.nflight.application.service.network.rmi.impl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.Config;
import com.abreqadhabra.nflight.application.service.network.rmi.AbstractRMIServant;
import com.abreqadhabra.nflight.application.service.network.rmi.RMIServiceDescriptor;
import com.abreqadhabra.nflight.application.service.network.rmi.conf.RMIServantConfig;
import com.abreqadhabra.nflight.application.service.network.rmi.exception.RMIServantException;
import com.abreqadhabra.nflight.application.service.network.rmi.helper.UnicastRMIServantHelper;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.exception.UnexpectedRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class UnicastRMIServantImpl extends AbstractRMIServant {
	private static Class<UnicastRMIServantImpl> THIS_CLAZZ = UnicastRMIServantImpl.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public UnicastRMIServantImpl(RMIServiceDescriptor serviceDescriptor)
			throws NFlightRemoteException {
		super(Config.getBoolean(RMIServantConfig.KEY_STR_RMI_UNICAST_RUNNING),
				serviceDescriptor.getHostAddress(), serviceDescriptor.getPort(), Config
						.get(RMIServantConfig.KEY_STR_RMI_UNICAST_BOUND_NAME));
	}

	@Override
	protected void start() throws NFlightRemoteException {
		bind();
	}

	private void bind() throws NFlightRemoteException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			if (UnicastRMIServantHelper.isActivatedRegistry(this.registry,
					this.boundName)) {
				throw new RMIServantException(this.boundName
						+ "가 레지스트리에 이미 등록되어 있습니다.");
			} else {
				Remote stub = exportObjectRemoteObject();
				UnicastRMIServantHelper.rebind(this.registry, this.boundName,
						stub);
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
			}
		} catch (RemoteException e) {
			throw new RMIServantException(e);
		} catch (Exception e) {
			throw new UnexpectedRemoteException(e);
		}
	}

	private Remote exportObjectRemoteObject() throws RemoteException {
		return UnicastRemoteObject.exportObject(this, 0);
	}

	@Override
	protected void stop() throws NFlightRemoteException {
		UnicastRMIServantHelper.unbind(this.registry, this.boundName);
		this.interrupt();
	}
}