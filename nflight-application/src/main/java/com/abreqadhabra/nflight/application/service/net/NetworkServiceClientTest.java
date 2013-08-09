package com.abreqadhabra.nflight.application.service.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardProtocolFamily;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class NetworkServiceClientTest {
	private static final Class<NetworkServiceClientTest> THIS_CLAZZ = NetworkServiceClientTest.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	private static Runnable blockingServiceClient;
	private static Runnable nonBlockingServiceClient;
	private static Runnable asyncServiceClient;
	private static Runnable unicastServiceClient;
	private static Runnable multicastServiceClient;

	static int cnt = 5000;
	static int millis = 5;

	public static void main(final String[] args) throws UnknownHostException {

		InetAddress DEFAULT_ADDRESS = InetAddress.getLocalHost();

		Configure configure = new ConfigureImpl(
				Configure.FILE_SOCKET_SERVER_PROPERTIES);

		blockingServiceClient = executeBlockingNetworkServiceClient(configure,
				DEFAULT_ADDRESS, 0);
		nonBlockingServiceClient = executeNonBlockingNetworkServiceClient(
				configure, DEFAULT_ADDRESS, 0);
		asyncServiceClient = executeAsyncNetworkServiceClient(configure,
				DEFAULT_ADDRESS, 0);

		unicastServiceClient = executeUnicastNetworkServiceClient(configure,
				DEFAULT_ADDRESS, 0);

		multicastServiceClient = executeMulticastNetworkServiceClient(
				configure, DEFAULT_ADDRESS, 0);

		executeAll();

	}

	private static void executeAll() {
		ThreadGroup clientThreadGroup = new ThreadGroup(
				"NF-Service-Client-ThreadGroup");

		new Thread(clientThreadGroup, blockingServiceClient,
				"NF-Service-Client-Blocking").start();

		new Thread(clientThreadGroup, nonBlockingServiceClient,
				"NF-Service-Client-Non-Blocking").start();

		new Thread(clientThreadGroup, asyncServiceClient,
				"NF-Service-Client-Async").start();

		new Thread(clientThreadGroup, unicastServiceClient,
				"NF-Service-Client-Unicast").start();

		new Thread(clientThreadGroup, multicastServiceClient,
				"NF-Service-Client-Multicast").start();
	}

	private static Runnable executeMulticastNetworkServiceClient(
			Configure configure, InetAddress addr, int port) {
		if (port == 0) {
			port = configure.getInt(configure.MULTICAST_DEFAULT_PORT);
		}

		InetSocketAddress endpoint = new InetSocketAddress(addr, port);
		return multicastTypeTest(StandardProtocolFamily.INET, endpoint, cnt,
				millis);
	}


	private static Runnable executeUnicastNetworkServiceClient(
			Configure configure, InetAddress addr, int port) {
		if (port == 0) {
			port = configure.getInt(configure.UNICAST_DEFAULT_PORT);
		}

		InetSocketAddress endpoint = new InetSocketAddress(addr, port);
		return unicastTypeTest(StandardProtocolFamily.INET, endpoint, cnt,
				millis);
	}

	private static Runnable executeAsyncNetworkServiceClient(
			Configure configure, InetAddress addr, int port) {

		if (port == 0) {
			port = configure.getInt(configure.ASYNC_DEFAULT_PORT);
		}

		// port = NetworkServiceHelper.validatedStreamPort(port);

		InetSocketAddress endpoint = new InetSocketAddress(addr, port);

		return asyncTypeTest(endpoint, cnt, millis);
	}

	private static Runnable executeNonBlockingNetworkServiceClient(
			Configure configure, InetAddress addr, int port) {
		if (port == 0) {
			port = configure.getInt(configure.NONBLOCKING_DEFAULT_PORT);
		}

		InetSocketAddress endpoint = new InetSocketAddress(addr, port);
		return blockingTypeTest(endpoint, cnt, millis);
	}

	private static Runnable executeBlockingNetworkServiceClient(
			Configure configure, InetAddress addr, int port) {
		if (port == 0) {
			port = configure.getInt(configure.BLOCKING_DEFAULT_PORT);
		}

		InetSocketAddress endpoint = new InetSocketAddress(addr, port);
		return blockingTypeTest(endpoint, cnt, millis);
	}

	private static Runnable asyncTypeTest(InetSocketAddress endpoint, int num,
			int millis) {
		return new Runnable() {

			InetSocketAddress endpoint;
			int num;
			private int millis;

			public Runnable init(InetSocketAddress endpoint, int num, int millis) {
				this.endpoint = endpoint;
				this.num = num;
				this.millis = millis;
				return (this);
			}

			@Override
			public void run() {
				final String METHOD_NAME = Thread.currentThread()
						.getStackTrace()[1].getMethodName();

				LOGGER.logp(
						Level.FINER,
						THIS_CLAZZ.getSimpleName(),
						METHOD_NAME,
						"current thread is "
								+ LoggingHelper.getThreadName(Thread
										.currentThread()));

				for (int i = 0; i < num; i++) {
					try {
						AsynchronousSocketChannel channel = AsynchronousSocketChannel
								.open();

						LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
								METHOD_NAME, endpoint.toString());

						channel.connect(endpoint).get();
						channel.write(
								ByteBuffer
										.wrap(("Hello World! ---------------------> " + endpoint
												.toString()).getBytes())).get();
						Thread.sleep(millis);
						System.out.println(i + 1 + ":" + channel);
						channel.close();
					} catch (IOException | InterruptedException
							| ExecutionException e) {
						e.printStackTrace();
					}
				}
			}

		}.init(endpoint, num, millis);
	}

	private static Runnable blockingTypeTest(InetSocketAddress endpoint,
			int num, int millis) {
		return new Runnable() {

			InetSocketAddress endpoint;
			int num;
			int millis;

			public Runnable init(InetSocketAddress endpoint, int num, int millis) {
				this.endpoint = endpoint;
				this.num = num;
				this.millis = millis;
				return (this);
			}

			@Override
			public void run() {
				final String METHOD_NAME = Thread.currentThread()
						.getStackTrace()[1].getMethodName();

				LOGGER.logp(
						Level.FINER,
						THIS_CLAZZ.getSimpleName(),
						METHOD_NAME,
						"current thread is "
								+ LoggingHelper.getThreadName(Thread
										.currentThread()));

				for (int i = 0; i < num; i++) {
					try {
						SocketChannel channel = SocketChannel.open();

						LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
								METHOD_NAME, endpoint.toString());

						channel.connect(endpoint);
						channel.write(ByteBuffer
								.wrap(("Hello World! ---------------------> " + endpoint
										.toString()).getBytes()));
						Thread.sleep(millis);
						System.out.println(i + 1 + ":" + channel);
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}.init(endpoint, num, millis);
	}

	private static Runnable unicastTypeTest(StandardProtocolFamily family,
			InetSocketAddress endpoint, int num, int millis) {
		return new Runnable() {

			StandardProtocolFamily family;
			InetSocketAddress endpoint;
			int num;
			int millis;

			public Runnable init(StandardProtocolFamily family,
					InetSocketAddress endpoint, int num, int millis) {
				this.family = family;
				this.endpoint = endpoint;
				this.num = num;
				this.millis = millis;
				return (this);
			}

			@Override
			public void run() {
				final String METHOD_NAME = Thread.currentThread()
						.getStackTrace()[1].getMethodName();

				LOGGER.logp(
						Level.FINER,
						THIS_CLAZZ.getSimpleName(),
						METHOD_NAME,
						"current thread is "
								+ LoggingHelper.getThreadName(Thread
										.currentThread()));

				for (int i = 0; i < num; i++) {
					try {
						DatagramChannel channel = DatagramChannel.open(family);

						LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
								METHOD_NAME, endpoint.toString());

						channel.connect(endpoint);
						channel.write(ByteBuffer
								.wrap(("Hello World! ---------------------> " + endpoint
										.toString()).getBytes()));
						Thread.sleep(millis);
						System.out.println(i + 1 + ":" + channel);
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}.init(family, endpoint, num, millis);

	}
	
	
	private static Runnable multicastTypeTest(StandardProtocolFamily family,
			InetSocketAddress endpoint, int num, int millis) {
		return new Runnable() {

			StandardProtocolFamily family;
			InetSocketAddress endpoint;
			int num;
			int millis;

			public Runnable init(StandardProtocolFamily family,
					InetSocketAddress endpoint, int num, int millis) {
				this.family = family;
				this.endpoint = endpoint;
				this.num = num;
				this.millis = millis;
				return (this);
			}

			@Override
			public void run() {
				final String METHOD_NAME = Thread.currentThread()
						.getStackTrace()[1].getMethodName();

				
				
				LOGGER.logp(
						Level.FINER,
						THIS_CLAZZ.getSimpleName(),
						METHOD_NAME,
						"current thread is "
								+ LoggingHelper.getThreadName(Thread
										.currentThread()));

				for (int i = 0; i < num; i++) {
					try {
						
						InetAddress group = InetAddress.getByName(Configure.MULTICAST_GROUP_ADDRESS);
						
						final String networkInterfaceName = NetworkServiceHelper
								.getNetworkInterfaceName(InetAddress.getLocalHost()
										.getHostAddress());

						// get the network interface used for multicast
						final NetworkInterface networkInterface = NetworkInterface
								.getByName(networkInterfaceName);

						
						DatagramChannel channel = DatagramChannel.open(family);

						LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
								METHOD_NAME, endpoint.toString());

						channel.bind(endpoint);
						
						MembershipKey key = channel.join(group, networkInterface);

						
						channel.write(ByteBuffer
								.wrap(("Hello World! ---------------------> " + endpoint
										.toString()).getBytes()));
						Thread.sleep(millis);
						System.out.println(i + 1 + ":" + channel);
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}.init(family, endpoint, num, millis);

	}

}