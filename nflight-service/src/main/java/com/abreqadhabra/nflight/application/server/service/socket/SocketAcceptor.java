package com.abreqadhabra.nflight.application.server.service.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.abreqadhabra.nflight.application.server.service.socket.tcp.impl.DataEvent;

public interface SocketAcceptor {

	void bind() throws IOException;

	void accept(SelectionKey key) throws IOException;

	void read(SelectionKey key) throws IOException;

	void write(SelectionKey key) throws IOException;
	
	void send(SocketChannel socket, ByteBuffer data);

	void execute(DataEvent dataEvent);


}
