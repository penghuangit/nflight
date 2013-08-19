package com.abreqadhabra.nflight.application.service;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.abreqadhabra.nflight.application.common.launcher.Config;
import com.abreqadhabra.nflight.application.service.conf.ServiceConfiguration.ENUM_SERVICE_TYPE;
import com.abreqadhabra.nflight.application.service.network.RMIServantFactoryImpl;
import com.abreqadhabra.nflight.application.service.network.SocketServiceFactoryImpl;
import com.abreqadhabra.nflight.application.service.network.rmi.conf.RMIServantConfiguration;
import com.abreqadhabra.nflight.application.service.network.socket.conf.SocketServiceConfiguration;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;

public abstract class ServiceFactory {

	public static ServiceFactory getServiceFactory(ENUM_SERVICE_TYPE serviceType) {
		switch (serviceType) {
			case network_blocking :
				return new SocketServiceFactoryImpl(serviceType,
						getEndpoint(Config
								.getInt(SocketServiceConfiguration.KEY_INT_SOCKET_BLOCKING_DEFAULT_PORT)));
			case network_nonblocking :
				return new SocketServiceFactoryImpl(serviceType,
						getEndpoint(Config
								.getInt(SocketServiceConfiguration.KEY_INT_SOCKET_NONBLOCKING_DEFAULT_PORT)));
			case network_async :
				return new SocketServiceFactoryImpl(serviceType,
						getEndpoint(Config
								.getInt(SocketServiceConfiguration.KEY_INT_SOCKET_ASYNC_DEFAULT_PORT)));
			case network_unicast :
				return new SocketServiceFactoryImpl(serviceType,
						getEndpoint(Config
								.getInt(SocketServiceConfiguration.KEY_INT_SOCKET_UNICAST_DEFAULT_PORT)));
			case network_multicast :
				return new SocketServiceFactoryImpl(serviceType,
						getEndpoint(Config
								.getInt(SocketServiceConfiguration.KEY_INT_SOCKET_BLOCKING_DEFAULT_PORT)));
			case rmi_unicast :
				return new RMIServantFactoryImpl(serviceType,
						getEndpoint(Config.getInt(RMIServantConfiguration.KEY_INT_RMI_DEFAULT_PORT)));
			case rmi_activation :
				return new RMIServantFactoryImpl(serviceType,
						getEndpoint(Config
								.getInt(RMIServantConfiguration.KEY_INT_RMI_DEFAULT_PORT)));
			default :
				break;
		}
		return null;
	}

	public abstract Runnable createService() throws NFlightException,
			NFlightRemoteException;

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
