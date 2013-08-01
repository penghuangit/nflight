package com.abreqadhabra.nflight.application.server.aio;

public interface Session {

	public Long getSessionId();

	public void init(Configure config);

	public void open();

	public void read();

}
