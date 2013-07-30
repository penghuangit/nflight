package com.abreqadhabra.nflight.application.server.service.socket.test.sample.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public interface SocketServer {

	void send(SocketChannel socket, byte[] data);
	void accept(SelectionKey key) throws IOException;
	
	void init() throws IOException;
}
