package com.abreqadhabra.nflight.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.server.app.NFlightCommands;
import com.abreqadhabra.nflight.server.rmi.RMIServerImpl;

/**
 * <p>
 * [개 요] 모든 서버프로세스를 기동합니다.
 * </p>
 * <p>
 * [설 명] 모든 서버프로세스를 기동합니다.
 * </p>
 * <p>
 * [비 고]
 * </p>
 * <p>
 * [환 경] Java SDK 1.7_25
 * </p>
 * <p>
 * Copyright(c) Kim, Dong-Sup 2013
 * </p>
 * 
 * @author dongsup.kim@gmail.com
 * @since STEP1
 * @see StartupAllServer
 */
public class StartupAllServer extends NFlightCommands {

	private static final Class<StartupAllServer> THIS_CLAZZ = StartupAllServer.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public StartupAllServer() throws Exception {
		super();
	}

	/**
	 * <p>
	 * [개 요] 모든 서버프로세스를 기동합니다.
	 * </p>
	 * <p>
	 * [상 세] 모든 서버프로세스를 기동합니다.
	 * </p>
	 * <p>
	 * [비 고]
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 * @since STEP1
	 */
	public static void main(String[] args) throws Exception {
		final String METHOD_NAME = "void main(String[] args)";

		StartupAllServer sas = new StartupAllServer();

		// 각종 커맨드입니다.
		String rmiserverCommand = sas.getStartupCommand("rmiserver");

		// RMI Server Startup
		sas.startupRMIServer(rmiserverCommand);
	}

	protected String getStartupCommand(String command) {
		return super.getStartupCommand(command);
	}

	protected void startupRMIServer(String command) throws Exception {
		final String METHOD_NAME = "void startupRMIServer(String command)";

		if (RMIServerImpl.checkStatus()) {
			LOGGER.logp(Level.WARNING, THIS_CLAZZ.getName(), METHOD_NAME,
					"NFlight RMI Server is already running.");
		} else {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Startup background process.Command :" + command);
			super.boot(command);
		}
	}
}
