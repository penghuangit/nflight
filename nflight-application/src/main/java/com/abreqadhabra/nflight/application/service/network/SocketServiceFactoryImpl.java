package com.abreqadhabra.nflight.application.service.network;

import java.net.InetSocketAddress;

import com.abreqadhabra.nflight.application.common.launcher.Configure;
import com.abreqadhabra.nflight.application.common.launcher.Configure.SERVICE_TYPE;
import com.abreqadhabra.nflight.application.service.ServiceFactory;
import com.abreqadhabra.nflight.application.service.network.socket.SocketServiceException;
import com.abreqadhabra.nflight.application.service.network.socket.impl.AsynchronousSocketServiceImpl;
import com.abreqadhabra.nflight.application.service.network.socket.impl.BlockingSocketServiceImpl;
import com.abreqadhabra.nflight.application.service.network.socket.impl.MulticastDatagramServiceImpl;
import com.abreqadhabra.nflight.application.service.network.socket.impl.NonblockingSocketServiceImpl;
import com.abreqadhabra.nflight.application.service.network.socket.impl.UnicastSocketServiceImpl;
import com.abreqadhabra.nflight.common.exception.NFlightException;

public class SocketServiceFactoryImpl implements ServiceFactory {

	private SERVICE_TYPE serviceType;
	private Configure configure;
	private InetSocketAddress endpoint;

	public SocketServiceFactoryImpl(SERVICE_TYPE serviceType,
			Configure configure, InetSocketAddress endpoint) {
		this.serviceType = serviceType;
		this.configure = configure;
		this.endpoint = endpoint;
	}
	@Override
	public Runnable createService() throws NFlightException {
		if (this.serviceType.equals(SERVICE_TYPE.network_blocking)) {
			return getBlockingSocketServiceImpl();
		} else if (this.serviceType.equals(SERVICE_TYPE.network_nonblocking)) {
			return getNonblockingSocketServiceImpl();
		} else if (this.serviceType.equals(SERVICE_TYPE.network_async)) {
			return getAsynchronousSocketServiceImpl();
		} else if (this.serviceType.equals(SERVICE_TYPE.network_unicast)) {
			return getUnicastSocketServiceImpl();
		} else if (this.serviceType.equals(SERVICE_TYPE.network_multicast)) {
			return getMulticastDatagramServiceImpl();
		} else {
			throw new SocketServiceException("서비스 객체 생성에 실패하였습니다.");
		}
	}

	private Runnable getMulticastDatagramServiceImpl() throws NFlightException {
		return new MulticastDatagramServiceImpl(configure, endpoint);
	}

	private Runnable getUnicastSocketServiceImpl() throws NFlightException {
		return new UnicastSocketServiceImpl(configure, endpoint);
	}

	private Runnable getAsynchronousSocketServiceImpl() throws NFlightException {
		return new AsynchronousSocketServiceImpl(configure, endpoint);
	}

	private Runnable getNonblockingSocketServiceImpl() throws NFlightException {
		return new NonblockingSocketServiceImpl(configure, endpoint);
	}

	private Runnable getBlockingSocketServiceImpl() throws NFlightException {
		return new BlockingSocketServiceImpl(configure, endpoint);
	}

}
