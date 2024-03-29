package com.abreqadhabra.nflight.samples;

import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

/**
 * <p>
 * [개 요] 모든 서버프로세스를 종료합니다.
 * </p>
 * <p>
 * [설 명] 모든 서버프로세스를 종료합니다.
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
 * @see ShutdownAllServer
 */
public class ShutdownAllServer {

	private static Class<ShutdownAllServer> THIS_CLAZZ = ShutdownAllServer.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	/**
	 * <p>
	 * [개 요] 모든 서버프로세스를 종료합니다.
	 * </p>
	 * <p>
	 * [상 세] 모든 서버프로세스를 종료합니다.
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
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		// RMI Server Shutdown
	//	NFlightServer  rmiServer = new RMIServer();
	//	rmiServer.exit();
	
	}
}
