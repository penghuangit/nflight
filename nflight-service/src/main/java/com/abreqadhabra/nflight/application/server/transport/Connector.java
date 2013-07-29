package com.abreqadhabra.nflight.application.server.transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

public interface Connector {

	DatagramChannel connect() throws IOException;

	DatagramChannel bind() throws IOException;

	public void close(DatagramChannel datagramChannel) throws Exception;

	

}