package com.abreqadhabra.nflight.service.rmi.server;

import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.core.server.IService;

public abstract class RMIServerAbstractFactory {
	public abstract AbstractRMIServer createServer(ProfileImpl profile, IService service)
			throws Exception;

	public abstract AbstractRMIService createService();
}