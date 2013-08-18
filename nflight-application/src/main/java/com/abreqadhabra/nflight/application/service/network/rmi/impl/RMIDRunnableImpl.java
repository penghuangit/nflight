package com.abreqadhabra.nflight.application.service.network.rmi.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.concurrent.AbstractRunnable;
import com.abreqadhabra.nflight.application.common.concurrent.thread.ThreadHelper;
import com.abreqadhabra.nflight.application.common.launcher.Configure;
import com.abreqadhabra.nflight.application.common.launcher.runtime.process.ProcessExecutor;
import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class RMIDRunnableImpl extends AbstractRunnable {
	private static Class<RMIDRunnableImpl> THIS_CLAZZ = RMIDRunnableImpl.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private Configure configure;
	private ProcessExecutor process;

	public RMIDRunnableImpl(Configure configure) {
		this.configure = configure;
		this.process = new ProcessExecutor();
		this.setShutdownHook();
	}

	@Override
	protected void start() throws NFlightException {
		String command = this.configure
				.get(Configure.ACTIVATABLE_RMI_SYSTEM_COMMAND_RMID_START)
				+ " -J-D"
				+ Env.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString()
				+ "="
				+ Configure.FILE_RMID_POLICY
				+ " -log "
				+ Configure.CODE_BASE_PATH.resolve("rmid.log");
		this.process.execute(command);
	}

	@Override
	protected void stop() throws NFlightException, NFlightRemoteException {
		String command = this.configure
				.get(Configure.ACTIVATABLE_RMI_SYSTEM_COMMAND_RMID_STOP);
		this.process.execute(command);
		this.interrupt();
	}

	@Override
	protected void setShutdownHook() {
		super.setShutdownHookThread(new Thread(this.getShutdownHook()));
	}

	private Runnable getShutdownHook() {
		return new Runnable() {
			public Runnable init() {
				return (this);
			}
			@Override
			public void run() {
				final Thread CURRENT_THREAD = Thread.currentThread();
				final String METHOD_NAME = CURRENT_THREAD.getStackTrace()[1]
						.getMethodName();
				LOGGER.logp(
						Level.FINER,
						CLAZZ_NAME,
						METHOD_NAME,
						"current thread is "
								+ ThreadHelper.getThreadName(CURRENT_THREAD));
				try {
					RMIDRunnableImpl.this.stop();
				} catch (Exception e) {
					StackTraceElement[] current = e.getStackTrace();
					if (e instanceof NFlightException) {
						NFlightException ne = (NFlightException) e;
						LOGGER.logp(Level.SEVERE, current[0].getClassName(),
								current[0].getMethodName(), "\n"
										+ NFlightException.getStackTrace(ne));
						ThreadHelper.interrupt(Thread.currentThread());
					} else {
						e.printStackTrace();
						ThreadHelper.shutdown();
					}
				}
			}
		}.init();
	}
}
