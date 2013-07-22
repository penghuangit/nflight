package com.abreqadhabra.nflight.server.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.server.IServer;
import com.abreqadhabra.nflight.service.core.command.Command;

//Command ConcreteCommand 
public class StartupServerCommand implements Command {
	private static final Class<StartupServerCommand> THIS_CLAZZ = StartupServerCommand.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	
	private IServer server;

	public StartupServerCommand(IServer server) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		this.server = server;
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Command -> ConcreteCommand  : " +  server.getOperation().getClass().getSimpleName());
	}

	public void execute() throws Exception {
		this.server.startup();
	}
}