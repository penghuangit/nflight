package com.abreqadhabra.nflight.server.core;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.server.ns.rmi.ServerController;

public class CommandProcessor {

	private static final Class<CommandProcessor> THIS_CLAZZ = CommandProcessor.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	
	private static CommandProcessor thisInstance;
	private LinkedList<Runnable> terminators = new LinkedList<Runnable>();

	static {
		thisInstance = new CommandProcessor();
	}
	
	/**
	 * This method returns the singleton instance of this class
	 * that should be then used to create agent containers.
	 **/
	public static CommandProcessor instance() {
		return thisInstance;
	}

	/**
    Causes the local JVM to be closed when the last container in this
    JVM terminates.
	 */
	public void setCloseVM(boolean flag) {
		final String METHOD_NAME = "void setCloseVM(boolean flag)";

		if (flag) {
			terminators.addLast(new Runnable() {
				public void run() {
					// Give one more chance to other threads to complete
					Thread.yield();
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, "NFlight is closing down now.");
					System.exit(0);
				}
			} );
		}
	}

	public void createRMIContainer() {

	new ServerController(9999);
		
	}
}
