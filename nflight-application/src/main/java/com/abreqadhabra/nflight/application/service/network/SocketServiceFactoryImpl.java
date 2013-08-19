package com.abreqadhabra.nflight.application.service.network;

import java.net.InetSocketAddress;

import com.abreqadhabra.nflight.application.service.ServiceFactory;
import com.abreqadhabra.nflight.application.service.conf.ServiceConfiguration.ENUM_SERVICE_TYPE;
import com.abreqadhabra.nflight.application.service.network.socket.exception.SocketServiceException;
import com.abreqadhabra.nflight.application.service.network.socket.impl.AsynchronousSocketServiceImpl;
import com.abreqadhabra.nflight.application.service.network.socket.impl.BlockingSocketServiceImpl;
import com.abreqadhabra.nflight.application.service.network.socket.impl.MulticastDatagramServiceImpl;
import com.abreqadhabra.nflight.application.service.network.socket.impl.NonblockingSocketServiceImpl;
import com.abreqadhabra.nflight.application.service.network.socket.impl.UnicastSocketServiceImpl;
import com.abreqadhabra.nflight.common.exception.NFlightException;

public class SocketServiceFactoryImpl extends ServiceFactory {

	private ENUM_SERVICE_TYPE serviceType;
	private InetSocketAddress endpoint;

	public SocketServiceFactoryImpl(ENUM_SERVICE_TYPE serviceType,
			InetSocketAddress endpoint) {
		this.serviceType = serviceType;
		this.endpoint = endpoint;
	}
	@Override
	public Runnable createService() throws NFlightException {
		if (this.serviceType.equals(ENUM_SERVICE_TYPE.network_blocking)) {
			return getBlockingSocketServiceImpl();
		} else if (this.serviceType.equals(ENUM_SERVICE_TYPE.network_nonblocking)) {
			return getNonblockingSocketServiceImpl();
		} else if (this.serviceType.equals(ENUM_SERVICE_TYPE.network_async)) {
			return getAsynchronousSocketServiceImpl();
		} else if (this.serviceType.equals(ENUM_SERVICE_TYPE.network_unicast)) {
			return getUnicastSocketServiceImpl();
		} else if (this.serviceType.equals(ENUM_SERVICE_TYPE.network_multicast)) {
			return getMulticastDatagramServiceImpl();
		} else {
			throw new SocketServiceException("서비스 객체 생성에 실패하였습니다.");
		}
	}

	private Runnable getMulticastDatagramServiceImpl() throws NFlightException {
		return new MulticastDatagramServiceImpl(endpoint);
	}

	private Runnable getUnicastSocketServiceImpl() throws NFlightException {
		return new UnicastSocketServiceImpl(endpoint);
	}

	private Runnable getAsynchronousSocketServiceImpl() throws NFlightException {
		return new AsynchronousSocketServiceImpl(endpoint);
	}

	private Runnable getNonblockingSocketServiceImpl() throws NFlightException {
		return new NonblockingSocketServiceImpl(endpoint);
	}

	private Runnable getBlockingSocketServiceImpl() throws NFlightException {
		return new BlockingSocketServiceImpl(endpoint);
	}

}
