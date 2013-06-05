package com.abreqadhabra.nflight.app.dao.exception;

public class DAORuntimeException extends Exception {

	private Throwable cause = null;

	public DAORuntimeException(String message, Throwable cause) {
		super(message);
		this.cause = cause;
	}

}
