package com.abreqadhabra.nflight.application.server.command;

import com.abreqadhabra.nflight.application.server.IServer;
import com.abreqadhabra.nflight.application.launcher.command.Command;

public class ShutdownServerCommand implements Command {
	private IServer server;

	public ShutdownServerCommand(IServer server) {
		this.server = server;
	}

	@Override
	public void execute() throws Exception {
		this.server.shutdown();
	}
}