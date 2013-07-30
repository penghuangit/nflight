package com.abreqadhabra.nflight.application.server.service.socket.test.sample.server;

import java.nio.channels.SocketChannel;

class ServerDataEvent {
	public SocketServer server;
	public SocketChannel socket;
	public byte[] data;

	public ServerDataEvent(SocketServer server, SocketChannel socket, byte[] data) {
		this.server = server;
		this.socket = socket;
		this.data = data;
	}
}