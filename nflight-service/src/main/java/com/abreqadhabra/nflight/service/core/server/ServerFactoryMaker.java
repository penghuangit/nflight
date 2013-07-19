package com.abreqadhabra.nflight.service.core.server;

import com.abreqadhabra.nflight.service.core.Profile;
import com.abreqadhabra.nflight.service.rmi.server.activatable.ActivatableRMIServerConcreteFactory;
import com.abreqadhabra.nflight.service.rmi.server.unicast.UnicastRMIServerConcreteFactory;

// Factory creator - an indirect way of instantiating the factories
public class ServerFactoryMaker {
	private static ServerAbstractFactory abstractFactory = null;

	public static ServerAbstractFactory getFactory(String serviceName) {
		if (serviceName.equals(Profile.RMI_SERVICE.unicast.toString())) {
			abstractFactory = new UnicastRMIServerConcreteFactory();
		} else if (serviceName.equals(Profile.RMI_SERVICE.activatable
				.toString())) {
			abstractFactory = new ActivatableRMIServerConcreteFactory();
		}
		return abstractFactory;
	}
}