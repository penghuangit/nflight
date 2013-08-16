package com.abreqadhabra.nflight.application.service.temp.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationSystem;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.service.net.datagram.multicast.MulticastNetworkServiceImpl;
import com.abreqadhabra.nflight.application.service.net.datagram.unicast.UnicastNetworkServiceImpl;
import com.abreqadhabra.nflight.application.service.net.stream.asynchronous.AsyncNetworkServiceImpl;
import com.abreqadhabra.nflight.application.transport.rmi.activation.ActivatableRMIServantImpl;
import com.abreqadhabra.nflight.application.transport.rmi.rmid.RMIDCommand;
import com.abreqadhabra.nflight.application.transport.rmi.unicast.UnicastRMIServantImpl;
import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.common.concurrency.thread.ThreadHelper;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.launcher.Configure;
import com.abreqadhabra.nflight.common.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.common.launcher.concurrent.executor.ThreadPoolExecutorServiceImpl;
import com.abreqadhabra.nflight.common.launcher.concurrent.executor.monitor.ThreadPoolMonitorServiceImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ServiceServerTest {
	private static Class<ServiceServerTest> THIS_CLAZZ = ServiceServerTest.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	static boolean isThreadPoolMonitoring = false;
	static int delaySeconds = 10;
	private static BlockingNetworkServiceImpl blockingService;
	private static NonBlockingNetworkServiceImpl nonBlockingService;
	private static AsyncNetworkServiceImpl asyncService;
	private static UnicastNetworkServiceImpl unicastService;
	private static MulticastNetworkServiceImpl multicastService;
	private static UnicastRMIServantImpl unicastServant;
	private static ActivatableRMIServantImpl activatableServant;

	public static void main(String[] args) {

		try {
			InetAddress DEFAULT_ADDRESS = InetAddress.getLocalHost();

			Configure netConfigure = new ConfigureImpl(
					Configure.FILE_NETWORK_SERVICE_PROPERTIES);

			Configure rmiConfigure = new ConfigureImpl(
					Configure.FILE_RMI_SERVICE_PROPERTIES);

			rmidProcessStart(rmiConfigure);

			blockingService = executeBlockingNetworkService(netConfigure,
					DEFAULT_ADDRESS, 0);

			blockingService.startup();

			nonBlockingService = executeNonBlockingNetworkService(netConfigure,
					DEFAULT_ADDRESS, 0);

			nonBlockingService.startup();

			asyncService = executeAsyncNetworkService(netConfigure,
					DEFAULT_ADDRESS, 0);

			asyncService.startup();

			unicastService = executeUnicastNetworkService(netConfigure,
					DEFAULT_ADDRESS, 0);

			unicastService.startup();

			multicastService = executeMulticastNetworkService(netConfigure,
					DEFAULT_ADDRESS, 0);

			multicastService.startup();

			unicastServant = executeUnicastServant(rmiConfigure,
					DEFAULT_ADDRESS, 0);

			activatableServant = executeActivatableServant(rmiConfigure,
					DEFAULT_ADDRESS, 0);

			executeAll();

			// nonBlockingServer.start();

			// testDatagramAcceptor(DEFAULT_ADDRESS, DEFAULT_PORT);

			// System.exit(0);
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
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		String command = configure
				.get(Configure.ACTIVATABLE_RMI_SYSTEM_COMMAND_RMID_START);

		command = command + " -J-D"
				+ Env.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString() + "="
				+ Configure.FILE_RMID_POLICY + " -log rmid.log";
		// + Configure.CODE_BASE_PATH.resolve("rmid.log");

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"system command :" + command);

		return command;
	}

	private static UnicastRMIServantImpl executeUnicastServant(
			Configure configure, InetAddress addr, int port) throws Exception {
		if (port == 0) {
			port = configure.getInt(Configure.RMI_DEFAULT_PORT);
		}
		return new UnicastRMIServantImpl(configure, addr, port);
	}

	private static void executeAll() {
		ThreadGroup serviceThreadGroup = new ThreadGroup("NF-RMI-ThreadGroup");

		// Executors.n.newSingleThreadExecutor().execute(

		// new Thread(serviceThreadGroup, blockingService,
		// "NF-Channel-Blocking")
		// .start();
		//
		// new Thread(serviceThreadGroup, nonBlockingService,
		// "NF-Channel-Non-Blocking").start();
		//
		// new Thread(serviceThreadGroup, asyncService, "NF-Channel-Async")
		// .start();
		//
		// new Thread(serviceThreadGroup, unicastService, "NF-Channel-Unicast")
		// .start();
		//
		// new Thread(serviceThreadGroup, multicastService,
		// "NF-Channel-Multicast")
		// .start();
		//
		new Thread(serviceThreadGroup, unicastServant, "NF-RMI-Unicast")
				.start();

		new Thread(serviceThreadGroup, activatableServant, "NF-RMI-Activatable")
				.start();
	}

	private static ActivatableRMIServantImpl executeActivatableServant(
			Configure configure, InetAddress addr, int port) throws Exception {
		if (port == 0) {
			port = configure.getInt(Configure.ACTIVATABLE_RMI_DEFAULT_PORT);
		}

		return new ActivatableRMIServantImpl(configure, addr, port);
	}

	private static MulticastNetworkServiceImpl executeMulticastNetworkService(
			Configure configure, InetAddress addr, int port) {
		if (port == 0) {
			port = configure.getInt(Configure.MULTICAST_DEFAULT_PORT);
		}

		InetSocketAddress endpoint = new InetSocketAddress(addr, port);

		return new MulticastNetworkServiceImpl(configure, null, endpoint);
	}

	private static UnicastNetworkServiceImpl executeUnicastNetworkService(
			Configure configure, InetAddress addr, int port) {

		if (port == 0) {
			port = configure.getInt(Configure.UNICAST_DEFAULT_PORT);
		}

		InetSocketAddress endpoint = new InetSocketAddress(addr, port);

		return new UnicastNetworkServiceImpl(configure, null, endpoint);

	}

	private static AsyncNetworkServiceImpl executeAsyncNetworkService(
			Configure configure, InetAddress addr, int port) {

		if (port == 0) {
			port = configure.getInt(Configure.ASYNC_DEFAULT_PORT);
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
			port = configure.getInt(Configure.BLOCKING_DEFAULT_PORT);
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
			port = configure.getInt(Configure.NONBLOCKING_DEFAULT_PORT);
		}

		InetSocketAddress endpoint = new InetSocketAddress(addr, port);

		return new NonBlockingNetworkServiceImpl(configure, null, endpoint);

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