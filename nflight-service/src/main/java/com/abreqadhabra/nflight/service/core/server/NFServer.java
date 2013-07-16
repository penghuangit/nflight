package com.abreqadhabra.nflight.service.core.server;

public interface NFServer{

	public void startup() throws Exception;

	public void shutdown() throws Exception;

	public boolean status() throws Exception;

}
