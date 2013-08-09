package com.abreqadhabra.nflight.app.server.service;

import java.rmi.Remote;

//Strategy IStrategy / Flyweight Interface
public interface IService extends Remote {

	public void init() throws Exception;

	public boolean isRunning() throws Exception;

	public String sayHello() throws Exception;

	public void shutdown() throws Exception;

	public void startup() throws Exception;

	public boolean status() throws Exception;
}
