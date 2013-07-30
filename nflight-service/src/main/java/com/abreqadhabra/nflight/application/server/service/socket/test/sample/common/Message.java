package com.abreqadhabra.nflight.application.server.service.socket.test.sample.common;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	String str;

	public Message(String str) {
		this.str = str;
	}

	@Override
	public String toString() {
		return "Message [str=" + str + "]";
	}

}
