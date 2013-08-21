package com.abreqadhabra.nflight.application.service;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.abreqadhabra.nflight.application.service.conf.ServiceConfig.ENUM_SERVICE_TYPE;
import com.abreqadhabra.nflight.application.service.network.rmi.RMIServiceDescriptor;
import com.abreqadhabra.nflight.application.service.network.rmi.impl.ActivatableRMIServantImpl;
import com.abreqadhabra.nflight.application.service.network.rmi.impl.UnicastRMIServantImpl;
import com.abreqadhabra.nflight.application.service.network.socket.SocketServiceDescriptor;
import com.abreqadhabra.nflight.application.service.network.socket.exception.SocketServiceException;
import com.abreqadhabra.nflight.application.service.network.socket.impl.datagram.MulticastDatagramServiceImpl;
import com.abreqadhabra.nflight.application.service.network.socket.impl.datagram.UnicastSocketServiceImpl;
import com.abreqadhabra.nflight.application.service.network.socket.impl.stream.AsynchronousSocketServiceImpl;
import com.abreqadhabra.nflight.application.service.network.socket.impl.stream.BlockingSocketServiceImpl;
import com.abreqadhabra.nflight.application.service.network.socket.impl.stream.NonblockingSocketServiceImpl;
import com.abreqadhabra.nflight.common.exception.NFlightException;

public class ServiceFactory {

	public static Callable<?> createtServiceTask(ENUM_SERVICE_TYPE serviceType)
			throws NFlightException, IOException {
		SocketServiceDescriptor ssd = new SocketServiceDescriptor(
				serviceType);
		RMIServiceDescriptor rsd = new RMIServiceDescriptor(
				serviceType);
		
		if (serviceType.equals(ENUM_SERVICE_TYPE.network_blocking)) {
			// 블로킹 소켓 서비스 객체 생성
			return new BlockingSocketServiceImpl(ssd);
		} else if (serviceType.equals(ENUM_SERVICE_TYPE.network_nonblocking)) {
			return new NonblockingSocketServiceImpl(ssd);
		} else if (serviceType.equals(ENUM_SERVICE_TYPE.network_async)) {
			return new AsynchronousSocketServiceImpl(ssd);
		} else if (serviceType.equals(ENUM_SERVICE_TYPE.network_unicast)) {
			return new UnicastSocketServiceImpl(ssd);
		} else if (serviceType.equals(ENUM_SERVICE_TYPE.network_multicast)) {
			return new MulticastDatagramServiceImpl(ssd);
		} else if (serviceType.equals(ENUM_SERVICE_TYPE.rmi_unicast)) {
			return new UnicastRMIServantImpl(rsd);
		} else if (serviceType.equals(ENUM_SERVICE_TYPE.rmi_activation)) {
			return new ActivatableRMIServantImpl(rsd);
		} else {
			throw new SocketServiceException("서비스 객체 생성에 실패하였습니다.");
		}
	}
}
