package com.abreqadhabra.nflight.app.server;

import com.abreqadhabra.nflight.app.server.service.IService;

public interface IServer {

	public void setService(IService _service);

	public IService getService();

	public void init() throws Exception;

	public void startup() throws Exception;

	public boolean status() throws Exception;

	public void shutdown() throws Exception;

}
