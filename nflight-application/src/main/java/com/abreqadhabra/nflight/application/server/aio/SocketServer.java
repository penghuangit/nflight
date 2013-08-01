package com.abreqadhabra.nflight.application.server.aio;

public interface SocketServer {

	public void startup() throws Exception;

	public void stop() throws Exception;

	public Session getSession(long sessionId);

}
