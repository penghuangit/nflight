package com.abreqadhabra.nflight.service.rmi.server.unicast;

import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.rmi.server.AbstractRMIServer;
import com.abreqadhabra.nflight.service.rmi.server.AbstractRMIService;
import com.abreqadhabra.nflight.service.rmi.server.RMIServerAbstractFactory;

public class UnicastRMIServerConcreteFactory extends RMIServerAbstractFactory {

	public AbstractRMIServer createServer(ProfileImpl profile) throws Exception {
		return new UnicastRMIServerImpl(profile);
	}

	public AbstractRMIService createService() {
		return new UnicastRMIServiceImpl();
	}
}
