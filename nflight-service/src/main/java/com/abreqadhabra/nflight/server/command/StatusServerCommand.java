package com.abreqadhabra.nflight.server.command;

import com.abreqadhabra.nflight.server.IServer;
import com.abreqadhabra.nflight.service.core.command.Command;

public class StatusServerCommand implements Command {
	private IServer server;

	public StatusServerCommand(IServer server) {
		this.server = server;
	}

	public void execute() throws Exception {
		this.server.status();
	}
}