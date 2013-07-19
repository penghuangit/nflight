package com.abreqadhabra.nflight.service.core.designpattern;

import java.util.LinkedList;
import java.util.Queue;

 interface Command {
	public abstract void execute();
}

// Receiver class.
 class AbstractServerReceiver {
	public void startup() {
		System.out.println("startup");
	}

	public void shutdown() {
		System.out.println("shutdown");
	}

	public void stop() {
		System.out.println("stop");
	}
}

// Invoker.
 class Invoker {
	Queue<Command> cmdQueue = new LinkedList<Command>();
	public void executeCommand(Command command) {
		cmdQueue.offer(command);
		command.execute();
	}
}

// ConcreteCommand Class.
 class ShutdownServerConcreteCommand implements Command {
	private AbstractServerReceiver server;

	public ShutdownServerConcreteCommand(AbstractServerReceiver server) {
		this.server = server;
	}

	public void execute() {
		this.server.shutdown();
	}
}

// Client
public class AbstractServerCommandTest {
	public static void main(String[] args) {
		AbstractServerReceiver receiver = new AbstractServerReceiver();
		ShutdownServerConcreteCommand sscc = new ShutdownServerConcreteCommand(
				receiver);
		Invoker invoker = new Invoker();

		invoker.executeCommand(sscc); // shutdown server
	}
}