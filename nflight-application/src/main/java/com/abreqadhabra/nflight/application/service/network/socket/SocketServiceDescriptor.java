package com.abreqadhabra.nflight.application.service.network.socket;

import java.io.IOException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.NetworkChannel;
import java.nio.channels.ServerSocketChannel;

import com.abreqadhabra.nflight.application.service.ServiceDescriptor;
import com.abreqadhabra.nflight.application.service.conf.ServiceConfig.ENUM_SERVICE_TYPE;
import com.abreqadhabra.nflight.common.exception.NFlightException;

public class SocketServiceDescriptor extends ServiceDescriptor {
	private NetworkChannel channel;

	public SocketServiceDescriptor(ENUM_SERVICE_TYPE serviceType)
			throws NFlightException, IOException {
		super(serviceType);
		this.channel = getNetworkChannelFactory(serviceType);
	}

	public ServerSocketChannel getServerSocketChannel() {
		return (ServerSocketChannel) this.channel;
	}

	public AsynchronousServerSocketChannel getAsynchronousChannel() {
		return (AsynchronousServerSocketChannel) this.channel;
	}

	public DatagramChannel getDatagramChannel() {
		return (DatagramChannel) this.channel;
	}

	private NetworkChannel getNetworkChannelFactory(
			ENUM_SERVICE_TYPE serviceType) throws IOException, NFlightException {
		return NetworkChannelFactory.getNetworkChannelFactory(serviceType);
	}
}