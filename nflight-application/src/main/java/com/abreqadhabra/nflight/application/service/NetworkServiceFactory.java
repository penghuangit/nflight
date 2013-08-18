package com.abreqadhabra.nflight.application.service;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.abreqadhabra.nflight.application.common.launcher.Configure;
import com.abreqadhabra.nflight.application.common.launcher.Configure.SERVICE_TYPE;
import com.abreqadhabra.nflight.application.service.network.RMIServantFactoryImpl;
import com.abreqadhabra.nflight.application.service.network.SocketServiceFactoryImpl;

public abstract class NetworkServiceFactory implements ServiceFactory {

	public static ServiceFactory getServiceFactory(SERVICE_TYPE serviceType,
			Configure configure) {
		switch (serviceType) {
			case network_blocking :
				return new SocketServiceFactoryImpl(serviceType, configure,
						getEndpoint(configure
								.getInt(Configure.BLOCKING_DEFAULT_PORT)));
			case network_nonblocking :
				return new SocketServiceFactoryImpl(serviceType, configure,
						getEndpoint(configure
								.getInt(Configure.NONBLOCKING_DEFAULT_PORT)));
			case network_async :
				return new SocketServiceFactoryImpl(serviceType, configure,
						getEndpoint(configure
								.getInt(Configure.ASYNC_DEFAULT_PORT)));
			case network_unicast :
				return new SocketServiceFactoryImpl(serviceType, configure,
						getEndpoint(configure
								.getInt(Configure.UNICAST_DEFAULT_PORT)));
			case network_multicast :
				return new SocketServiceFactoryImpl(serviceType, configure,
						getEndpoint(configure
								.getInt(Configure.BLOCKING_DEFAULT_PORT)));
			case rmi_unicast :
				return new RMIServantFactoryImpl(serviceType, configure,
						getEndpoint(configure
								.getInt(Configure.RMI_DEFAULT_PORT)));
			case rmi_activation :
				return new RMIServantFactoryImpl(serviceType, configure,
						getEndpoint(configure
								.getInt(Configure.RMI_DEFAULT_PORT)));
			default :
				break;
		}
		return null;
	}

	private static InetSocketAddress getEndpoint(int port) {
		return getEndpoint(null, port);
	}

	private static InetSocketAddress getEndpoint(InetAddress addr, int port) {
		try {
			if (addr == null) {
				addr = InetAddress.getLocalHost();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return new InetSocketAddress(addr, port);
	}

}
