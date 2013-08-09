package com.abreqadhabra.nflight.application.service.net;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.application.launcher.concurrent.executor.ThreadPoolExecutorServiceImpl;
import com.abreqadhabra.nflight.application.launcher.concurrent.executor.monitor.ThreadPoolMonitorServiceImpl;
import com.abreqadhabra.nflight.application.service.net.datagram.multicast.MulticastNetworkServiceImpl;
import com.abreqadhabra.nflight.application.service.net.datagram.unicast.UnicastNetworkServiceImpl;
import com.abreqadhabra.nflight.application.service.net.stream.asynchronous.AsyncNetworkServiceImpl;
import com.abreqadhabra.nflight.application.service.net.stream.blocking.BlockingNetworkServiceImpl;
import com.abreqadhabra.nflight.application.service.net.stream.nonblocking.NonBlockingNetworkServiceImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class NetworkServiceTest {
	private static final Class<NetworkServiceTest> THIS_CLAZZ = NetworkServiceTest.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	static boolean isThreadPoolMonitoring = false;
	static int delaySeconds = 10;
	private static BlockingNetworkServiceImpl blockingService;
	private static NonBlockingNetworkServiceImpl nonBlockingService;
	private static AsyncNetworkServiceImpl asyncService;
	private static UnicastNetworkServiceImpl unicastService;
	private static MulticastNetworkServiceImpl multicastService;

	public static void main(String[] args) throws Exception {

		InetAddress DEFAULT_ADDRESS = InetAddress.getLocalHost();

		Configure configure = new ConfigureImpl(
				Configure.FILE_SOCKET_SERVER_PROPERTIES);

		blockingService = executeBlockingNetworkService(configure,
				DEFAULT_ADDRESS, 0);
		nonBlockingService = executeNonBlockingNetworkService(configure,
				DEFAULT_ADDRESS, 0);
		asyncService = executeAsyncNetworkService(configure, DEFAULT_ADDRESS, 0);

		unicastService = executeUnicastNetworkService(configure, DEFAULT_ADDRESS, 0);
		
		multicastService = executeMulticastNetworkService(configure, DEFAULT_ADDRESS, 0);

		
		executeAll();

		// nonBlockingServer.start();

		// testDatagramAcceptor(DEFAULT_ADDRESS, DEFAULT_PORT);

		// System.exit(0);

	}


	private static MulticastNetworkServiceImpl executeMulticastNetworkService(
			Configure configure, InetAddress addr, int port) {
		if (port == 0) {
			port = configure.getInt(configure.MULTICAST_DEFAULT_PORT);
		}

		InetSocketAddress endpoint = new InetSocketAddress(addr, port);

		return new MulticastNetworkServiceImpl(configure, null, endpoint);
	}

	private static UnicastNetworkServiceImpl executeUnicastNetworkService(
			Configure configure, InetAddress addr, int port) {

		if (port == 0) {
			port = configure.getInt(configure.UNICAST_DEFAULT_PORT);
		}

		InetSocketAddress endpoint = new InetSocketAddress(addr, port);

		return new UnicastNetworkServiceImpl(configure,
				null, endpoint);

	}
	
	private static AsyncNetworkServiceImpl executeAsyncNetworkService(
			Configure configure, InetAddress addr, int port) {

		if (port == 0) {
			port = configure.getInt(configure.ASYNC_DEFAULT_PORT);
		}

		InetSocketAddress endpoint = new InetSocketAddress(addr, port);
		ThreadPoolExecutor asyncNetworkThreadPool = getThreadPoolExecutor(
				"NF-Service-ThreadPool-Async", delaySeconds,
				isThreadPoolMonitoring);

		return new AsyncNetworkServiceImpl(configure, asyncNetworkThreadPool,
				endpoint);

	}

	private static BlockingNetworkServiceImpl executeBlockingNetworkService(
			Configure configure, InetAddress addr, int port) {

		if (port == 0) {
			port = configure.getInt(configure.BLOCKING_DEFAULT_PORT);
		}

		InetSocketAddress endpoint = new InetSocketAddress(addr, port);

		ThreadPoolExecutor blockingNetworkThreadPool = getThreadPoolExecutor(
				"NF-Service-ThreadPool-Blocking", delaySeconds,
				isThreadPoolMonitoring);

		return new BlockingNetworkServiceImpl(configure,
				blockingNetworkThreadPool, endpoint);

	}

	private static NonBlockingNetworkServiceImpl executeNonBlockingNetworkService(
			Configure configure, InetAddress addr, int port) {

		if (port == 0) {
			port = configure.getInt(configure.NONBLOCKING_DEFAULT_PORT);
		}

		InetSocketAddress endpoint = new InetSocketAddress(addr, port);

		return new NonBlockingNetworkServiceImpl(configure, null, endpoint);

	}

	private static void executeAll() {
		ThreadGroup serviceThreadGroup = new ThreadGroup(
				"NF-Service-ThreadGroup");

		// Executors.n.newSingleThreadExecutor().execute(

		new Thread(serviceThreadGroup, blockingService, "NF-Service-Blocking")
				.start();

		new Thread(serviceThreadGroup, nonBlockingService,
				"NF-Service-Non-Blocking").start();

		new Thread(serviceThreadGroup, asyncService, "NF-Service-Async")
				.start();
		
		new Thread(serviceThreadGroup, unicastService, "NF-Service-Unicast")
		.start();
		
		new Thread(serviceThreadGroup, multicastService, "NF-Service-Multicast")
		.start();
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