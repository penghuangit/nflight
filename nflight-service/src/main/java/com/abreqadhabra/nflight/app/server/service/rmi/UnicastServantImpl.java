package com.abreqadhabra.nflight.app.server.service.rmi;

import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.app.core.exception.NFRemoteException;
import com.abreqadhabra.nflight.app.core.rmi.RMIServiceHelper;
import com.abreqadhabra.nflight.app.server.service.ServiceDescriptor;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

//Strategy ConcreteStrategy
public class UnicastServantImpl extends AbstractServant {

	private static Class<UnicastServantImpl> THIS_CLAZZ = UnicastServantImpl.class;

	private static Logger LOGGER = LoggingHelper
			.getLogger(THIS_CLAZZ);

	public UnicastServantImpl(ServiceDescriptor _desc)
			throws Exception {
		super(_desc);
	}

	@Override
	public void startup() throws Exception {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER,
				THIS_CLAZZ.getName(), METHOD_NAME,
				"Strategy -> ConcreteStrategy -> BehaviourInterface() : "
						+ METHOD_NAME);

		if (RMIServiceHelper.isActivatedRegistry(super.registry,
				super.boundName)) {
			throw new NFRemoteException(this.boundName
					+ "가 레지스트리에 이미 등록되어 있습니다.");
		} else {
			try {
				Remote servant = UnicastRemoteObject
						.exportObject(this, 0);
				RMIServiceHelper.rebind(super.registry, RMIServiceHelper
						.getBoundName(super.host, super.port, "unicast"),
						servant);
			} catch (Exception e) { 
				throw e;
			}
		}

	}

}
