package com.abreqadhabra.nflight.application.service.network;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.abreqadhabra.nflight.application.service.conf.ServiceConfig.ENUM_SERVICE_TYPE;
import com.abreqadhabra.nflight.application.service.network.RMINetworkServiceFactoryImpl;
import com.abreqadhabra.nflight.application.service.network.SocketNetworkServiceFactoryImpl;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;

public abstract class NetworkServiceFactory {

	public static NetworkServiceFactory getNetworkServiceServiceFactory(ENUM_SERVICE_TYPE serviceType) {
		switch (serviceType) {
			case network_blocking :
			case network_nonblocking :
			case network_async :
			case network_unicast :
			case network_multicast :
				return new SocketNetworkServiceFactoryImpl(serviceType);
			case rmi_unicast :
			case rmi_activation :
				return new RMINetworkServiceFactoryImpl(serviceType);
			default :
				break;
		}
		return null;
	}

	public abstract Runnable createService() throws NFlightException,
			NFlightRemoteException;

	protected InetSocketAddress getEndpoint(int port) {
		return getEndpoint(null, port);
	}

	protected InetSocketAddress getEndpoint(InetAddress addr, int port) {
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
