package com.abreqadhabra.nflight.application.server.command;

import com.abreqadhabra.nflight.application.server.IServer;
import com.abreqadhabra.nflight.application.launcher.command.Command;

public class StatusServerCommand implements Command {
	private IServer server;

	public StatusServerCommand(IServer server) {
		this.server = server;
	}

	@Override
	public void execute() throws Exception {
		this.server.status();
	}
}