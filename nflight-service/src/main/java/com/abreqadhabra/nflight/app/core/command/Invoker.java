package com.abreqadhabra.nflight.app.core.command;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class Invoker {
	private static final Class<Invoker> THIS_CLAZZ = Invoker.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	Queue<Command> cmdQueue = new LinkedList<Command>();

	public void add(final Command cmd) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		this.cmdQueue.offer(cmd);

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(),
				METHOD_NAME, "cmdQueue  :" + this.cmdQueue.size());
	}

	public void clear() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		this.cmdQueue.clear();
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(),
				METHOD_NAME, "cmdQueue  :" + this.cmdQueue.size());
	}

	public void execute(final Command command) throws Exception {
		command.execute();
	}

	public void executeAll() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		while (this.cmdQueue.peek() != null) {
			final Command cmd = this.cmdQueue.poll();
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(),
					METHOD_NAME, "execute  :" + cmd.getClass().getSimpleName());
			cmd.execute();
		}
	}

}