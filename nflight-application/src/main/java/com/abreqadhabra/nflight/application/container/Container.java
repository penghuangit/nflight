package com.abreqadhabra.nflight.application.container;

import com.abreqadhabra.nflight.common.exception.NFlightException;

public interface Container {

	public void startup() throws NFlightException;
	public void shutdown();

}
