package com.abreqadhabra.nflight.application.service.temp.server;

import java.net.InetSocketAddress;

import com.abreqadhabra.nflight.application.transport.network.datagram.multicast.MulticastService;
import com.abreqadhabra.nflight.application.transport.network.datagram.unicast.UnicastBlockingService;
import com.abreqadhabra.nflight.application.transport.network.stream.asynchronous.AsyncService;
import com.abreqadhabra.nflight.application.transport.network.stream.blocking.BlockingService;
import com.abreqadhabra.nflight.application.transport.network.stream.nonblocking.NonBlockingService;
import com.abreqadhabra.nflight.application.transport.rmi.activation.ActivatableRMIServantImpl;
import com.abreqadhabra.nflight.application.transport.rmi.unicast.UnicastRMIServantImpl;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.launcher.Configure;

public class ServiceFactory {

	public Runnable createBlockingService(boolean isRunning,
			Configure configure, InetSocketAddress endpoint)
			throws NFlightException {
		return new BlockingService(isRunning, configure, endpoint);
	}

	public Runnable createNonBlockingService(boolean isRunning,
			Configure configure, InetSocketAddress endpoint)
			throws NFlightException {
		return new NonBlockingService(isRunning, endpoint, configure);
	}

	public Runnable createAsyncService(boolean isRunning, Configure configure,
			InetSocketAddress endpoint) throws NFlightException {
		return new AsyncService(isRunning, configure, endpoint);
	}

	public Runnable createUnicastService(boolean isRunning,
			Configure configure, InetSocketAddress endpoint)
			throws NFlightException {
		return new UnicastBlockingService(isRunning, configure, endpoint);
	}

	public Runnable createMulticastService(boolean isRunning,
			Configure configure, InetSocketAddress endpoint)
			throws NFlightException {
		return new MulticastService(isRunning, configure, endpoint);
	}

	public Runnable createRMIUnicastService(Configure configure,
			InetSocketAddress endpoint) throws NFlightException  {
		return new UnicastRMIServantImpl(endpoint.getAddress(),
				endpoint.getPort());
	}

	public Runnable createRMIMulticastService(Configure configure,
			InetSocketAddress endpoint) throws NFlightException {
		return new ActivatableRMIServantImpl(endpoint.getAddress(),
				endpoint.getPort());
	}

}
