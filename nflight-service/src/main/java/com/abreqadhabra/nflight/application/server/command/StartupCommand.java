package com.abreqadhabra.nflight.application.server.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.command.Command;
import com.abreqadhabra.nflight.application.server.IServer;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

//Command ConcreteCommand 
public class StartupCommand implements Command {
	private static final Class<StartupCommand> THIS_CLAZZ = StartupCommand.class;
	private static Logger LOGGER = LoggingHelper
			.getLogger(StartupCommand.THIS_CLAZZ);

	private final IServer server;

	public StartupCommand(final IServer server) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		this.server = server;

		LOGGER.logp(Level.FINER,
				THIS_CLAZZ.getName(), METHOD_NAME,
				"Command -> ConcreteCommand  : " + THIS_CLAZZ.getName());
	}

	@Override
	public void execute() throws Exception {
		this.server.startup();
	}
}