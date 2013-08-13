package com.abreqadhabra.nflight.app.server.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.app.core.command.Command;
import com.abreqadhabra.nflight.app.server.IServer;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

//Command ConcreteCommand 
public class StartupServerCommand implements Command {
	private static Class<StartupServerCommand> THIS_CLAZZ = StartupServerCommand.class;
	private static Logger LOGGER = LoggingHelper
			.getLogger(THIS_CLAZZ);

	private IServer server;

	public StartupServerCommand(IServer server) {
		this.server = server;
	}

	@Override
	public void execute() throws Exception {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		this.server.startup();
		LOGGER.logp(Level.FINER,
				THIS_CLAZZ.getName(), METHOD_NAME,
				"Command -> ConcreteCommand  : " + THIS_CLAZZ.getName());
	}
}