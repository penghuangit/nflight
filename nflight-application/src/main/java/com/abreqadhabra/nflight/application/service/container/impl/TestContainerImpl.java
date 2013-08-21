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
import com.abreqadhabra.nflight.application.service.network.rmi.AbstractRMIServant;
import com.abreqadhabra.nflight.application.service.network.rmi.impl.UnicastRMIServantImpl;
import com.abreqadhabra.nflight.application.service.network.socket.impl.datagram.MulticastDatagramServiceImpl;
import com.abreqadhabra.nflight.application.service.thread.ServiceThreadFactory;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class TestContainerImpl {
	private static Class<TestContainerImpl> THIS_CLAZZ = TestContainerImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private HashMap<String, Callable<String>> services = new HashMap<String, Callable<String>>();
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
	
	public void startupAllService() throws InterruptedException, ExecutionException, NFlightException, NFlightRemoteException {

//		ThreadGroup serviceThreadGroup = new ThreadGroup(
//				"NF-Service-ThreadGroup");
//		for (String serviceType : this.services.keySet()) {
//			new Thread(serviceThreadGroup, this.services.get(serviceType),
//					serviceType).start();
//		}
//		
		
		ExecutorService completionService = null;
		for (String serviceType : this.services.keySet()) {
			 completionService = getCompletionService();
			 Callable<String> task =this.services.get(serviceType);

			 Future<String> future = completionService.submit(task);
			
				
				Object aa = future.get();
				if(aa instanceof MulticastDatagramServiceImpl) {
					MulticastDatagramServiceImpl ss= (MulticastDatagramServiceImpl) aa;
					System.out.println("---------------------->1---------------------->"+ss.getClass().getName());
				//	ss.stop();
				}else if(aa instanceof AbstractRMIServant){
					AbstractRMIServant rs=( AbstractRMIServant)aa;
					
					System.out.println("---------------------->2---------------------->"+rs.sayHello());
				}
					
				
	}
		
		
		
		

	}

	public ExecutorService getCompletionService() throws InterruptedException {
		ThreadFactory threadFactory = new ServiceThreadFactory();
		ExecutorService executor = Executors
				.newSingleThreadExecutor(threadFactory);
/*		CompletionService<String> completionService = new ExecutorCompletionService<String>(
				executor);*/
		
		return executor;
	}

	
	public void shutdown(ENUM_SERVICE_TYPE serviceType) {
		// TODO Auto-generated method stub

	}

	
	public void shutdownAllService() {
		for (String serviceType : this.services.keySet()) {
	//		new Thread(this.services.get(serviceType), serviceType).start();
		}
	}

	private void init() throws NFlightException, IOException {
		Callable serviceRunnable = null;
		for (ENUM_SERVICE_TYPE serviceType : ENUM_SERVICE_TYPE.values()) {
			serviceRunnable = ServiceFactory
					.createtServiceTask(serviceType);
			this.services.put(serviceType.toString(), serviceRunnable);
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