package com.abreqadhabra.nflight.application.server.service.socket.tcp.common;

import java.io.Serializable;

public class Message implements Serializable {

	private static long serialVersionUID = 1L;

	String str;

	public Message(String str) {
		this.str = str;
	}

	@Override
	public String toString() {
		return str;
	}

}
