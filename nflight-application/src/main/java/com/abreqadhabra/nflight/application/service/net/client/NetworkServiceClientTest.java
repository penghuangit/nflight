package com.abreqadhabra.nflight.application.service.net.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.NetworkChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map.Entry;
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

	static HashMap<String, Runnable> serviceGroupMap = new HashMap<String, Runnable>();
	static int cnt = 1;
	static int millis = 10;

	public static SocketChannelFactory createSocketChannelFactory() {
		return new SocketChannelFactory();
	}

	public static void main(final String[] args) throws UnknownHostException {

		final InetAddress DEFAULT_ADDRESS = InetAddress.getLocalHost();

		final Configure configure = new ConfigureImpl(
				Configure.FILE_SOCKET_SERVER_PROPERTIES);

		serviceGroupMap.put(
				Configure.STREAM_SERVICE_TYPE.blocking.toString(),
				getBlockingNetworkService(DEFAULT_ADDRESS,
						configure.getInt(Configure.BLOCKING_DEFAULT_PORT),
						Configure.STREAM_SERVICE_TYPE.blocking));
		serviceGroupMap.put(
				Configure.STREAM_SERVICE_TYPE.nonblocking.toString(),
				getBlockingNetworkService(DEFAULT_ADDRESS,
						configure.getInt(Configure.NONBLOCKING_DEFAULT_PORT),
						Configure.STREAM_SERVICE_TYPE.nonblocking));
		serviceGroupMap.put(
				Configure.STREAM_SERVICE_TYPE.async.toString(),
				getAsyncNetworkService(DEFAULT_ADDRESS,
						configure.getInt(Configure.ASYNC_DEFAULT_PORT)));

		serviceGroupMap.put(
				Configure.STREAM_SERVICE_TYPE.unicast.toString(),
				getUnicastNetworkService(DEFAULT_ADDRESS,
						configure.getInt(Configure.UNICAST_DEFAULT_PORT)));

		final InetAddress multicastGroup = InetAddress
				.getByName(Configure.MULTICAST_GROUP_ADDRESS);

		serviceGroupMap.put(
				Configure.STREAM_SERVICE_TYPE.multicast.toString(),
				getMulticastNetworkService(multicastGroup, DEFAULT_ADDRESS,
						configure.getInt(Configure.MULTICAST_DEFAULT_PORT)));

		executeAll();

	}

	private static Runnable getBlockingNetworkService(final InetAddress addr,
			final int port, final Configure.STREAM_SERVICE_TYPE type) {
		// port = NetworkServiceHelper.validatedStreamPort(port);
		final SocketChannel channel = createSocketChannelFactory()
				.createBlockingSocketChannel(new InetSocketAddress(addr, port),
						type);
		return clientTest(channel, new InetSocketAddress(addr, port), cnt,
				millis);
	}

	private static Runnable getAsyncNetworkService(final InetAddress addr,
			final int port) {
		// port = NetworkServiceHelper.validatedStreamPort(port);
		final AsynchronousSocketChannel channel = createSocketChannelFactory()
				.createAsyncSocketChannel(new InetSocketAddress(addr, port));
		return clientTest(channel, new InetSocketAddress(addr, port), cnt,
				millis);
	}

	private static Runnable getUnicastNetworkService(final InetAddress addr,
			final int port) {
		// port = NetworkServiceHelper.validatedStreamPort(port);
		final DatagramChannel channel = createSocketChannelFactory()
				.createUnicastSocketChannel(StandardProtocolFamily.INET,
						new InetSocketAddress(addr, port));
		return clientTest(channel, new InetSocketAddress(addr, port), cnt,
				millis);
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
		return clientTest(channel, new InetSocketAddress(addr, port), cnt,
				millis);
	}

	private static Runnable clientTest(final NetworkChannel channel,
			final InetSocketAddress endpoint, final int num, final int millis) {
		return new Runnable() {

			NetworkChannel channel;
			int num;
			private int millis;
			private InetSocketAddress endpoint;

			public Runnable init(final NetworkChannel channel,
					final InetSocketAddress multicastGroup, final int num,
					final int millis) {
				this.channel = channel;
				this.endpoint = multicastGroup;
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

				for (int i = 0; i < this.num; i++) {
					try {

						final String message = "Hello World! "
								+ this.channel.getLocalAddress().toString()
								+ "---------------------> " + this.endpoint;;

						if (this.channel instanceof AsynchronousSocketChannel) {
							((AsynchronousSocketChannel) this.channel).write(
									getStringToByteBuffer(message)).get();
						} else if (this.channel instanceof SocketChannel) {
							((SocketChannel) this.channel)
									.write(getStringToByteBuffer(message));
						} else if (this.channel instanceof DatagramChannel) {
							final int sent = ((DatagramChannel) this.channel)
									.send(getStringToByteBuffer(message),
											this.endpoint);
							System.out.println(message + ": Sccessfully sent "
									+ sent + " bytes to the Server!");

						} else {
							// throw
						}
						Thread.sleep(this.millis);
						System.out.println(i + 1 + ":" + this.channel);
						this.channel.close();
					} catch (IOException | InterruptedException
							| ExecutionException e) {
						e.printStackTrace();
					}
				}
			}

		}.init(channel, endpoint, num, millis);
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