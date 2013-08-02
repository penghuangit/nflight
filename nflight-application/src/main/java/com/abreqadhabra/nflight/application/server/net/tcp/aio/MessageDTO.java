package com.abreqadhabra.nflight.application.server.net.tcp.aio;

import java.io.Serializable;
import java.nio.ByteBuffer;

public interface MessageDTO extends Serializable {

	public MessageDTO transfer(ByteBuffer buffer);
	void setType(String type);
	String getType();

	ByteBuffer getContent();

	void setContent(ByteBuffer content);
}
