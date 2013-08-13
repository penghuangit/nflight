package com.abreqadhabra.nflight.application.service.net;

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

import com.abreqadhabra.nflight.application.launcher.Configure.STREAM_SERVICE_TYPE;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ServerChannelFactory {
	private static final Class<ServerChannelFactory> THIS_CLAZZ = ServerChannelFactory.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public AsynchronousServerSocketChannel createAsyncServerSocketChannel(
			final ThreadPoolExecutor threadPool, final int initialSize,
			final SocketAddress endpoint, final int backlog)
			throws IOException, InterruptedException, ExecutionException {

		return (AsynchronousServerSocketChannel) this.getNetworkChannel(
				STREAM_SERVICE_TYPE.async, endpoint, backlog, null, threadPool,
				initialSize);
	}

	public ServerSocketChannel createBlockingServerSocketChannel(
			final InetSocketAddress endpoint, final int backlog)
			throws IOException, InterruptedException, ExecutionException {

		return (ServerSocketChannel) this.getNetworkChannel(
				STREAM_SERVICE_TYPE.blocking, endpoint, backlog, null, null, 0);
	}

	public ServerSocketChannel createNonBlockingServerSocketChannel(
			final InetSocketAddress endpoint, final int backlog)
			throws IOException, InterruptedException, ExecutionException {

		return (ServerSocketChannel) this.getNetworkChannel(
				STREAM_SERVICE_TYPE.nonblocking, endpoint, backlog, null, null,
				0);
	}

	public DatagramChannel createUnicastDatagramChannel(
			final ProtocolFamily family, final InetSocketAddress endpoint)
			throws IOException, InterruptedException, ExecutionException {

		return (DatagramChannel) this.getNetworkChannel(
				STREAM_SERVICE_TYPE.unicast, endpoint, 0,
				StandardProtocolFamily.INET, null, 0);
	}

	public DatagramChannel createMulticastDatagramChannel(
			final StandardProtocolFamily family,
			final InetSocketAddress endpoint) throws IOException,
			InterruptedException, ExecutionException {

		return (DatagramChannel) this.getNetworkChannel(
				STREAM_SERVICE_TYPE.multicast, endpoint, 0,
				StandardProtocolFamily.INET, null, 0);
	}

	private NetworkChannel getNetworkChannel(
			final STREAM_SERVICE_TYPE serviceType,
			final SocketAddress endpoint, final int backlog,
			final StandardProtocolFamily protocolFamily,
			final ThreadPoolExecutor threadPool, final int initialSize)
			throws IOException, InterruptedException, ExecutionException {

		NetworkChannel channel = null;
		String networkInterfaceName = null;

		switch (serviceType) {
			case blocking :
			case nonblocking :
				channel = ServerSocketChannel.open();
				break;
			case async :
				// create asynchronous server-socket channel bound to the thread
				// Group
				final AsynchronousChannelGroup threadGroup = AsynchronousChannelGroup
						.withCachedThreadPool(threadPool, initialSize);
				channel = AsynchronousServerSocketChannel.open(threadGroup);
				break;
			case unicast :
				channel = DatagramChannel.open(protocolFamily);
				break;
			case multicast :
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

	private NetworkChannel bind(final STREAM_SERVICE_TYPE serviceType,
			final NetworkChannel channel, final SocketAddress endpoint,
			final int backlog, final String networkInterfaceName)
			throws IOException, InterruptedException, ExecutionException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		if (channel.isOpen()) {
			// set some options
			if ((channel instanceof DatagramChannel)
					&& serviceType.equals(STREAM_SERVICE_TYPE.multicast)) {
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
				if (serviceType.equals(STREAM_SERVICE_TYPE.nonblocking)) {
					// configure blocking mode
					((ServerSocketChannel) channel).configureBlocking(false);
				}
				((ServerSocketChannel) channel).bind(endpoint, backlog);
			} else if ((channel instanceof DatagramChannel)
					&& serviceType.equals(STREAM_SERVICE_TYPE.unicast)) {
				// configure non-blocking mode
				((DatagramChannel) channel).configureBlocking(false);
				((DatagramChannel) channel).bind(endpoint);
			} else if ((channel instanceof DatagramChannel)
					&& serviceType.equals(STREAM_SERVICE_TYPE.multicast)) {
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
