package com.abreqadhabra.nflight.service.rmi.server.activatable;

import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.rmi.server.AbstractRMIServer;
import com.abreqadhabra.nflight.service.rmi.server.AbstractRMIService;
import com.abreqadhabra.nflight.service.rmi.server.RMIServerAbstractFactory;

public class ActivatableRMIServerConcreteFactory extends RMIServerAbstractFactory {
	public AbstractRMIServer createServer(ProfileImpl profile) throws Exception {
		return new ActivatableRMIServerImpl(profile);
	}

	public AbstractRMIService createService(ProfileImpl profile) {
		return new ActivatableRMIServiceImpl(profile);
	}
}
