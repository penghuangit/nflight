package com.abreqadhabra.nflight.application.server.command;

import com.abreqadhabra.nflight.application.launcher.command.Command;
import com.abreqadhabra.nflight.application.server.IServer;

//Command ConcreteCommand 
public class StartupCommand implements Command {
	private IServer server;

	public StartupCommand(IServer server) {
		this.server = server;
	}

	@Override
	public void execute() throws Exception {
		server.startup();
	}
}