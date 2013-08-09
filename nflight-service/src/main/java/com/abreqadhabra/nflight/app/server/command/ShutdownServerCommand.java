package com.abreqadhabra.nflight.app.server.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.command.Command;
import com.abreqadhabra.nflight.application.server.IServer;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;


public class ShutdownServerCommand implements Command {
	private static final Class<ShutdownServerCommand> THIS_CLAZZ = ShutdownServerCommand.class;
	private static Logger LOGGER = LoggingHelper
			.getLogger(THIS_CLAZZ);
	
	
	private final IServer server;

	public ShutdownServerCommand(final IServer server) {
		this.server = server;
	}

	@Override
	public void execute() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		this.server.shutdown();
		LOGGER.logp(Level.FINER,
				THIS_CLAZZ.getName(), METHOD_NAME,
				"Command -> ConcreteCommand  : " + THIS_CLAZZ.getName());
	}
}