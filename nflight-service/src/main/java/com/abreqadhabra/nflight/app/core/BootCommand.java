package com.abreqadhabra.nflight.app.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.app.core.exception.NFBootCommandException;
import com.abreqadhabra.nflight.common.exception.WrapperException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.IOStream;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.common.util.PropertyLoader;

public class BootCommand {

	private static final Class<BootCommand> THIS_CLAZZ = BootCommand.class;
	private static final Logger LOGGER = LoggingHelper
			.getLogger(BootCommand.THIS_CLAZZ);

	static {
		try {
			final Properties props = PropertyFile.readPropertyFilePath(
					BootCommand.THIS_CLAZZ.getName(),
					Profile.FILE_BOOTCOMMAND_PROPERTIES);
			PropertyLoader.setSystemProperties(props);
		} catch (final Exception e) {
			final StackTraceElement[] current = e.getStackTrace();
			if (e instanceof WrapperException) {
				BootCommand.LOGGER.logp(Level.SEVERE,
						current[0].getClassName(), current[0].getMethodName(),
						"\n" + WrapperException.getStackTrace(e));
			} else {
				BootCommand.LOGGER.logp(Level.SEVERE,
						current[0].getClassName(), current[0].getMethodName(),
						e.getMessage());
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
	public static void main(final String[] args) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		BootCommand.LOGGER.logp(Level.FINER, BootCommand.THIS_CLAZZ.getName(),
				METHOD_NAME, "args: " + Arrays.toString(args));

		final BootCommand bootCommand = new BootCommand();
		String command = null;

		if (args.length == 1) {
			command = args[0];
		} else if (args.length == 0) {
			command = System
					.getProperty(Profile.PROPERTIES_BOOTCOMMAND.NFLIGHT_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_START_WINDOWS
							.toString());
			// command =
			// System.getProperty(Env.Properties.BootCommand.PropertyKey.NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_STOP_WINDOWS
			// .toString());

		}
		BootCommand.LOGGER.logp(Level.FINER, BootCommand.THIS_CLAZZ.getName(),
				METHOD_NAME, "command: " + command);
		try {
			bootCommand.execute(command);
		} catch (final Exception e) {
			final StackTraceElement[] current = e.getStackTrace();
			if (e instanceof WrapperException) {
				BootCommand.LOGGER.logp(Level.SEVERE,
						current[0].getClassName(), current[0].getMethodName(),
						"\n" + WrapperException.getStackTrace(e));
			} else {
				BootCommand.LOGGER.logp(Level.SEVERE,
						current[0].getClassName(), current[0].getMethodName(),
						"\n" + WrapperException.getStackTrace(e));
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
	public void execute(final String command) throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		BootCommand.LOGGER.logp(Level.INFO, BootCommand.THIS_CLAZZ.getName(),
				METHOD_NAME, "command: " + command);

		if (command != null) {
			try {
				final Runtime rt = Runtime.getRuntime();
				final Process proc = rt.exec(command);
				Thread.sleep(Profile.BOOTCOMMAND_SLEEPTIME_1);
				final String errorString = IOStream.convertStreamToString(proc
						.getErrorStream());
				final String outputString = IOStream.convertStreamToString(proc
						.getInputStream());
				// any error message?
				if (errorString.length() != 0) {
					BootCommand.LOGGER.logp(Level.INFO,
							BootCommand.THIS_CLAZZ.getName(), METHOD_NAME,
							"command-line output of the subprocess"
									+ errorString);
				}
				// any output?
				if (outputString.length() != 0) {
					BootCommand.LOGGER.logp(Level.INFO,
							BootCommand.THIS_CLAZZ.getName(), METHOD_NAME,
							"command-line output of the subprocess"
									+ outputString);
				}
				// any error???
				final int exitValue = proc.waitFor();
				if (exitValue == 0) {
					BootCommand.LOGGER.logp(Level.FINER,
							BootCommand.THIS_CLAZZ.getName(), METHOD_NAME,
							"subprocess normal termination :" + exitValue);
				} else {
					throw new NFBootCommandException(command
							+ ": subprocess abnormal termination :" + exitValue);
				}

			} catch (final InterruptedException e) {
				final StackTraceElement[] current = e.getStackTrace();
				BootCommand.LOGGER.logp(Level.SEVERE,
						current[0].getClassName(), current[0].getMethodName(),
						"이 오류는 발생하지 않습니다.");
			} catch (final IOException ioe) {
				throw new NFBootCommandException(
						"Can't boot background process.Command: " + command,
						ioe);
			}
		} else {
			BootCommand.LOGGER.logp(Level.FINER,
					BootCommand.THIS_CLAZZ.getName(), METHOD_NAME, "command :"
							+ command);
		}
	}

}
