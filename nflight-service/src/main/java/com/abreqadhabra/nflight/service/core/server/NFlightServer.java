package com.abreqadhabra.nflight.service.core.server;

import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.Profile;
import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.core.command.Command;
import com.abreqadhabra.nflight.service.core.command.Invoker;
import com.abreqadhabra.nflight.service.core.server.command.ShutdownServerConcreteCommand;
import com.abreqadhabra.nflight.service.core.server.command.StartupServerConcreteCommand;
import com.abreqadhabra.nflight.service.core.server.command.StatusServerConcreteCommand;

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

		ServerAbstractFactory abstractFactory = null;
		IService service = null;
		AbstractServerReceiver server = null;
		Command cmd = null;
		Invoker invoker = new Invoker();
	
		
		abstractFactory = ServerFactoryMaker.getFactory(Profile.RMI_SERVICE.unicast.toString());
		service = abstractFactory.createService();
		server = abstractFactory.createServer(profile, service);
		
		cmd = new StartupServerConcreteCommand(server);
		invoker.add(cmd);

		cmd = new StatusServerConcreteCommand(server);
		invoker.add(cmd);

		cmd = new ShutdownServerConcreteCommand(server);
		invoker.add(cmd);

		invoker.executeAll();
		invoker.clear();
		
		abstractFactory = ServerFactoryMaker.getFactory(Profile.RMI_SERVICE.activatable.toString());
		service = abstractFactory.createService();
		server = abstractFactory.createServer(profile, service);

		cmd = new StartupServerConcreteCommand(server);
		invoker.execute(cmd);
		
		cmd = new StatusServerConcreteCommand(server);
		invoker.execute(cmd);
		
		cmd = new ShutdownServerConcreteCommand(server);
		invoker.execute(cmd);

	}


}
