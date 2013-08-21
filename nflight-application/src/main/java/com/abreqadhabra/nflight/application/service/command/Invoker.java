package com.abreqadhabra.nflight.application.service.command;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class Invoker {
	private static Class<Invoker> THIS_CLAZZ = Invoker.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	Queue<Command> cmdQueue = new LinkedList<Command>();

	public void add(Command cmd) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		this.cmdQueue.offer(cmd);

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(),
				METHOD_NAME, "cmdQueue  :" + this.cmdQueue.size());
	}

	public void clear() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		this.cmdQueue.clear();
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(),
				METHOD_NAME, "cmdQueue  :" + this.cmdQueue.size());
	}

	public void execute(Command command) throws Exception {
		command.execute();
	}

	public void executeAll() throws Exception {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		while (this.cmdQueue.peek() != null) {
			Command cmd = this.cmdQueue.poll();
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(),
					METHOD_NAME, "execute  :" + cmd.getClass().getSimpleName());
			cmd.execute();
		}
	}

}