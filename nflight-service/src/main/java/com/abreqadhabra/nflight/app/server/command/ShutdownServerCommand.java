package com.abreqadhabra.nflight.app.server.command;

import com.abreqadhabra.nflight.app.core.command.Command;
import com.abreqadhabra.nflight.app.server.IServer;

public class ShutdownServerCommand implements Command {
	private IServer server;

	public ShutdownServerCommand(IServer server) {
		this.server = server;
	}

	public void execute() throws Exception {
		this.server.shutdown();
	}
}