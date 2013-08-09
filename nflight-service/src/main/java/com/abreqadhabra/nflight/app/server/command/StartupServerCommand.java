package com.abreqadhabra.nflight.app.server.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.command.Command;
import com.abreqadhabra.nflight.application.server.IServer;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

//Command ConcreteCommand 
public class StartupServerCommand implements Command {
	private static final Class<StartupServerCommand> THIS_CLAZZ = StartupServerCommand.class;
	private static Logger LOGGER = LoggingHelper
			.getLogger(THIS_CLAZZ);

	private final IServer server;

	public StartupServerCommand(final IServer server) {
		this.server = server;
	}

	@Override
	public void execute() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		this.server.startup();
		LOGGER.logp(Level.FINER,
				THIS_CLAZZ.getName(), METHOD_NAME,
				"Command -> ConcreteCommand  : " + THIS_CLAZZ.getName());
	}
}