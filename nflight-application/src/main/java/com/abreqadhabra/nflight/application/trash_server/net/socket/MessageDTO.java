package com.abreqadhabra.nflight.application.trash_server.net.socket;

import java.io.Serializable;
import java.nio.ByteBuffer;

import com.abreqadhabra.nflight.dao.dto.Airline;

public interface MessageDTO extends Serializable {

	// public MessageDTO transfer(ByteBuffer buffer);

	void setType(String type);
	
	String getType();

	ByteBuffer getContent();

	void setContent(ByteBuffer content);

	void setAirlines(Airline[] airlines);
	
	Airline[] getAirlines();

}
