package com.abreqadhabra.nflight.application.service.container.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.Config;
import com.abreqadhabra.nflight.application.common.launcher.LauncherHelper;
import com.abreqadhabra.nflight.application.common.launcher.concurrent.thread.ThreadHelper;
import com.abreqadhabra.nflight.application.service.ServiceFactory;
import com.abreqadhabra.nflight.application.service.conf.ServiceConfig;
import com.abreqadhabra.nflight.application.service.conf.ServiceConfig.ENUM_SERVICE_TYPE;
import com.abreqadhabra.nflight.application.service.thread.ServiceThreadFactory;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class TestContainerImpl {
	private static Class<TestContainerImpl> THIS_CLAZZ = TestContainerImpl.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private HashMap<String, CompletionService<Object>> services = new HashMap<String, CompletionService<Object>>();
	boolean isRunning;

	public TestContainerImpl() throws NFlightException, IOException {

		// 시스템프로퍼티 등록
		Config.load(THIS_CLAZZ, ServiceConfig.PATH_SERVICE_PROPERTIES);

		this.isRunning = true;
		this.init();

		LauncherHelper.setSecurityManager();// 추후 삭제
	}

	public void startupService(ENUM_SERVICE_TYPE serviceType) {

	}

	public void startupAllService() throws ExecutionException,
			NFlightException, NFlightRemoteException, InterruptedException {
		final Thread CURRENT_THREAD = Thread.currentThread();
		final String METHOD_NAME = CURRENT_THREAD.getStackTrace()[1]
				.getMethodName();
		
		for (String serviceType : this.services.keySet()) {
			Future<Object> future = this.services.get(serviceType).take();
			
//			Service service = (Service) future.get();
//			LOGGER.logp(Level.FINER, CLAZZ_NAME, METHOD_NAME, serviceType + " status :"
//					+ service.status());
		}

	}

	public CompletionService<Object> getCompletionService(String serviceType)
			throws InterruptedException {
		ThreadFactory threadFactory = new ServiceThreadFactory(serviceType);
		ExecutorService executor = Executors
				.newSingleThreadExecutor(threadFactory);

		return new ExecutorCompletionService<Object>(executor);
	}

	public void shutdown(ENUM_SERVICE_TYPE serviceType) {
		// TODO Auto-generated method stub

	}

	public void shutdownAllService() {
		for (String serviceType : this.services.keySet()) {
			// new Thread(this.services.get(serviceType), serviceType).start();
		}
	}

	private void init() throws NFlightException {
		String serviceName = null;
		Callable<Object> serviceTask = null;
		CompletionService<Object> completionService = null;
		try {
			for (ENUM_SERVICE_TYPE serviceType : ENUM_SERVICE_TYPE.values()) {
				serviceName = serviceType.name();
				serviceTask = ServiceFactory.createtServiceTask(serviceType);
				completionService = getCompletionService(serviceName);
				completionService.submit(serviceTask);
				this.services.put(serviceName, completionService);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} catch (NFlightException e) {
			throw e;
		}
	}

	public static void main(String[] args) {
		try {
			TestContainerImpl container = new TestContainerImpl();

			container.startupAllService();

			container.shutdownAllService();

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

}