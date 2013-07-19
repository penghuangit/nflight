package com.abreqadhabra.nflight.service.core.command;

import java.util.LinkedList;
import java.util.Queue;

public  class Invoker {
	Queue<Command> cmdQueue = new LinkedList<Command>();
	public void executeCommand(Command command) throws Exception {
		cmdQueue.offer(command);
		command.execute();
	}
}