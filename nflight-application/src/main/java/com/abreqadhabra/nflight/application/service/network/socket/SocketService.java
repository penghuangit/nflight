package com.abreqadhabra.nflight.application.service.network.socket;

import java.net.InetSocketAddress;
import java.nio.channels.NetworkChannel;
import java.nio.channels.SocketChannel;

import com.abreqadhabra.nflight.common.exception.NFlightException;

public interface SocketService {
	public void init(InetSocketAddress endpoint) throws NFlightException;
	public void start() throws NFlightException;
	public void stop() throws NFlightException;
	public void accept(NetworkChannel socketChannel) throws NFlightException;
	public void send(SocketChannel socket);
	public void send(SocketChannel socket, Object message)
			throws NFlightException;
	public void receive(NetworkChannel socketChannel) throws NFlightException;
	public ServerSocketChannelFactory createServerChannelFactory();
}
