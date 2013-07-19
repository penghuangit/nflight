package com.abreqadhabra.nflight.service.core.server;

import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.core.server.AbstractServerReceiver;
import com.abreqadhabra.nflight.service.core.server.IService;

public abstract class ServerAbstractFactory {
	public abstract AbstractServerReceiver createServer(ProfileImpl profile, IService service)
			throws Exception;

	public abstract IService createService();
}