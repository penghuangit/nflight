package com.abreqadhabra.nflight.application.service.net;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import com.abreqadhabra.nflight.common.exception.NFlightException;

public interface Acceptor {
	public void init(InetSocketAddress endpoint) throws NFlightException;
	public void start() throws NFlightException;
	public void stop() throws NFlightException;
	public void accept(SocketChannel socket) throws NFlightException;
	public void send(SocketChannel socket);
	public void send(SocketChannel socket, Object message)
			throws NFlightException;
	public void receive(SocketChannel socket) throws NFlightException;
	public ServerChannelFactory createServerChannelFactory();
	

}
