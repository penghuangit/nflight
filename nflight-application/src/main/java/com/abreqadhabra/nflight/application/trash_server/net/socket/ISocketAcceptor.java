package com.abreqadhabra.nflight.application.trash_server.net.socket;

public interface ISocketAcceptor {

	public void init();

	public void start();

	public void receive();

	public void send();

	public void close();

	public void setAttribute(String key, Object value);

	public Object getAttribute(String key);

	public void addOutputQueue(MessageDTO messageDTO);

	public Long getSessionId();

}
