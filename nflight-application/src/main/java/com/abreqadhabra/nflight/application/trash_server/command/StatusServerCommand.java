package com.abreqadhabra.nflight.application.trash_server.command;

import com.abreqadhabra.nflight.application.trash_server.IServer;
import com.abreqadhabra.nflight.application.common.launcher.command.Command;

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