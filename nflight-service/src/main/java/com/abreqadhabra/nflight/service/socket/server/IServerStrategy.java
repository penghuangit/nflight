package com.abreqadhabra.nflight.service.socket.server;

public interface IServerStrategy {
	
	public void initBehaviorInterface();
	public void startupBehaviorInterface();
	public boolean statusBehaviorInterface();
	public void shutdownBehaviorInterface();

}
