package com.abreqadhabra.nflight.application.service.client;

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

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.application.service.client.net.SocketChannelFactory;
import com.abreqadhabra.nflight.application.service.rmi.RMIServant;
import com.abreqadhabra.nflight.application.service.rmi.RMIServiceHelper;
import com.abreqadhabra.nflight.common.exception.WrapperException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ServiceClientTest {
	private static final Class<ServiceClientTest> THIS_CLAZZ = ServiceClientTest.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	static HashMap<String, Runnable> serviceGroupMap = new HashMap<String, Runnable>();
	static int cnt = 1000;
	static int millis = 1;

	public static SocketChannelFactory createSocketChannelFactory() {
		return new SocketChannelFactory();
	}

	public static void main(final String[] args) throws Exception {

		final InetAddress DEFAULT_ADDRESS = InetAddress.getLocalHost();

		final Configure netConfigure = new ConfigureImpl(
				Configure.FILE_NETWORK_SERVICE_PROPERTIES);

		final Configure rmiConfigure = new ConfigureImpl(
				Configure.FILE_RMI_SERVICE_PROPERTIES);
		for (int i = 0; i < cnt; i++) {

			serviceGroupMap.put(
					Configure.STREAM_SERVICE_TYPE.blocking.toString(),
					getBlockingNetworkService(DEFAULT_ADDRESS, netConfigure
							.getInt(Configure.BLOCKING_DEFAULT_PORT),
							Configure.STREAM_SERVICE_TYPE.blocking));
			serviceGroupMap.put(
					Configure.STREAM_SERVICE_TYPE.nonblocking.toString(),
					getBlockingNetworkService(DEFAULT_ADDRESS, netConfigure
							.getInt(Configure.NONBLOCKING_DEFAULT_PORT),
							Configure.STREAM_SERVICE_TYPE.nonblocking));
			serviceGroupMap.put(
					Configure.STREAM_SERVICE_TYPE.async.toString(),
					getAsyncNetworkService(DEFAULT_ADDRESS,
							netConfigure.getInt(Configure.ASYNC_DEFAULT_PORT)));

			serviceGroupMap
					.put(Configure.STREAM_SERVICE_TYPE.unicast.toString(),
							getUnicastNetworkService(
									DEFAULT_ADDRESS,
									netConfigure
											.getInt(Configure.UNICAST_DEFAULT_PORT)));

			final InetAddress multicastGroup = InetAddress
					.getByName(Configure.MULTICAST_GROUP_ADDRESS);

			serviceGroupMap.put(
					Configure.STREAM_SERVICE_TYPE.multicast.toString(),
					getMulticastNetworkService(multicastGroup, DEFAULT_ADDRESS,
							netConfigure
									.getInt(Configure.MULTICAST_DEFAULT_PORT)));

			serviceGroupMap.put(
					Configure.RMI_SERVICE_TYPE.unicast.toString(),
					getUnicastRMIService(DEFAULT_ADDRESS, rmiConfigure
							.getInt(Configure.UNICAST_RMI_DEFAULT_PORT)));

			serviceGroupMap.put(
					Configure.RMI_SERVICE_TYPE.activatable.toString(),
					getActivatableRMIService(DEFAULT_ADDRESS, rmiConfigure
							.getInt(Configure.ACTIVATABLE_RMI_DEFAULT_PORT)));

			executeAll();
		}

	}

	private static Runnable getActivatableRMIService(final InetAddress addr,
			final int port) throws Exception {

		String boundName = RMIServiceHelper.getBoundName(addr.getHostAddress(),
				port, Configure.RMI_SERVICE_TYPE.activatable.toString());

		return rmiClient(addr, port, boundName, millis);

	}

	private static Runnable getUnicastRMIService(final InetAddress addr,
			final int port) throws Exception {

		String boundName = RMIServiceHelper.getBoundName(addr.getHostAddress(),
				port, Configure.RMI_SERVICE_TYPE.unicast.toString());

		return rmiClient(addr, port, boundName, millis);
	}

	private static Runnable getBlockingNetworkService(final InetAddress addr,
			final int port, final Configure.STREAM_SERVICE_TYPE type) {
		// port = NetworkServiceHelper.validatedStreamPort(port);
		final SocketChannel channel = createSocketChannelFactory()
				.createBlockingSocketChannel(new InetSocketAddress(addr, port),
						type);
		return scoketClient(channel, new InetSocketAddress(addr, port), millis);
	}

	private static Runnable getAsyncNetworkService(final InetAddress addr,
			final int port) {
		// port = NetworkServiceHelper.validatedStreamPort(port);
		final AsynchronousSocketChannel channel = createSocketChannelFactory()
				.createAsyncSocketChannel(new InetSocketAddress(addr, port));
		return scoketClient(channel, new InetSocketAddress(addr, port), millis);
	}

	private static Runnable getUnicastNetworkService(final InetAddress addr,
			final int port) {
		// port = NetworkServiceHelper.validatedStreamPort(port);
		final DatagramChannel channel = createSocketChannelFactory()
				.createUnicastSocketChannel(StandardProtocolFamily.INET,
						new InetSocketAddress(addr, port));
		return scoketClient(channel, new InetSocketAddress(addr, port), millis);
	}

	private static Runnable getMulticastNetworkService(
			final InetAddress multicastGroupAddr, final InetAddress addr,
			final int port) {
		// port = NetworkServiceHelper.validatedStreamPort(port);
		final InetSocketAddress groupEndpoint = new InetSocketAddress(
				multicastGroupAddr, port);
		final DatagramChannel channel = createSocketChannelFactory()
				.createMulticastSocketChannel(StandardProtocolFamily.INET,
						groupEndpoint.getAddress(),
						new InetSocketAddress(addr, port));
		return scoketClient(channel, new InetSocketAddress(addr, port), millis);
	}

	private static Runnable rmiClient(InetAddress addr, int port,
			String boundName, int millis) throws Exception {

		return new Runnable() {

			String boundName;
			private int millis;
			Registry registry;

			public Runnable init(InetAddress addr, int port, String boundName,
					int millis) throws Exception {
				this.registry = RMIServiceHelper.getRegistry(
						addr.getHostAddress(), port);
				this.boundName = boundName;
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
				try {
					RMIServant stub = (RMIServant) registry.lookup(boundName);
					String response = stub.sayHello();
					Thread.sleep(this.millis);
					System.out.println(stub + "\t:\t" + response);
				} catch (Exception e) {
					System.out.println(WrapperException.getStackTrace(e));
				}

			}

		}.init(addr, port, boundName, millis);
	}

	private static Runnable scoketClient(final NetworkChannel channel,
			final InetSocketAddress endpoint, final int millis) {
		return new Runnable() {

			NetworkChannel channel;
			private int millis;
			private InetSocketAddress endpoint;

			public Runnable init(final NetworkChannel channel,
					final InetSocketAddress multicastGroup, final int millis) {
				this.channel = channel;
				this.endpoint = multicastGroup;
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

				try {

					final String message = "Hello World! "
							+ "---------------------> " + this.endpoint;;

					if (this.channel instanceof AsynchronousSocketChannel) {
						((AsynchronousSocketChannel) this.channel).write(
								getStringToByteBuffer(message)).get();
					} else if (this.channel instanceof SocketChannel) {
						((SocketChannel) this.channel)
								.write(getStringToByteBuffer(message));
					} else if (this.channel instanceof DatagramChannel) {
						final int sent = ((DatagramChannel) this.channel).send(
								getStringToByteBuffer(message), this.endpoint);
						System.out.println(message + ": Sccessfully sent "
								+ sent + " bytes to the Server!");

					} else {
						// throw
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
	protected static ByteBuffer getStringToByteBuffer(final String str) {
		return ByteBuffer.wrap((str.getBytes()));
	}
	private static void executeAll() {
		final ThreadGroup clientThreadGroup = new ThreadGroup(
				"nflight-client-ThreadGroup");
		for (final Entry<String, Runnable> serviceSet : serviceGroupMap
				.entrySet()) {
			final String key = serviceSet.getKey();
			final Runnable value = serviceSet.getValue();
			new Thread(clientThreadGroup, value, "nflight-client-" + key)
					.start();
		}
	}

}