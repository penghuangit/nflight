package com.abreqadhabra.nflight.application.service.net.stream;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.application.launcher.concurrent.executor.ThreadPoolExecutorServiceImpl;
import com.abreqadhabra.nflight.application.launcher.concurrent.executor.monitor.ThreadPoolMonitorServiceImpl;
import com.abreqadhabra.nflight.application.service.net.stream.blocking.BlockingNetworkServiceImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class BlockingNetworkServiceTest {
	private static final Class<BlockingNetworkServiceTest> THIS_CLAZZ = BlockingNetworkServiceTest.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static void main(String[] args) throws Exception {

		InetAddress DEFAULT_ADDRESS = InetAddress.getLocalHost();
		int DEFAULT_PORT = 9999;
		InetSocketAddress socketAddress = new InetSocketAddress(
				DEFAULT_ADDRESS, DEFAULT_PORT);

		Configure configure = new ConfigureImpl(
				Configure.FILE_SOCKET_SERVER_PROPERTIES);

		ThreadPoolExecutor blockingNetworkThreadPool = getThreadPoolExecutor();
		
		BlockingNetworkServiceImpl server = new BlockingNetworkServiceImpl(
				configure, socketAddress, blockingNetworkThreadPool);
		server.start();

		// testDatagramAcceptor(DEFAULT_ADDRESS, DEFAULT_PORT);

		// System.exit(0);

	}

	 static ThreadPoolExecutor getThreadPoolExecutor() {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutorServiceImpl()
				.createNewThreadPool();
		threadPoolExecutor.allowCoreThreadTimeOut(true);

		boolean isThreadPoolMonitoring = true;
		int delaySeconds = 5;
		if (isThreadPoolMonitoring) {
			// Created executor is set to ThreadPoolMonitorService...
			ThreadPoolMonitorServiceImpl tpms = new ThreadPoolMonitorServiceImpl(
					delaySeconds);
			tpms.setExecutor(threadPoolExecutor);

			// ThreadPoolMonitorService is started...
			Thread monitor = new Thread(tpms);
			monitor.start();
		}
		return threadPoolExecutor;
	}
}