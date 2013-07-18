package com.abreqadhabra.nflight.service.rmi.server;

import java.util.Arrays;

import com.abreqadhabra.nflight.service.rmi.server.activatable.ActivatableRMIServerConcreteFactory;
import com.abreqadhabra.nflight.service.rmi.server.unicast.UnicastRMIServerConcreteFactory;

// Factory creator - an indirect way of instantiating the factories
public class RMIServerFactoryMaker {
	private static RMIServerAbstractFactory abstractFactory = null;
	public static final String[] SERVICE_NAMES = { "unicast", "activatable" };

	static RMIServerAbstractFactory getFactory(String serviceName) {
		if (Arrays.asList(SERVICE_NAMES).contains(serviceName)) {
			abstractFactory = new UnicastRMIServerConcreteFactory();
		} else if (Arrays.asList(SERVICE_NAMES).contains(serviceName)) {
			abstractFactory = new ActivatableRMIServerConcreteFactory();
		}
		return abstractFactory;
	}
}