package com.abreqadhabra.nflight.application.service.network.rmi;

import java.net.InetAddress;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.concurrent.AbstractRunnable;
import com.abreqadhabra.nflight.application.common.launcher.concurrent.thread.ThreadHelper;
import com.abreqadhabra.nflight.application.service.network.rmi.helper.RMIServantHelper;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public abstract class AbstractRMIServant extends AbstractRunnable
		implements
			RMIServant {
	private static Class<AbstractRMIServant> THIS_CLAZZ = AbstractRMIServant.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	protected boolean isRunning;
	private InetAddress addr;
	private int port;
	protected Registry registry;
	protected String boundName;

	public AbstractRMIServant() {
	}

	public AbstractRMIServant(boolean isRunning, InetAddress addr, int port,
			String serviceName) throws NFlightRemoteException {
		this.isRunning = isRunning;
		this.addr = addr;
		this.port = port;
		this.registry = RMIServantHelper.getRegistry(
				this.addr.getHostAddress(), this.port);
		this.boundName = RMIServantHelper.getBoundName(addr.getHostAddress(),
				port, serviceName);
		this.setShutdownHook();
	}

	@Override
	public String sayHello() throws NFlightRemoteException {
		return this.boundName + ": Hello, world!";
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
					LOGGER.logp(Level.SEVERE, CLAZZ_NAME, METHOD_NAME,
							"Stopping...");
					AbstractRMIServant.this.stop();
					LOGGER.logp(Level.SEVERE, CLAZZ_NAME, METHOD_NAME,
							"Stopped");
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
