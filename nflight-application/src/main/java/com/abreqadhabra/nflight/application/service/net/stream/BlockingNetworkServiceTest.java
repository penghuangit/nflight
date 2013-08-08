package com.abreqadhabra.nflight.application.service.net.stream;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.application.launcher.concurrent.executor.ThreadPoolExecutorServiceImpl;
import com.abreqadhabra.nflight.application.launcher.concurrent.executor.monitor.ThreadPoolMonitorServiceImpl;
import com.abreqadhabra.nflight.application.service.net.stream.asynchronous.AsyncNetworkServiceImpl;
import com.abreqadhabra.nflight.application.service.net.stream.blocking.BlockingNetworkServiceImpl;
import com.abreqadhabra.nflight.application.service.net.stream.nonblocking.NonBlockingNetworkServiceImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class BlockingNetworkServiceTest {
	private static final Class<BlockingNetworkServiceTest> THIS_CLAZZ = BlockingNetworkServiceTest.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static void main(String[] args) throws Exception {

		InetAddress DEFAULT_ADDRESS = InetAddress.getLocalHost();
		int BLOCKING_DEFAULT_PORT = 8888;
		int NON_BLOCKING_DEFAULT_PORT = 9999;
		int ASYNC_DEFAULT_PORT=7777;

		InetSocketAddress socketAddress = new InetSocketAddress(
				DEFAULT_ADDRESS, BLOCKING_DEFAULT_PORT);

		Configure configure = new ConfigureImpl(
				Configure.FILE_SOCKET_SERVER_PROPERTIES);

		ThreadPoolExecutor blockingNetworkThreadPool = getThreadPoolExecutor(
				"NF-Service-ThreadPool-Blocking", 3, false);

		BlockingNetworkServiceImpl blockingService = new BlockingNetworkServiceImpl(
				configure, socketAddress, blockingNetworkThreadPool);

		socketAddress = new InetSocketAddress(DEFAULT_ADDRESS,
				NON_BLOCKING_DEFAULT_PORT);

		NonBlockingNetworkServiceImpl nonBlockingService = new NonBlockingNetworkServiceImpl(
				configure, socketAddress, blockingNetworkThreadPool);
		
		
		socketAddress = new InetSocketAddress(DEFAULT_ADDRESS,
				ASYNC_DEFAULT_PORT);

		
		ThreadPoolExecutor asyncNetworkThreadPool = getThreadPoolExecutor(
				"NF-Service-ThreadPool-Async", 3, false);
		
		AsyncNetworkServiceImpl asyncService = new AsyncNetworkServiceImpl(
				configure, socketAddress, asyncNetworkThreadPool);

		ThreadGroup serviceThreadGroup = new ThreadGroup(
				"NF-Service-ThreadGroup");

		new Thread(serviceThreadGroup, blockingService,
				"NF-Service-Blocking").start();

		new Thread(serviceThreadGroup, nonBlockingService,
				"NF-Service-Non-Blocking").start();
		
		new Thread(serviceThreadGroup, asyncService,
				"NF-Service-Async").start();

		// nonBlockingServer.start();

		// testDatagramAcceptor(DEFAULT_ADDRESS, DEFAULT_PORT);

		// System.exit(0);
		

	}



	static ThreadPoolExecutor getThreadPoolExecutor(String poolName,
			int delaySeconds, boolean isThreadPoolMonitoring) {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutorServiceImpl()
				.createNewThreadPool();
		threadPoolExecutor.allowCoreThreadTimeOut(true);

		if (isThreadPoolMonitoring) {
			
			Thread t = Thread.currentThread();
			ThreadGroup threadGroup = t.getThreadGroup();
			
			// Created executor is set to ThreadPoolMonitorService...
			ThreadPoolMonitorServiceImpl tpms = new ThreadPoolMonitorServiceImpl(
					delaySeconds, threadGroup, poolName);
			tpms.setExecutor(threadPoolExecutor);

			// ThreadPoolMonitorService is started...
			Thread monitor = new Thread(tpms);
			monitor.start();
		}
		return threadPoolExecutor;
	}
}