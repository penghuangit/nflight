package com.abreqadhabra.nflight.application.common.launcher.concurrent;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.concurrent.thread.ThreadHelper;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public abstract class AbstractServiceCallable implements Callable<Object> {

	private static Class<AbstractServiceCallable> THIS_CLAZZ = AbstractServiceCallable.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private Thread shutdownHookThread;

	public AbstractServiceCallable() {
	}

	@Override
	public Object call() {
		final Thread CURRENT_THREAD = Thread.currentThread();
		final String METHOD_NAME = CURRENT_THREAD.getStackTrace()[1]
				.getMethodName();

		// 어플리케이션의 종료를 안전하게 처리
		Runtime.getRuntime().addShutdownHook(this.shutdownHookThread);
		LOGGER.logp(Level.FINER, CLAZZ_NAME, METHOD_NAME, "current thread is "
				+ ThreadHelper.getThreadName(CURRENT_THREAD));

		try {
			this.start();
		} catch (Exception e) {
			StackTraceElement[] current = e.getStackTrace();
			if (e instanceof NFlightException) {
				NFlightException ne = (NFlightException) e;
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(),
						"\n" + NFlightException.getStackTrace(ne));
				interrupt(CURRENT_THREAD);
			} else {
				e.printStackTrace();
				ThreadHelper.shutdown();
			}
		}
		return this;
	}

	protected abstract void start() throws NFlightException,
			NFlightRemoteException;
	protected abstract void stop() throws NFlightException,
			NFlightRemoteException;
	protected abstract void setShutdownHook();

	protected Thread getShutdownHookThread() {
		return this.shutdownHookThread;
	}

	protected void setShutdownHookThread(Thread shutdownHookThread) {
		this.shutdownHookThread = shutdownHookThread;
	}

	/**
	 * stop execution of the task
	 * @param currentThread 
	 * 
	 * @param thread
	 */
	protected void interrupt(Thread currentThread) {
		ThreadHelper.interrupt(currentThread);
	}
	
	protected void interrupt() {
		
		ThreadHelper.interrupt(Thread.currentThread());
	}
}
