package com.abreqadhabra.nflight.service.core.server.command;

import com.abreqadhabra.nflight.service.core.command.Command;
import com.abreqadhabra.nflight.service.core.server.AbstractServerReceiver;

public class StatusServerConcreteCommand implements Command {
	private AbstractServerReceiver server;

	public StatusServerConcreteCommand(AbstractServerReceiver server) {
		this.server = server;
	}

	public void execute() throws Exception {
		this.server.status();
	}
}