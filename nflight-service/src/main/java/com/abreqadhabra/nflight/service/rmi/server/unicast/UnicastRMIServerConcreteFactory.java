package com.abreqadhabra.nflight.service.rmi.server.unicast;

import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.core.server.IService;
import com.abreqadhabra.nflight.service.rmi.server.AbstractRMIServer;
import com.abreqadhabra.nflight.service.rmi.server.AbstractRMIService;
import com.abreqadhabra.nflight.service.rmi.server.RMIServerAbstractFactory;

public class UnicastRMIServerConcreteFactory extends RMIServerAbstractFactory {

	@Override
	public AbstractRMIServer createServer(ProfileImpl profile, IService service) throws Exception {
		return new UnicastRMIServerImpl(profile, service);
	}

	@Override
	public AbstractRMIService createService() {
		return new UnicastRMIServiceImpl();
	}
}
