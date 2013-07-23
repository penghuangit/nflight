package com.abreqadhabra.nflight.app.server.command;

import com.abreqadhabra.nflight.app.core.command.Command;
import com.abreqadhabra.nflight.app.server.IServer;

public class ShutdownServerCommand implements Command {
	private final IServer server;

	public ShutdownServerCommand(final IServer server) {
		this.server = server;
	}

	@Override
	public void execute() throws Exception {
		this.server.shutdown();
	}
}