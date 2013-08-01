package com.abreqadhabra.nflight.application.server.command;

import com.abreqadhabra.nflight.application.launcher.command.Command;
import com.abreqadhabra.nflight.application.server.IServer;

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