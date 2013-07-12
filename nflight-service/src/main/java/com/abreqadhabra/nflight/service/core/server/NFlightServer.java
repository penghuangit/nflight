package com.abreqadhabra.nflight.service.core.server;

public interface NFlightServer{

	public void startup() throws Exception;

	public void shutdown() throws Exception;

	public boolean status() throws Exception;

}
