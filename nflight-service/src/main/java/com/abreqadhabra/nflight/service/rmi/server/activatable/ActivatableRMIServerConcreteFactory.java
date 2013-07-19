package com.abreqadhabra.nflight.service.rmi.server.activatable;

import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.core.server.IService;
import com.abreqadhabra.nflight.service.core.server.ServerAbstractFactory;
import com.abreqadhabra.nflight.service.rmi.server.AbstractRMIServer;
import com.abreqadhabra.nflight.service.rmi.server.AbstractRMIService;

public class ActivatableRMIServerConcreteFactory extends ServerAbstractFactory {
	@Override
	public AbstractRMIServer createServer(ProfileImpl profile, IService service) throws Exception {
		return new ActivatableRMIServerImpl(profile, service);
	}

	@Override
	public AbstractRMIService createService() {
		return new ActivatableRMIServiceImpl();
	}
}
