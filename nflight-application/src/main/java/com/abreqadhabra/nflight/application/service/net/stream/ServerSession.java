package com.abreqadhabra.nflight.application.service.net.stream;

public interface ServerSession {

	void send(ServerSession session);

	void receive(ServerSession session);

}
