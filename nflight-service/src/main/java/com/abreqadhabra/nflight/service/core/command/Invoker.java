package com.abreqadhabra.nflight.service.core.command;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.server.NFlightServer;

public class Invoker {
	private static final Class<Invoker> THIS_CLAZZ = Invoker.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	Queue<Command> cmdQueue = new LinkedList<Command>();

	public void execute(Command command) throws Exception {
		command.execute();
	}

	public void executeAll() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		while (cmdQueue.peek() != null) {
			Command cmd = cmdQueue.poll();
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"execute  :" + cmd.getClass().getSimpleName());
			cmd.execute();
		}
	}

	public void add(Command cmd) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		cmdQueue.offer(cmd);

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"cmdQueue  :" + cmdQueue.size());
	}

	public void clear() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		cmdQueue.clear();
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"cmdQueue  :" + cmdQueue.size());
	}

}