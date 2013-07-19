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
		AbstractServerReceiver server = null;
		Command cmd = null;
		
		abstractFactory = RMIServerFactoryMaker.getFactory(Profile.RMI_SERVICE.unicast.toString());
		service = abstractFactory.createService();
		server = abstractFactory.createServer(profile, service);
		
		cmd = new StartupServerConcreteCommand(server);
		executeCommand(cmd);

		cmd = new StatusServerConcreteCommand(server);
		executeCommand(cmd);

		cmd = new ShutdownServerConcreteCommand(server);
		executeCommand(cmd);
		
		abstractFactory = RMIServerFactoryMaker.getFactory(Profile.RMI_SERVICE.activatable.toString());
		service = abstractFactory.createService();
		server = abstractFactory.createServer(profile, service);

		cmd = new StartupServerConcreteCommand(server);
		executeCommand(cmd);
		
		cmd = new StatusServerConcreteCommand(server);
		executeCommand(cmd);
		
		cmd = new ShutdownServerConcreteCommand(server);
		executeCommand(cmd);

	}

	private void executeCommand(Command cmd)
			throws Exception {
		Invoker invoker = new Invoker();
		invoker.executeCommand(cmd); // shutdown server
	}

}
