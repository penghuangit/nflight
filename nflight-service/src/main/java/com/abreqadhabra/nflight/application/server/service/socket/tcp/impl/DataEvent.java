package com.abreqadhabra.nflight.application.server.service.socket.tcp.impl;

import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

import com.abreqadhabra.nflight.application.server.service.socket.SocketAcceptor;

public class DataEvent {
	public SocketAcceptor acceptor;
	public SocketChannel socket;
	public DatagramChannel datagram;

	public byte[] data;

	public DataEvent(SocketAcceptor acceptor, SocketChannel socket, byte[] data) {
		this.acceptor = acceptor;
		this.socket = socket;
		this.data = data;
	}

	public DataEvent(SocketAcceptor acceptor, DatagramChannel datagram,
			byte[] data) {
		this.acceptor = acceptor;
		this.datagram = datagram;
		this.data = data;
	}
}
