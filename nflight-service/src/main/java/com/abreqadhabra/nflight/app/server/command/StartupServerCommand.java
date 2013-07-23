package com.abreqadhabra.nflight.app.server.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.app.core.command.Command;
import com.abreqadhabra.nflight.app.server.IServer;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

//Command ConcreteCommand 
public class StartupServerCommand implements Command {
	private static final Class<StartupServerCommand> THIS_CLAZZ = StartupServerCommand.class;
	private static Logger LOGGER = LoggingHelper
			.getLogger(StartupServerCommand.THIS_CLAZZ);

	private final IServer server;

	public StartupServerCommand(final IServer server) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		this.server = server;

		StartupServerCommand.LOGGER.logp(Level.FINER,
				StartupServerCommand.THIS_CLAZZ.getName(), METHOD_NAME,
				"Command -> ConcreteCommand  : ");
	}

	@Override
	public void execute() throws Exception {
		this.server.startup();
	}
}