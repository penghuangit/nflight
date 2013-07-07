package com.abreqadhabra.nflight.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.NFlightPropertyException;
import com.abreqadhabra.nflight.common.exception.NFlightSystemException;
import com.abreqadhabra.nflight.common.exception.WrapperException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyLoader;

public class BootCommand {

	private static final Class<BootCommand> THIS_CLAZZ = BootCommand.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	/**
	 * nflight.propertiesコンフィグファイル名です。ここでは、nflight.propertiesファイルを指しています。
	 */
	public static final String BOOT_COMMAND_CONFIG_FILE_NAME = "/com/abreqadhabra/nflight/core/config/command.properties";

	public static final String BOOT_COMMAND_OS_WINDOWS = "windows";
	public static final String BOOT_COMMAND_OS_LINUX = "linux";

	public static final String BOOT_COMMAND_OS_DEFAULT = BOOT_COMMAND_OS_WINDOWS;

	public static final String BOOT_COMMAND_PROPERTY_KEY = "boot.command";

	public static final String BOOT_COMMAND_PROPERTY_OS_KEY = BOOT_COMMAND_PROPERTY_KEY
			+ ".os";

	static {
		boolean isLoaded = false;
		try {
			// コンフィグファイル（nflight.properties）をシステムプロパティーに反映させます。
			isLoaded = PropertyLoader.load(BOOT_COMMAND_CONFIG_FILE_NAME);
			if (isLoaded == false) {
				throw new NFlightPropertyException("Can't read property file")
						.addContextValue("NFLIGHT_CONFIG_FILE_NAME",
								BOOT_COMMAND_CONFIG_FILE_NAME);
			}
		} catch (Exception e) {
			StackTraceElement[] current = e.getStackTrace();
			if (e instanceof WrapperException) {
				WrapperException ce = (WrapperException) e;
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(), "\n" + ce.getStackTrace(e));
			} else {
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(), e.getMessage());
			}
		}
	}

	/**
	 * Main to start the <code> ServerApplication </code> and creates the
	 * Instance of <code> ServerController </code>. Create a RMI server with the
	 * specified port(1024-65535).
	 * 
	 * @param args
	 *            The port of naming service like rmi registry.
	 */
	public static void main(String[] args) {
		final String METHOD_NAME = "void main(String[] args)";

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, "args: "
				+ Arrays.toString(args));

		String command = getCommand();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, "command: "
				+ command);

		try {
			exec(command);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	protected static void exec(String command) throws Exception {
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(command);
		} catch (IOException ioe) {
			throw new NFlightSystemException(
					"Can't boot background process.Command :" + command, ioe);
		}
		try {
			Thread.sleep(Integer.parseInt(System
					.getProperty("boot.command.sleeptime2")));
		} catch (InterruptedException e) {
			StackTraceElement[] current = e.getStackTrace();
			LOGGER.logp(Level.SEVERE, current[0].getClassName(),
					current[0].getMethodName(), "이 오류는 발생하지 않습니다.");
		}
	}

	protected static String getCommand() {
		final String METHOD_NAME = "String getCommand()";

		String value = null;
		// java -D<name>=<value> 시스템 속성
		String os = System.getProperty(BOOT_COMMAND_PROPERTY_OS_KEY).trim();
		if (os == null) {
			os = BOOT_COMMAND_OS_DEFAULT;
		}
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				BOOT_COMMAND_PROPERTY_OS_KEY + ": " + os);
		String key = null;
		if (BOOT_COMMAND_OS_WINDOWS.equals(os)) {
			key = BOOT_COMMAND_PROPERTY_KEY + "." + BOOT_COMMAND_OS_WINDOWS;
			value = System.getProperty(key);
		} else if (BOOT_COMMAND_OS_LINUX.equals(os)) {
			key = BOOT_COMMAND_PROPERTY_KEY + "." + BOOT_COMMAND_OS_LINUX;
			value = System.getProperty(key);
		} else {
			LOGGER.logp(Level.SEVERE, THIS_CLAZZ.getName(), METHOD_NAME,
					"Incorrect property(" + BOOT_COMMAND_PROPERTY_OS_KEY + "):"
							+ os);
			System.exit(1);
		}
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, key + ": "
				+ value);

		return value;
	}
}
