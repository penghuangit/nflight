package com.abreqadhabra.nflight.server.bin;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyLoader;
import com.abreqadhabra.nflight.server.ns.rmi.bin.NRSCommands;

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
 * @see StartAllServer
 */
public class StartAllServer {

	private static final Class<StartAllServer> THIS_CLAZZ = StartAllServer.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	/*
	 * nflight.propertiesコンフィグファイル名のキー値です。
	 */
	public static final String NFLIGHT_SYSTEM_PROPERTY_NAME = "nflight.server.system.PropertyFileName";
	/**
	 * nflight.propertiesコンフィグファイル名です。ここでは、nflight.propertiesファイルを指しています。
	 */
	public static final String NFLIGHT_SYSTEM_CONFIG_FILE_NAME = "com/abreqadhabra/nflight/server/config/nflight_system.properties";

	/*
	 * nflight.propertiesコンフィグファイル名のキー値です。
	 */
	public static final String NFLIGHT_PROPERTY_NAME = "nflight.server.PropertyFileName";
	/**
	 * nflight.propertiesコンフィグファイル名です。ここでは、nflight.propertiesファイルを指しています。
	 */
	public static final String NFLIGHT_CONFIG_FILE_NAME = "com/abreqadhabra/nflight/server/config/nflight.properties";

	public static final String NFLIGHT_DEFAULT_OS = "windows";

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

		// 설정파일(nflight_system.properties)을 시스템프로퍼티에 반영합니다.
		boolean bl = PropertyLoader.load(NFLIGHT_SYSTEM_PROPERTY_NAME,
				NFLIGHT_SYSTEM_CONFIG_FILE_NAME);
		if (bl == false) {
			LOGGER.logp(Level.SEVERE, THIS_CLAZZ.getName(), METHOD_NAME,
					"Can't read property file("
							+ NFLIGHT_SYSTEM_CONFIG_FILE_NAME + ")");
			System.exit(1);
		}

		// 설정파일(nflight.properties)을 시스템프로퍼티에 반영합니다.
		bl = PropertyLoader.load(NFLIGHT_PROPERTY_NAME,
				NFLIGHT_CONFIG_FILE_NAME);
		if (bl == false) {
			LOGGER.logp(Level.SEVERE, THIS_CLAZZ.getName(), METHOD_NAME,
					"Can't read property file(" + NFLIGHT_CONFIG_FILE_NAME
							+ ")");
			System.exit(1);
		}

		// java -D<name>=<value> 시스템 속성
		String os = System.getProperty("nflight.server.system.os").trim();
		if (os == null) {
			os = NFLIGHT_DEFAULT_OS;
		}
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"nflight.server.system.os" + os);
		// 각종 커맨드입니다.
		String nsr = null;

		if (os.equals("windows")) {
			nsr = System
					.getProperty("nflight.server.system.ns.rmi.bootcommand.window");
		} else if (os.equals("linux")) {
			nsr = System
					.getProperty("nflight.server.system.ns.rmi.bootcommand.linux");
		} else {
			LOGGER.logp(Level.SEVERE, THIS_CLAZZ.getName(), METHOD_NAME,
					"Incorrect property(nflight.server.system.os):" + os);
			System.exit(1);
		}

		// 런타임을 획득합니다.
		Runtime runtime = Runtime.getRuntime();

		// 데이터서버의 RMI 레지스트리의 포트 번호를 가져옵니다. - java -D<name>=<value> 시스템 속성
		String nrsURL = System.getProperty("nflight.server.nrs.rmi.url").trim();

		String dsport = nrsURL.substring(nrsURL.indexOf(':') + 1,
				nrsURL.lastIndexOf('/'));

		if (NRSCommands.checkstatus("localhost", dsport)) {
			LOGGER.logp(Level.WARNING, THIS_CLAZZ.getName(), METHOD_NAME,
					"NFlight RMI Server is already running.");
		} else {
			try {

				runtime.exec(nsr);
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"nsr:" + nsr);

				// Process proc = new ProcessBuilder(nsr).start();

			} catch (IOException e) {
				StackTraceElement[] current = e.getStackTrace();
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(),
						"Can't boot background process.Command :" + nsr + "\n"
								+ e.getMessage());
			}
		}

		try {
			Thread.sleep(Integer.parseInt(System
					.getProperty("nflight.server.system.sleeptime2")));
		} catch (InterruptedException e) {
			StackTraceElement[] current = e.getStackTrace();
			LOGGER.logp(Level.SEVERE, current[0].getClassName(),
					current[0].getMethodName(), "이 오류는 발생하지 않습니다.");
		}

	}

}
