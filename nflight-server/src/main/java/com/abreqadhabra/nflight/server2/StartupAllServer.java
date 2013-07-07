package com.abreqadhabra.nflight.server2;

import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.server2.core.NFlightServer;
import com.abreqadhabra.nflight.server2.rmi.RMIServer;

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
public class StartupAllServer{

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
		// RMI Server Startup
		NFlightServer  rmiServer = new RMIServer();
		rmiServer.exec();
	}

}
