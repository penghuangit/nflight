package com.abreqadhabra.nflight.server.command;

import com.abreqadhabra.nflight.server.IServer;
import com.abreqadhabra.nflight.service.core.command.Command;

public class ShutdownServerCommand implements Command {
	private IServer server;

	public ShutdownServerCommand(IServer server) {
		this.server = server;
	}

	public void execute() throws Exception {
		this.server.shutdown();
	}
}