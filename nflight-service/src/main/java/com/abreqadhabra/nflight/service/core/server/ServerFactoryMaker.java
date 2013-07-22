package com.abreqadhabra.nflight.service.core.server;

import com.abreqadhabra.nflight.service.core.Profile;
import com.abreqadhabra.nflight.service.exception.NFBootException;
import com.abreqadhabra.nflight.service.rmi.server.activatable.ActivatableRMIServerConcreteFactory;
import com.abreqadhabra.nflight.service.rmi.server.unicast.UnicastRMIServerConcreteFactory;
import com.abreqadhabra.nflight.service.socket.server.datagram.DatagramSocketServerConcreteFactory;
import com.abreqadhabra.nflight.service.socket.server.multicast.MulticastSocketServerConcreteFactory;
import com.abreqadhabra.nflight.service.socket.server.stream.StreamSocketServerConcreteFactory;

// Factory creator - an indirect way of instantiating the factories
public class ServerFactoryMaker {
	private static ServerAbstractFactory abstractFactory = null;

	public static ServerAbstractFactory getFactory(String serviceName) throws NFBootException {
		if (serviceName.equals(Profile.RMI_SERVICE.unicast.toString())) {
			abstractFactory = new UnicastRMIServerConcreteFactory();
		} else if (serviceName.equals(Profile.RMI_SERVICE.activatable
				.toString())) {
			abstractFactory = new ActivatableRMIServerConcreteFactory();
		}
		else if (serviceName.equals(Profile.SOCKET_SERVICE.stream
				.toString())) {
			abstractFactory = new StreamSocketServerConcreteFactory();
		}else if (serviceName.equals(Profile.SOCKET_SERVICE.datagram
				.toString())) {
			abstractFactory = new DatagramSocketServerConcreteFactory();
		}else if (serviceName.equals(Profile.SOCKET_SERVICE.multicast
				.toString())) {
			abstractFactory = new MulticastSocketServerConcreteFactory();
		}else{
			throw new NFBootException(
					"No value specified for Service Name");
		}
		return abstractFactory;
	}
}