package com.abreqadhabra.nflight.application.server.transport.socket;

import java.net.InetSocketAddress;

import com.abreqadhabra.nflight.application.server.transport.Connector;

public interface DatagramConnector extends Connector {

	InetSocketAddress getRemoteSocketAddress();

	void setRemoteSocketAddress(InetSocketAddress remoteSocketAddress);
}
