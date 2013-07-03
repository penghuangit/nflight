package com.abreqadhabra.nflight.server.app;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyLoader;
import com.abreqadhabra.nflight.server.exception.NFlightServerException;

public class NFlightCommands {

	private static final Class<NFlightCommands> THIS_CLAZZ = NFlightCommands.class;
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

	public static final String NFLIGHT_SYSTEM_OS_WINDOWS = "windows";
	public static final String NFLIGHT_SYSTEM_OS_LINUX = "linux";

	public static final String NFLIGHT_SYSTEM_OS_DEFAULT = NFLIGHT_SYSTEM_OS_WINDOWS;

	public static final String NFLIGHT_SYSTEM_PROPERTY_KEY = "nflight.server.system";

	public static final String NFLIGHT_SYSTEM_PROPERTY_OS_KEY = NFLIGHT_SYSTEM_PROPERTY_KEY
			+ ".os";

	public static final String NFLIGHT_SYSTEM_STARTUP = "startup.command";
	public static final String NFLIGHT_SYSTEM_SHUTDOWN = "shutdown.command";

	public NFlightCommands() throws Exception {
		loadProperties();

	}

	protected void loadProperties() throws Exception {

		boolean isLoaded = false;
		// コンフィグファイル（nflight.properties）をシステムプロパティーに反映させます。
		isLoaded = PropertyLoader.load(NFLIGHT_PROPERTY_NAME,
				NFLIGHT_CONFIG_FILE_NAME);
		if (isLoaded == false) {
			throw new NFlightServerException("Can't read property file")
					.addContextValue("NFLIGHT_PROPERTY_NAME",
							NFLIGHT_PROPERTY_NAME).addContextValue(
							"NFLIGHT_CONFIG_FILE_NAME",
							NFLIGHT_CONFIG_FILE_NAME);
		} else {
			// コンフィグファイル（nflight_system.properties）をシステムプロパティーに反映させます。
			isLoaded = PropertyLoader.load(NFLIGHT_SYSTEM_PROPERTY_NAME,
					NFLIGHT_SYSTEM_CONFIG_FILE_NAME);
			if (isLoaded == false) {
				throw new NFlightServerException(
						"Can't read system property file").addContextValue(
						"NFLIGHT_PROPERTY_NAME", NFLIGHT_PROPERTY_NAME)
						.addContextValue("NFLIGHT_CONFIG_FILE_NAME",
								NFLIGHT_CONFIG_FILE_NAME);
			}
		}
	}

	/**
	 * <p>
	 * [개 요] 데이터 서버를 시작하는 명령입니다.
	 * </p>
	 * <p>
	 * [상 세] 데이터 서버를 시작하는 명령입니다.
	 * </p>
	 * <p>
	 * [비 고]
	 * </p>
	 * 
	 * @param String
	 *            command
	 * @throws Exception
	 * @since STEP1
	 */
	protected void boot(String command) throws Exception {
		final String METHOD_NAME = "void boot(String command)";

		try {
			Process proc = new ProcessBuilder(command).start();
		} catch (IOException ioe) {
			throw new NFlightServerException(
					"Can't boot background process.Command :" + command, ioe);
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

	protected String getStartupCommand(String command) {
		final String METHOD_NAME = "String getBootCommand(String commandName)";

		// java -D<name>=<value> 시스템 속성
		String os = System.getProperty(NFLIGHT_SYSTEM_PROPERTY_OS_KEY).trim();
		if (os == null) {
			os = NFLIGHT_SYSTEM_OS_DEFAULT;
		}
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				NFLIGHT_SYSTEM_PROPERTY_OS_KEY + ": " + os);

		String keyName = null;
		if (NFLIGHT_SYSTEM_OS_WINDOWS.equals(os)) {
			keyName = NFLIGHT_SYSTEM_PROPERTY_KEY + "." + command + "."
					+ NFLIGHT_SYSTEM_STARTUP + "." + NFLIGHT_SYSTEM_OS_WINDOWS;
			command = System.getProperty(keyName);
		} else if (NFLIGHT_SYSTEM_OS_LINUX.equals(os)) {

			keyName = NFLIGHT_SYSTEM_PROPERTY_KEY + "." + command + "."
					+ NFLIGHT_SYSTEM_STARTUP + "." + NFLIGHT_SYSTEM_OS_LINUX;
			command = System.getProperty(keyName);
		} else {
			LOGGER.logp(Level.SEVERE, THIS_CLAZZ.getName(), METHOD_NAME,
					"Incorrect property(" + NFLIGHT_SYSTEM_PROPERTY_OS_KEY
							+ "):" + os);
			System.exit(1);
		}

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, keyName
				+ ": " + command);

		return command;
	}

}
