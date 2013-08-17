package com.abreqadhabra.nflight.application.trash_server;

public interface IServer {
	
	public void startup() throws Exception;

	public boolean status() throws Exception;

	public void shutdown() throws Exception;
}
