package com.abreqadhabra.nflight.application.service;

import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;

public interface ServiceFactory {

	public Runnable createService() throws NFlightException,
			NFlightRemoteException;
}
