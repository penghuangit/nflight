package com.abreqadhabra.nflight.service.socket.server.multicast;

import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.core.server.IService;
import com.abreqadhabra.nflight.service.core.server.ServerAbstractFactory;
import com.abreqadhabra.nflight.service.socket.server.AbstractSocketServer;
import com.abreqadhabra.nflight.service.socket.server.AbstractSocketService;




public class MulticastSocketServerConcreteFactory extends ServerAbstractFactory {

	@Override
	public AbstractSocketServer createServer(ProfileImpl profile, IService service) throws Exception {
		return new MulticastSocketServerImpl(profile, service);
	}

	@Override
	public AbstractSocketService createService() {
		return new MulticastSocketServiceImpl();
	}
}
