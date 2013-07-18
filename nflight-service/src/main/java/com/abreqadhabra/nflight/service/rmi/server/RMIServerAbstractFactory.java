package com.abreqadhabra.nflight.service.rmi.server;

import com.abreqadhabra.nflight.service.core.ProfileImpl;

public abstract class RMIServerAbstractFactory {
	public abstract AbstractRMIServer createServer(ProfileImpl profile)
			throws Exception;

	public abstract AbstractRMIService createService();
}