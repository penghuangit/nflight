package com.abreqadhabra.nflight.application.transport.rmi;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.concurrency.thread.ThreadHelper;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ServantShutdownHook implements Runnable {
	private static Class<ServantShutdownHook> THIS_CLAZZ = ServantShutdownHook.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private Servant servant;

	public ServantShutdownHook(Servant servant) {
		this.servant = servant;
	}

	@Override
	public void run() {
		final Thread CURRENT_THREAD = Thread.currentThread();
		final String METHOD_NAME = CURRENT_THREAD.getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, CLAZZ_NAME, METHOD_NAME, "current thread is "
				+ ThreadHelper.getThreadName(CURRENT_THREAD));

		try {
			this.servant.stop();
		} catch (Exception e) {
			StackTraceElement[] current = e.getStackTrace();
			if (e instanceof NFlightException) {
				NFlightException ne = (NFlightException) e;
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(),
						"\n" + NFlightException.getStackTrace(ne));
				ThreadHelper.interrupt(Thread.currentThread());
			} else {
				e.printStackTrace();
				ThreadHelper.shutdown();
			}
		}
	}

	public Thread getThread() {
		return new Thread(this);
	}
}
