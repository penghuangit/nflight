package com.abreqadhabra.nflight.application.service.net.stream.nonblocking;

public interface ServerSession {

	void send(ServerSession session);

	void receive(ServerSession session);

}
