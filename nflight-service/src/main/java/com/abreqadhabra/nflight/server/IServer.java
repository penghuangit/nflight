package com.abreqadhabra.nflight.server;

import com.abreqadhabra.nflight.server.service.IService;

public interface IServer {

	public void setService(IService _service);

	public IService getService();

	public void init();

	public void startup();

	public boolean status();

	public void shutdown();

}
