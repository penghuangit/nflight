package com.abreqadhabra.nflight.application.transport.rmi.rmid;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.concurrency.thread.ThreadHelper;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class RMIDShutdownHook implements Runnable {
	private static Class<RMIDShutdownHook> THIS_CLAZZ = RMIDShutdownHook.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private RMIDProcess rmidProcess;
	public RMIDShutdownHook(RMIDProcess rmidProcess) {
		this.rmidProcess=rmidProcess;
	}

	@Override
	public void run() {
		final Thread CURRENT_THREAD = Thread.currentThread();
		final String METHOD_NAME = CURRENT_THREAD.getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, CLAZZ_NAME, METHOD_NAME, "current thread is "
				+ ThreadHelper.getThreadName(CURRENT_THREAD));

		try {
			this.rmidProcess.stop();
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