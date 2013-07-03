package com.abreqadhabra.nflight.server.bin;

import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.server.NFlightServer;
import com.abreqadhabra.nflight.server.rmi.RMIServerImpl;

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

	private static final Class<ShutdownAllServer> THIS_CLAZZ = ShutdownAllServer.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

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
		final String METHOD_NAME = "void main(String[] args)";

		// RMI Server Shutdown
		NFlightServer  rmiServer = new RMIServerImpl();
		rmiServer.exit();
	
/*		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"nflight.server.system.os:" + os);
		// 각종 커맨드입니다.
		String nsr = null;

		if (os.equals("windows")) {
			nsr = System
					.getProperty("nflight.server.system.ns.rmi.shutdowncommand.window");
		} else if (os.equals("linux")) {
			nsr = System
					.getProperty("nflight.server.system.ns.rmi.shutdowncommand.linux");
		} else {
			LOGGER.logp(Level.SEVERE, THIS_CLAZZ.getName(), METHOD_NAME,
					"Incorrect property(nflight.server.system.os):" + os);
			System.exit(1);
		}

		// 런타임을 획득합니다.
		Runtime runtime = Runtime.getRuntime();

		// データ削除を終了します。
		try {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"nflight.server.system.ns.rmi.shutdowncommand.window:"
							+ nsr);
			runtime.exec(nsr);
			// Process proc = new ProcessBuilder(nsr).start();
		} catch (IOException e) {
			StackTraceElement[] current = e.getStackTrace();
			LOGGER.logp(Level.SEVERE, current[0].getClassName(),
					current[0].getMethodName(),
					"Can't shutdown background process.Command :" + nsr + "\n"
							+ e.getMessage());
		}*/

	}
}
