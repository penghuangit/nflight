package com.abreqadhabra.nflight.service.core.server.command;

import com.abreqadhabra.nflight.service.core.command.Command;
import com.abreqadhabra.nflight.service.core.server.AbstractServerReceiver;

public class StartupServerConcreteCommand implements Command {
	private AbstractServerReceiver server;

	public StartupServerConcreteCommand(AbstractServerReceiver server) {
		this.server = server;
	}

	public void execute() throws Exception {
		this.server.startup();
	}
}