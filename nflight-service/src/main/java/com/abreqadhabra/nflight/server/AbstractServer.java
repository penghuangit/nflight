package com.abreqadhabra.nflight.server;

import com.abreqadhabra.nflight.service.core.Profile;

public abstract class AbstractServer {

	Profile profile;
	
	
	public abstract void init() throws Exception;

	public abstract void startup() throws Exception;

	public abstract boolean status() throws Exception;

	public abstract void shutdown() throws Exception;

	
}
