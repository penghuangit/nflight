package com.abreqadhabra.nflight.application.server.net.tcp;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.server.net.tcp.aio.MessageDTO;

public interface Session {

	public Long getSessionId();

	public void init(Configure config);

	public void open();

	public void read();

	public void setAttribute(String key, Object value);

	public Object getAttribute(String key);

	public void add(MessageDTO messageDTO);

	public void write();

}
