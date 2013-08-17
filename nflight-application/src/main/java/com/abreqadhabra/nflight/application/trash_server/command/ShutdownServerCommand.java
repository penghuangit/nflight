package com.abreqadhabra.nflight.application.trash_server.command;

import com.abreqadhabra.nflight.application.trash_server.IServer;
import com.abreqadhabra.nflight.application.common.launcher.command.Command;

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