package com.abreqadhabra.nflight.application.trash_server.command;

import com.abreqadhabra.nflight.application.trash_server.IServer;
import com.abreqadhabra.nflight.application.common.launcher.command.Command;

//Command ConcreteCommand 
public class StartupServerCommand implements Command {
	private IServer server;

	public StartupServerCommand(IServer server) {
		this.server = server;
	}

	@Override
	public void execute() throws Exception {
		server.startup();
	}
}