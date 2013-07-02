package com.abreqadhabra.nflight.server.bin;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyLoader;

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

	public static void main(String[] args) {
		final String METHOD_NAME = "void main(String[] args)";

		// 설정파일(nflight_system.properties)을 시스템프로퍼티에 반영합니다.
		boolean bl = PropertyLoader.load(
				StartupAllServer.NFLIGHT_SYSTEM_PROPERTY_NAME,
				StartupAllServer.NFLIGHT_SYSTEM_CONFIG_FILE_NAME);
		if (bl == false) {
			LOGGER.logp(Level.SEVERE, THIS_CLAZZ.getName(), METHOD_NAME,
					"Can't read property file("
							+ StartupAllServer.NFLIGHT_SYSTEM_CONFIG_FILE_NAME
							+ ")");
			System.exit(1);
		}

		// 설정파일(nflight.properties)을 시스템프로퍼티에 반영합니다.
		bl = PropertyLoader.load(StartupAllServer.NFLIGHT_PROPERTY_NAME,
				StartupAllServer.NFLIGHT_CONFIG_FILE_NAME);
		if (bl == false) {
			LOGGER.logp(Level.SEVERE, THIS_CLAZZ.getName(), METHOD_NAME,
					"Can't read property file("
							+ StartupAllServer.NFLIGHT_CONFIG_FILE_NAME + ")");
			System.exit(1);
		}

		// java -D<name>=<value> 시스템 속성
		String os = System.getProperty("nflight.server.system.os").trim();
		if (os == null) {
			os = StartupAllServer.NFLIGHT_DEFAULT_OS;
		}
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
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
		}

	}
}
