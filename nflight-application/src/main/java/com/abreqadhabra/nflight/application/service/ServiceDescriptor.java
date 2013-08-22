package com.abreqadhabra.nflight.application.service;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.abreqadhabra.nflight.application.common.launcher.Config;
import com.abreqadhabra.nflight.application.service.conf.ServiceConfig.ENUM_SERVICE_TYPE;
import com.abreqadhabra.nflight.application.service.network.rmi.conf.RMIServantConfig;
import com.abreqadhabra.nflight.application.service.network.socket.conf.SocketServiceConfig;
import com.abreqadhabra.nflight.application.service.thread.ServiceThreadFactory;

public class ServiceDescriptor {

	private ENUM_SERVICE_TYPE serviceType;
	private ExecutorService executor;
	private ExecutorCompletionService<Object> completionService;
	protected InetSocketAddress endpoint;

	public ServiceDescriptor(ENUM_SERVICE_TYPE serviceType) {
		this.serviceType = serviceType;
		this.executor = this.getSingleThreadExecutor(serviceType);
		this.completionService = new ExecutorCompletionService<Object>(
				this.executor);
		// 서비스에서 사용할 주소 객체를 생성 (IP 주소는 로컬 주소를 자동 할당)
		this.endpoint = this.getEndpoint(serviceType);
	}

	private ExecutorService getSingleThreadExecutor(
			ENUM_SERVICE_TYPE serviceType2) {
		ThreadFactory threadFactory = new ServiceThreadFactory(
				this.serviceType.name());
		return Executors.newSingleThreadExecutor(threadFactory);
	}

	public ExecutorService getExecutor() {
		return this.executor;
	}

	public ExecutorCompletionService<Object> getCompletionService() {
		return this.completionService;
	}

	public InetSocketAddress getEndpoint() {
		return this.endpoint;
	}

	private InetSocketAddress getEndpoint(ENUM_SERVICE_TYPE serviceType) {
		String key = null;
		int port = 0;
		switch (serviceType) {
			case network_blocking :
				key = SocketServiceConfig.KEY_INT_SOCKET_BLOCKING_DEFAULT_PORT;
				break;
			case network_nonblocking :
				key = SocketServiceConfig.KEY_INT_SOCKET_NONBLOCKING_DEFAULT_PORT;
				break;
			case network_async :
				key = SocketServiceConfig.KEY_INT_SOCKET_ASYNC_DEFAULT_PORT;
				break;
			case network_unicast :
				key = SocketServiceConfig.KEY_INT_SOCKET_UNICAST_DEFAULT_PORT;
				break;
			case network_multicast :
				key = SocketServiceConfig.KEY_INT_SOCKET_MULTICAST_DEFAULT_PORT;
				break;
			case rmi_unicast :
			case rmi_activation :
				key = RMIServantConfig.KEY_INT_RMI_DEFAULT_PORT;
				break;
			default :
				break;
		}
		if (key != null) {
			port = Config.getInt(key);
		}

		return this.getEndpoint(port);
	}

	private InetSocketAddress getEndpoint(int port) {
		return this.getEndpoint(null, port);
	}

	private InetSocketAddress getEndpoint(InetAddress addr, int port) {
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
