package com.abreqadhabra.nflight.application.transport.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ProtocolFamily;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.NetworkChannel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.launcher.Configure.SERVICE_TYPE;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ServerChannelFactory {
	private static Class<ServerChannelFactory> THIS_CLAZZ = ServerChannelFactory.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public AsynchronousServerSocketChannel createAsyncServerSocketChannel(
			ThreadPoolExecutor threadPool, int initialSize,
			SocketAddress endpoint, int backlog)
			throws IOException, InterruptedException, ExecutionException {

		return (AsynchronousServerSocketChannel) this.getNetworkChannel(
				SERVICE_TYPE.network_async, endpoint, backlog, null, threadPool,
				initialSize);
	}

	public ServerSocketChannel createBlockingServerSocketChannel(
			InetSocketAddress endpoint, int backlog)
			throws IOException, InterruptedException, ExecutionException {

		return (ServerSocketChannel) this.getNetworkChannel(
				SERVICE_TYPE.network_blocking, endpoint, backlog, null, null, 0);
	}

	public ServerSocketChannel createNonBlockingServerSocketChannel(
			InetSocketAddress endpoint, int backlog)
			throws IOException, InterruptedException, ExecutionException {

		return (ServerSocketChannel) this.getNetworkChannel(
				SERVICE_TYPE.network_nonblocking, endpoint, backlog, null, null,
				0);
	}

	public DatagramChannel createUnicastDatagramChannel(
			ProtocolFamily family, InetSocketAddress endpoint)
			throws IOException, InterruptedException, ExecutionException {

		return (DatagramChannel) this.getNetworkChannel(
				SERVICE_TYPE.network_unicast, endpoint, 0,
				StandardProtocolFamily.INET, null, 0);
	}

	public DatagramChannel createMulticastDatagramChannel(
			StandardProtocolFamily family,
			InetSocketAddress endpoint) throws IOException,
			InterruptedException, ExecutionException {

		return (DatagramChannel) this.getNetworkChannel(
				SERVICE_TYPE.network_multicast, endpoint, 0,
				StandardProtocolFamily.INET, null, 0);
	}

	private NetworkChannel getNetworkChannel(
			SERVICE_TYPE serviceType,
			SocketAddress endpoint, int backlog,
			StandardProtocolFamily protocolFamily,
			ThreadPoolExecutor threadPool, int initialSize)
			throws IOException, InterruptedException, ExecutionException {

		NetworkChannel channel = null;
		String networkInterfaceName = null;

		switch (serviceType) {
			case network_blocking :
			case network_nonblocking :
				channel = ServerSocketChannel.open();
				break;
			case network_async :
				// create asynchronous server-socket channel bound to the thread
				// Group
				AsynchronousChannelGroup threadGroup = AsynchronousChannelGroup
						.withCachedThreadPool(threadPool, initialSize);
				channel = AsynchronousServerSocketChannel.open(threadGroup);
				break;
			case network_unicast :
				channel = DatagramChannel.open(protocolFamily);
				break;
			case network_multicast :
				networkInterfaceName = NetworkServiceHelper
						.getNetworkInterfaceName(InetAddress.getLocalHost()
								.getHostAddress());
				channel = DatagramChannel.open(protocolFamily);
				break;
			default :
				break;

		}
		return this.bind(serviceType, channel, endpoint, backlog,
				networkInterfaceName);
	}

	private NetworkChannel bind(SERVICE_TYPE serviceType,
			NetworkChannel channel, SocketAddress endpoint,
			int backlog, String networkInterfaceName)
			throws IOException, InterruptedException, ExecutionException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		if (channel.isOpen()) {
			// set some options
			if ((channel instanceof DatagramChannel)
					&& serviceType.equals(SERVICE_TYPE.network_multicast)) {
				NetworkServiceHelper.setMulticastChannelOption(
						(DatagramChannel) channel, networkInterfaceName,
						serviceType);
			} else {
				NetworkServiceHelper.setChannelOption(channel, serviceType);
			}

			if (channel instanceof AsynchronousServerSocketChannel) {
				((AsynchronousServerSocketChannel) channel).bind(endpoint,
						backlog);
			} else if (channel instanceof ServerSocketChannel) {
				if (serviceType.equals(SERVICE_TYPE.network_nonblocking)) {
					// configure blocking mode
					((ServerSocketChannel) channel).configureBlocking(false);
				}
				((ServerSocketChannel) channel).bind(endpoint, backlog);
			} else if ((channel instanceof DatagramChannel)
					&& serviceType.equals(SERVICE_TYPE.network_unicast)) {
				// configure non-blocking mode
				((DatagramChannel) channel).configureBlocking(false);
				((DatagramChannel) channel).bind(endpoint);
			} else if ((channel instanceof DatagramChannel)
					&& serviceType.equals(SERVICE_TYPE.network_multicast)) {
				((DatagramChannel) channel).bind(endpoint);

			}

			// display a waiting message while ... waiting clients
			LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"Waiting for connections ..." + endpoint);

			return channel;

		} else {
			throw new IllegalStateException("채널이 열려있지 않습니다.");
		}
	}

}
