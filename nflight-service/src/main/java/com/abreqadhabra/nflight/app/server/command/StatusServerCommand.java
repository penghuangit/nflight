package com.abreqadhabra.nflight.app.server.command;

import com.abreqadhabra.nflight.app.core.command.Command;
import com.abreqadhabra.nflight.app.server.IServer;

public class StatusServerCommand implements Command {
	private final IServer server;

	public StatusServerCommand(final IServer server) {
		this.server = server;
	}

	@Override
	public void execute() throws Exception {
		this.server.status();
	}
}