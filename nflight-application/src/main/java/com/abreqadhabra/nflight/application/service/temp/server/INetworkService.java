package com.abreqadhabra.nflight.application.service.temp.server;

import com.abreqadhabra.nflight.common.exception.NFlightException;

public interface INetworkService {

	public void startup() throws NFlightException;
	public void shutdown();

}
