package com.abreqadhabra.nflight.application.service.client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationSystem;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
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
import com.abreqadhabra.nflight.application.service.rmi.activatable.ActivatableRMIServantImpl;
import com.abreqadhabra.nflight.application.service.rmi.activatable.RMIDCommand;
import com.abreqadhabra.nflight.application.service.rmi.unicast.UnicastRMIServantImpl;
import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ServiceServerTest {
	private static final Class<ServiceServerTest> THIS_CLAZZ = ServiceServerTest.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	static boolean isThreadPoolMonitoring = false;
	static int delaySeconds = 10;
	private static BlockingNetworkServiceImpl blockingService;
	private static NonBlockingNetworkServiceImpl nonBlockingService;
	private static AsyncNetworkServiceImpl asyncService;
	private static UnicastNetworkServiceImpl unicastService;
	private static MulticastNetworkServiceImpl multicastService;
	private static UnicastRMIServantImpl unicastServant;
	private static ActivatableRMIServantImpl activatableServant;

	public static void main(final String[] args) throws Exception {

		final InetAddress DEFAULT_ADDRESS = InetAddress.getLocalHost();

		final Configure netConfigure = new ConfigureImpl(
				Configure.FILE_NETWORK_SERVICE_PROPERTIES);

		final Configure rmiConfigure = new ConfigureImpl(
				Configure.FILE_RMI_SERVICE_PROPERTIES);

		rmidProcessStart(rmiConfigure);

		blockingService = executeBlockingNetworkService(netConfigure,
				DEFAULT_ADDRESS, 0);
		nonBlockingService = executeNonBlockingNetworkService(netConfigure,
				DEFAULT_ADDRESS, 0);
		asyncService = executeAsyncNetworkService(netConfigure,
				DEFAULT_ADDRESS, 0);

		unicastService = executeUnicastNetworkService(netConfigure,
				DEFAULT_ADDRESS, 0);

		multicastService = executeMulticastNetworkService(netConfigure,
				DEFAULT_ADDRESS, 0);

		unicastServant = executeUnicastServant(rmiConfigure, DEFAULT_ADDRESS, 0);

		activatableServant = executeActivatableServant(rmiConfigure,
				DEFAULT_ADDRESS, 0);

		executeAll();

		// nonBlockingServer.start();

		// testDatagramAcceptor(DEFAULT_ADDRESS, DEFAULT_PORT);

		// System.exit(0);

	}

	private static void rmidProcessStart(Configure configure)
			throws InterruptedException {

		String commmad = null;

		try {
			// 액티베이션 시스템이 기동되어 있을 경우 RMID를 기동을 스킵
			ActivationSystem activationSystem = ActivationGroup.getSystem();

			System.out.println("액티베이션 시스템이 기동되어 있을 경우 RMID를 기동을 스킵 "
					+ activationSystem);

		} catch (ActivationException ae) {
			// Port already in use: 1098 Address already in use: JVM_Bind
			// 액티베이션 시스템이 기동되어 있지 않을 경우 RMID 시작
			commmad = getRMIDStartSystemCommand(configure);

			new Thread(new RMIDCommand(commmad)).start();

			// 콜러블로 기동결과에 대한 값이 있을때까지 대기

			Thread.sleep(1000);

			System.out.println("액티베이션 시스템이 기동되어 있지 않을 경우 RMID 시작 ");

		}

	}

	private static String getRMIDStartSystemCommand(Configure configure) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		String command = configure
				.get(Configure.ACTIVATABLE_RMI_SYSTEM_COMMAND_RMID_START);

		command = command + " -J-D"
				+ Env.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString() + "="
				+ Configure.FILE_RMID_POLICY + " -log "
				+ Configure.CODE_BASE_PATH.resolve("rmid.log");

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"system command :" + command);

		return command;
	}

	private static String getRMIDStopSystemCommand(Configure configure) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		String command = configure
				.get(Configure.ACTIVATABLE_RMI_SYSTEM_COMMAND_RMID_STOP);

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"system command :" + command);

		return command;
	}

	private static UnicastRMIServantImpl executeUnicastServant(
			final Configure configure, final InetAddress addr, int port)
			throws Exception {
		if (port == 0) {
			port = configure.getInt(Configure.UNICAST_RMI_DEFAULT_PORT);
		}
		return new UnicastRMIServantImpl(configure, addr, port);
	}

	private static void executeAll() {
		final ThreadGroup serviceThreadGroup = new ThreadGroup(
				"NF-Service-ThreadGroup");

		// Executors.n.newSingleThreadExecutor().execute(

		new Thread(serviceThreadGroup, blockingService, "NF-Channel-Blocking")
				.start();

		new Thread(serviceThreadGroup, nonBlockingService,
				"NF-Channel-Non-Blocking").start();

		new Thread(serviceThreadGroup, asyncService, "NF-Channel-Async")
				.start();

		new Thread(serviceThreadGroup, unicastService, "NF-Channel-Unicast")
				.start();

		new Thread(serviceThreadGroup, multicastService, "NF-Channel-Multicast")
				.start();

		new Thread(serviceThreadGroup, unicastServant, "NF-RMI-Unicast")
				.start();

		new Thread(serviceThreadGroup, activatableServant, "NF-RMI-Activatable")
				.start();
	}

	private static ActivatableRMIServantImpl executeActivatableServant(
			final Configure configure, final InetAddress addr, int port)
			throws Exception {
		if (port == 0) {
			port = configure.getInt(Configure.ACTIVATABLE_RMI_DEFAULT_PORT);
		}

		return new ActivatableRMIServantImpl(configure, addr, port);
	}

	private static MulticastNetworkServiceImpl executeMulticastNetworkService(
			final Configure configure, final InetAddress addr, int port) {
		if (port == 0) {
			port = configure.getInt(Configure.MULTICAST_DEFAULT_PORT);
		}

		final InetSocketAddress endpoint = new InetSocketAddress(addr, port);

		return new MulticastNetworkServiceImpl(configure, null, endpoint);
	}

	private static UnicastNetworkServiceImpl executeUnicastNetworkService(
			final Configure configure, final InetAddress addr, int port) {

		if (port == 0) {
			port = configure.getInt(Configure.UNICAST_DEFAULT_PORT);
		}

		final InetSocketAddress endpoint = new InetSocketAddress(addr, port);

		return new UnicastNetworkServiceImpl(configure, null, endpoint);

	}

	private static AsyncNetworkServiceImpl executeAsyncNetworkService(
			final Configure configure, final InetAddress addr, int port) {

		if (port == 0) {
			port = configure.getInt(Configure.ASYNC_DEFAULT_PORT);
		}

		final InetSocketAddress endpoint = new InetSocketAddress(addr, port);
		final ThreadPoolExecutor asyncNetworkThreadPool = getThreadPoolExecutor(
				"NF-Service-ThreadPool-Async", delaySeconds,
				isThreadPoolMonitoring);

		return new AsyncNetworkServiceImpl(configure, asyncNetworkThreadPool,
				endpoint);

	}

	private static BlockingNetworkServiceImpl executeBlockingNetworkService(
			final Configure configure, final InetAddress addr, int port) {

		if (port == 0) {
			port = configure.getInt(Configure.BLOCKING_DEFAULT_PORT);
		}

		final InetSocketAddress endpoint = new InetSocketAddress(addr, port);

		final ThreadPoolExecutor blockingNetworkThreadPool = getThreadPoolExecutor(
				"NF-Service-ThreadPool-Blocking", delaySeconds,
				isThreadPoolMonitoring);

		return new BlockingNetworkServiceImpl(configure,
				blockingNetworkThreadPool, endpoint);

	}

	private static NonBlockingNetworkServiceImpl executeNonBlockingNetworkService(
			final Configure configure, final InetAddress addr, int port) {

		if (port == 0) {
			port = configure.getInt(Configure.NONBLOCKING_DEFAULT_PORT);
		}

		final InetSocketAddress endpoint = new InetSocketAddress(addr, port);

		return new NonBlockingNetworkServiceImpl(configure, null, endpoint);

	}

	static ThreadPoolExecutor getThreadPoolExecutor(final String poolName,
			final int delaySeconds, final boolean isThreadPoolMonitoring) {
		final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutorServiceImpl()
				.createNewThreadPool();
		threadPoolExecutor.allowCoreThreadTimeOut(true);

		if (isThreadPoolMonitoring) {

			final Thread t = Thread.currentThread();
			final ThreadGroup threadGroup = t.getThreadGroup();

			// Created executor is set to ThreadPoolMonitorService...
			final ThreadPoolMonitorServiceImpl tpms = new ThreadPoolMonitorServiceImpl(
					delaySeconds, threadGroup, poolName);
			tpms.setExecutor(threadPoolExecutor);

			// ThreadPoolMonitorService is started...
			final Thread monitor = new Thread(tpms);
			monitor.start();
		}
		return threadPoolExecutor;
	}
}