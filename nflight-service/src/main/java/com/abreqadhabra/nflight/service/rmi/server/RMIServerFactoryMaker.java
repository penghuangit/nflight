package com.abreqadhabra.nflight.service.rmi.server;

import com.abreqadhabra.nflight.service.rmi.server.activatable.ActivatableRMIServerConcreteFactory;
import com.abreqadhabra.nflight.service.rmi.server.unicast.UnicastRMIServerConcreteFactory;

// Factory creator - an indirect way of instantiating the factories
public class RMIServerFactoryMaker {
	private static RMIServerAbstractFactory abstractFactory = null;
	public static final String[] SERVICE_NAMES = { "unicast", "activatable" };

	public static RMIServerAbstractFactory getFactory(String serviceName) {
		if (serviceName.equals("unicast")) {
			abstractFactory = new UnicastRMIServerConcreteFactory();
		} else if (serviceName.equals("activatable")) {
			abstractFactory = new ActivatableRMIServerConcreteFactory();
		}
		return abstractFactory;
	}
}