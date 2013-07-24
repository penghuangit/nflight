package com.abreqadhabra.nflight.application.server;

public interface IServer {
	public void startup() throws Exception;

	public boolean status() throws Exception;

	public void shutdown() throws Exception;
}
