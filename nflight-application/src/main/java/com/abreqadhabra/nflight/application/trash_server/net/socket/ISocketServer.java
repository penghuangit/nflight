package com.abreqadhabra.nflight.application.trash_server.net.socket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.abreqadhabra.nflight.application.trash_server.IServer;

public interface ISocketServer extends IServer {

	void init();
	boolean open() throws IOException;

	void bind();

	void accept();

	// void setSessionMap(ConcurrentHashMap<Long, ISocketAcceptor> sessionMap);

	ConcurrentHashMap<Long, ISocketAcceptor> getSessionMap();

}
