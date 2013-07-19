package com.abreqadhabra.nflight.service.core.server.command;

import com.abreqadhabra.nflight.service.core.command.Command;
import com.abreqadhabra.nflight.service.core.server.AbstractServerReceiver;

public class ShutdownServerConcreteCommand implements Command {
	private AbstractServerReceiver server;

	public ShutdownServerConcreteCommand(AbstractServerReceiver server) {
		this.server = server;
	}

	public void execute() throws Exception {
		this.server.shutdown();
	}
}