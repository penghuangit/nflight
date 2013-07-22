package com.abreqadhabra.nflight.service.rmi.server.unicast;

import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.core.rmi.RegistryManager;
import com.abreqadhabra.nflight.service.core.server.IService;
import com.abreqadhabra.nflight.service.exception.NFRemoteException;
import com.abreqadhabra.nflight.service.rmi.server.AbstractRMIServer;

public class UnicastRMIServerImpl extends AbstractRMIServer {

	private static final Class<UnicastRMIServerImpl> THIS_CLAZZ = UnicastRMIServerImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public UnicastRMIServerImpl(ProfileImpl profile, IService service)
			throws Exception {
		super(profile, service);
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Instantiating a " + THIS_CLAZZ.getSimpleName() + " Class ");		
	}

	@Override
	public void startup() throws Exception {
		if (RegistryManager
				.isActivatedRegistry(super.registry, super.boundName)) {
			throw new NFRemoteException(boundName + "가 레지스트리에 이미 등록되어 있습니다.");
		} else {
			try {
				Remote servant = UnicastRemoteObject.exportObject(
						super.service, 0);
				RegistryManager.rebind(super.registry, RegistryManager
						.getBoundName(super.host, super.port, "unicast"),
						servant);
			} catch (Exception e) {
				throw e;
			}
		}
	}
}