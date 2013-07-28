package com.abreqadhabra.nflight.application.server.service;

import java.nio.channels.DatagramChannel;


public interface IService  {
	
	public boolean isRunning() throws Exception;

	public String sayHello() throws Exception;

	public void shutdown() throws Exception;

	public void startup() throws Exception;

	public boolean status() throws Exception;

	public DatagramChannel getDatagramChannel();
	
}
