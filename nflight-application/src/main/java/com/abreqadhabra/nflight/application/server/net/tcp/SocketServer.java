package com.abreqadhabra.nflight.application.server.net.tcp;

import java.util.concurrent.ConcurrentHashMap;

public interface SocketServer {

	public void startup() throws Exception;

	public void shutdown() throws Exception;

	public Session getSession(long sessionId);

	//void setSessionMap(ConcurrentHashMap<Long, Session> sessionMap);

	ConcurrentHashMap<Long, Session> getSessionMap();

}
