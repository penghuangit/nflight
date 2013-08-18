package com.abreqadhabra.nflight.application.container.network.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.NetworkChannel;
import java.nio.channels.SocketChannel;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.concurrent.thread.ThreadHelper;
import com.abreqadhabra.nflight.application.common.launcher.Configure;
import com.abreqadhabra.nflight.application.common.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.application.service.network.rmi.RMIServant;
import com.abreqadhabra.nflight.application.service.network.rmi.RMIServantHelper;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ServiceClientTest {
	private static Class<ServiceClientTest> THIS_CLAZZ = ServiceClientTest.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	static HashMap<String, Runnable> serviceGroupMap = new HashMap<String, Runnable>();
	static int cnt = 1;
	static int millis = 0;

	public static SocketFactory createSocketChannelFactory() {
		return new SocketFactory();
	}

	public static void main(String[] args) {
		try {
			InetAddress DEFAULT_ADDRESS = InetAddress.getLocalHost();

			Configure configure = new ConfigureImpl(THIS_CLAZZ,
					Configure.FILE_SERVICE_PROPERTIES);

			for (int i = 0; i < cnt; i++) {

				serviceGroupMap.put(
						Configure.SERVICE_TYPE.network_blocking.toString(),
						getBlockingNetworkService(DEFAULT_ADDRESS, configure
								.getInt(Configure.BLOCKING_DEFAULT_PORT),
								Configure.SERVICE_TYPE.network_blocking));
				serviceGroupMap.put(
						Configure.SERVICE_TYPE.network_nonblocking.toString(),
						getBlockingNetworkService(DEFAULT_ADDRESS, configure
								.getInt(Configure.NONBLOCKING_DEFAULT_PORT),
								Configure.SERVICE_TYPE.network_nonblocking));

				serviceGroupMap
						.put(Configure.SERVICE_TYPE.network_async.toString(),
								getAsyncNetworkService(
										DEFAULT_ADDRESS,
										configure
												.getInt(Configure.ASYNC_DEFAULT_PORT)));

				serviceGroupMap.put(
						Configure.SERVICE_TYPE.network_unicast.toString()
								+ "--------->socket",
						getUnicastNetworkService(DEFAULT_ADDRESS, configure
								.getInt(Configure.UNICAST_DEFAULT_PORT)));

				InetAddress multicastGroup = InetAddress
						.getByName(Configure.MULTICAST_GROUP_ADDRESS);

				serviceGroupMap
						.put(Configure.SERVICE_TYPE.network_multicast
								.toString(),
								getMulticastNetworkService(
										multicastGroup,
										DEFAULT_ADDRESS,
										configure
												.getInt(Configure.MULTICAST_DEFAULT_PORT)));

				serviceGroupMap
						.put(configure.get(Configure.UNICAST_RMI_BOUND_NAME),
								getRMIService(
										DEFAULT_ADDRESS,
										configure
												.getInt(Configure.RMI_DEFAULT_PORT),
										configure
												.get(Configure.UNICAST_RMI_BOUND_NAME)));

				serviceGroupMap.put(
						configure.get(Configure.ACTIVATABLE_RMI_BOUND_NAME),
						getRMIService(DEFAULT_ADDRESS, configure
								.getInt(Configure.RMI_DEFAULT_PORT), configure
								.get(Configure.ACTIVATABLE_RMI_BOUND_NAME)));

				System.out
						.println("serviceGroupMap------------------------------------>:"
								+ serviceGroupMap);

				executeAll();
			}
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

	private static Runnable getRMIService(InetAddress addr, int port,
			String serviceName) throws Exception {

		String boundName = RMIServantHelper.getBoundName(addr.getHostAddress(),
				port, serviceName);

		return rmiClient(addr, port, boundName, millis);
	}

	private static Runnable getBlockingNetworkService(InetAddress addr,
			int port, Configure.SERVICE_TYPE type) throws IOException,
			InterruptedException, ExecutionException {
		// port = NetworkServiceHelper.validatedStreamPort(port);
		SocketChannel channel = createSocketChannelFactory()
				.createBlockingSocketChannel(new InetSocketAddress(addr, port),
						type);
		return socketClient(channel, new InetSocketAddress(addr, port), millis);
	}

	private static Runnable getAsyncNetworkService(InetAddress addr, int port)
			throws IOException, InterruptedException, ExecutionException {
		// port = NetworkServiceHelper.validatedStreamPort(port);
		AsynchronousSocketChannel channel = createSocketChannelFactory()
				.createAsyncSocketChannel(new InetSocketAddress(addr, port));
		return socketClient(channel, new InetSocketAddress(addr, port), millis);
	}

	private static Runnable getUnicastNetworkService(InetAddress addr, int port)
			throws IOException, InterruptedException, ExecutionException {
		// port = NetworkServiceHelper.validatedStreamPort(port);
		DatagramChannel channel = createSocketChannelFactory()
				.createUnicastSocketChannel(StandardProtocolFamily.INET,
						new InetSocketAddress(addr, port));

		return socketClient(channel, new InetSocketAddress(addr, port), millis);
	}

	private static Runnable getMulticastNetworkService(
			InetAddress multicastGroupAddr, InetAddress addr, int port)
			throws IOException, InterruptedException, ExecutionException {
		// port = NetworkServiceHelper.validatedStreamPort(port);
		InetSocketAddress groupEndpoint = new InetSocketAddress(
				multicastGroupAddr, port);
		DatagramChannel channel = createSocketChannelFactory()
				.createMulticastSocketChannel(StandardProtocolFamily.INET,
						groupEndpoint.getAddress(),
						new InetSocketAddress(addr, port));
		return socketClient(channel, new InetSocketAddress(addr, port), millis);
	}

	private static Runnable rmiClient(InetAddress addr, int port,
			String boundName, int millis) throws Exception {

		return new Runnable() {

			String boundName;
			private int millis;
			Registry registry;

			public Runnable init(InetAddress addr, int port, String boundName,
					int millis) throws Exception {
				this.registry = RMIServantHelper.getRegistry(
						addr.getHostAddress(), port);
				this.boundName = boundName;
				this.millis = millis;
				return (this);
			}

			@Override
			public void run() {
				String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
						.getMethodName();

				LOGGER.logp(
						Level.FINER,
						THIS_CLAZZ.getSimpleName(),
						METHOD_NAME,
						"current thread is "
								+ ThreadHelper.getThreadName(Thread
										.currentThread()));
				try {
					RMIServant stub = (RMIServant) this.registry
							.lookup(this.boundName);
					String response = stub.sayHello();
					Thread.sleep(this.millis);
					System.out.println(stub + "\t:\t" + response);
				} catch (Exception e) {
					System.out.println(NFlightException.getStackTrace(e));
				}

			}

		}.init(addr, port, boundName, millis);
	}

	private static Runnable socketClient(NetworkChannel channel,
			InetSocketAddress endpoint, int millis) {
		return new Runnable() {

			NetworkChannel channel;
			private int millis;
			private InetSocketAddress endpoint;

			public Runnable init(NetworkChannel channel,
					InetSocketAddress endpoint, int millis) {
				this.channel = channel;
				this.endpoint = endpoint;

				this.millis = millis;
				return (this);
			}

			@Override
			public void run() {
				String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
						.getMethodName();

				LOGGER.logp(
						Level.FINER,
						THIS_CLAZZ.getSimpleName(),
						METHOD_NAME,
						"current thread is "
								+ ThreadHelper.getThreadName(Thread
										.currentThread()));

				try {

					String message = "Hello World! "
							+ "---------------------> "
							+ this.endpoint
							+ "\t"
							+ ThreadHelper
									.getThreadName(Thread.currentThread());

					if (this.channel instanceof AsynchronousSocketChannel) {
						((AsynchronousSocketChannel) this.channel).write(
								getStringToByteBuffer(message)).get();
						System.out.println(message + ": Sccessfully sent "
								+ message.getBytes().length
								+ " bytes to the Server!");
					} else if (this.channel instanceof SocketChannel) {
						((SocketChannel) this.channel)
								.write(getStringToByteBuffer(message));
						System.out.println(message + ": Sccessfully sent "
								+ message.getBytes().length
								+ " bytes to the Server!");
					} else if (this.channel instanceof DatagramChannel) {
						int sent = ((DatagramChannel) this.channel).send(
								getStringToByteBuffer(message), this.endpoint);
						System.out.println(message + ": Sccessfully sent "
								+ sent + " bytes to the Server!");

					} else {
						System.out.println(message + ":"
								+ channel.getClass().getName());
					}
					Thread.sleep(this.millis);
					this.channel.close();
				} catch (IOException | InterruptedException
						| ExecutionException e) {
					e.printStackTrace();
				}
			}

		}.init(channel, endpoint, millis);
	}
	protected static ByteBuffer getStringToByteBuffer(String str) {
		return ByteBuffer.wrap((str.getBytes()));
	}
	private static void executeAll() {
		ThreadGroup clientThreadGroup = new ThreadGroup(
				"nflight-client-ThreadGroup");
		for (Entry<String, Runnable> serviceSet : serviceGroupMap.entrySet()) {
			String key = serviceSet.getKey();
			Runnable value = serviceSet.getValue();
			new Thread(clientThreadGroup, value, "nflight-client-" + key)
					.start();
		}
	}

}