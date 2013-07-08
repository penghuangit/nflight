package com.abreqadhabra.nflight;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.Constants;
import com.abreqadhabra.nflight.common.exception.NFlightSystemException;
import com.abreqadhabra.nflight.common.exception.WrapperException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.common.util.PropertyLoader;

public class BootCommand {

	private static final Class<BootCommand> THIS_CLAZZ = BootCommand.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	static {
		try {
			Properties props = PropertyFile
					.readPropertyFile(Constants.BootCommand.DEFAULT_CONFIG_FILE_NAME);
			PropertyLoader.setSystemProperties(props);
		} catch (Exception e) {
			StackTraceElement[] current = e.getStackTrace();
			if (e instanceof WrapperException) {
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(),
						"\n" + WrapperException.getStackTrace(e));
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
			execute(command);
		} catch (Exception e) {
			StackTraceElement[] current = e.getStackTrace();
			if (e instanceof WrapperException) {
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(),
						"\n" + WrapperException.getStackTrace(e));
				Boot.printUsage(System.out);
				System.exit(-1);
			} else {
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(),
						"\n" + WrapperException.getStackTrace(e));
				Boot.printUsage(System.out);
				System.exit(-1);
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
	protected static void execute(String command) throws Exception {
		final String METHOD_NAME = "void execute(String command";

		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(command);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, "proc : " + proc.toString());
			Thread.sleep(Integer.parseInt(System
					.getProperty(Constants.BootCommand.KEY_COMMAND_SLEEP_TIME_2)));
		} catch (InterruptedException e) {
			StackTraceElement[] current = e.getStackTrace();
			LOGGER.logp(Level.SEVERE, current[0].getClassName(),
					current[0].getMethodName(), "이 오류는 발생하지 않습니다.");
		} catch (IOException ioe) {
			throw new NFlightSystemException(
					"Can't boot background process.Command :" + command, ioe);
		}
	}

	protected static String getCommand() {
		final String METHOD_NAME = "String getCommand()";

		// java -D<name>=<value> 시스템 속성
		String osName = System.getProperty(Constants.BootCommand.KEY_COMMAND_OS);
		if (osName == null) {
			osName = System.getProperty(Constants.BootCommand.KEY_COMMAND_OS);
		}
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				Constants.BootCommand.KEY_COMMAND_OS + ": " + osName);
		
		String key = null;
		String value = null;
		if (osName.equals(Constants.BootCommand.STR_COMMAND_OS_WINDOWS)) {
			key = Constants.BootCommand.KEY_COMMAND_OS_WINDOWS;
			value = System.getProperty(key);
		} else if (osName.equals(Constants.BootCommand.STR_COMMAND_OS_LINUX)) {
			key = Constants.BootCommand.KEY_COMMAND_OS_LINUX;
			value = System.getProperty(key);
		} else {
			LOGGER.logp(Level.SEVERE, THIS_CLAZZ.getName(), METHOD_NAME,
					"Incorrect property(" + Constants.BootCommand.KEY_COMMAND_OS + "):"
							+ osName);
			System.exit(1);
		}

		return value;
	}
}
