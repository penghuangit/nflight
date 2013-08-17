package com.abreqadhabra.nflight.application.common.launcher.runtime.process;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.application.common.launcher.Configure;
import com.abreqadhabra.nflight.application.common.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.IOStream;

public class ProcessExecutor {
	private static Class<ProcessExecutor> THIS_CLAZZ = ProcessExecutor.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	Configure configure;

	public ProcessExecutor() {
		try {
			configure = new ConfigureImpl(THIS_CLAZZ,
					Configure.FILE_SYSTEM_COMMAND_PROPERTIES);
		} catch (Exception e) {
			StackTraceElement[] current = e.getStackTrace();
			if (e instanceof NFlightException) {
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(),
						"\n" + NFlightException.getStackTrace(e));
			} else {
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(), e.getMessage());
			}
		}
	}

	/**
	 * Main to start the <code> ServerApplication </code> and creates the
	 * Instance of <code> ServerController </code>. Create a RMI acceptor with
	 * the specified port(1024-65535).
	 * 
	 * @param args
	 *            The port of naming service like rmi registry.
	 */
	public static void main(String[] args) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, "args: "
				+ Arrays.toString(args));

		ProcessExecutor systemCommand = new ProcessExecutor();
		String command = null;

		if (args.length == 1) {
			command = args[0];
		} else if (args.length == 0) {
			System.err.println("사용법을 확인하세요.");
		}
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, "command: "
				+ command);
		try {
			systemCommand.execute(command);
		} catch (Exception e) {
			StackTraceElement[] current = e.getStackTrace();
			if (e instanceof NFlightException) {
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(),
						"\n" + NFlightException.getStackTrace(e));
			} else {
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(),
						"\n" + NFlightException.getStackTrace(e));
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
	 * @throws NFlightException 
	 * @throws Exception
	 * @since STEP1
	 */
	public void execute(String command) throws NFlightException  {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME, "command: "
				+ command);

		if (command != null) {
			try {
				Runtime rt = Runtime.getRuntime();
				Process proc = rt.exec(command);
				Thread.sleep(configure
						.getInt(Configure.LAUNCHER_SYSTEM_COMMAND_SLEEPTIME_1));
				String errorString = IOStream.convertStreamToString(proc
						.getErrorStream());
				String outputString = IOStream.convertStreamToString(proc
						.getInputStream());
				// any error message?
				if (errorString.length() != 0) {
					LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
							"command-line output of the subprocess"
									+ errorString);
				}
				// any output?
				if (outputString.length() != 0) {
					LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
							"command-line output of the subprocess"
									+ outputString);
				}
				// any error???
				int exitValue = proc.waitFor();
				if (exitValue == 0) {
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
							"subprocess normal termination :" + exitValue);
				} else {
					throw new SystemCommandException(command
							+ ": subprocess abnormal termination :" + exitValue);
				}

			} catch (InterruptedException e) {
				StackTraceElement[] current = e.getStackTrace();
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(), "이 오류는 발생하지 않습니다.");
			} catch (IOException ioe) {
				throw new SystemCommandException(
						"Can't boot background process.Command: " + command,
						ioe);
			}
		} else {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"command :" + command);
		}
	}

}
