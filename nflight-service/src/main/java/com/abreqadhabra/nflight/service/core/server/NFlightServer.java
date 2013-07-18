package com.abreqadhabra.nflight.service.core.server;

import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.rmi.server.RMIServerAbstractFactory;
import com.abreqadhabra.nflight.service.rmi.server.RMIServerFactoryMaker;

public class NFlightServer {
	private static final Class<NFlightServer> THIS_CLAZZ = NFlightServer.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	private ProfileImpl profile;

	public NFlightServer(ProfileImpl profile) throws Exception {
		this.profile = profile;
		this.executeRMIServer();
	}


	private void executeRMIServer() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		RMIServerAbstractFactory abstractFactory = null;
		IService service = null;
		AbstractServer server = null;

		abstractFactory = RMIServerFactoryMaker.getFactory("unicast");
		service = abstractFactory.createService();
		server = abstractFactory.createServer(profile, service);

		abstractFactory = RMIServerFactoryMaker.getFactory("activatable");
		service = abstractFactory.createService();
		server = abstractFactory.createServer(profile, service);

	}

}
