package com.abreqadhabra.nflight.application.service.client.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MulticastChannel;
import java.nio.channels.NetworkChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.launcher.Configure.STREAM_SERVICE_TYPE;
import com.abreqadhabra.nflight.application.service.net.NetworkServiceHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class SocketChannelFactory {
	private static final Class<SocketChannelFactory> THIS_CLAZZ = SocketChannelFactory.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public AsynchronousSocketChannel createAsyncSocketChannel(
			final SocketAddress endpoint) throws IOException,
			InterruptedException, ExecutionException {

		return (AsynchronousSocketChannel) this.getNetworkChannel(
				STREAM_SERVICE_TYPE.async, endpoint, null, null);
	}

	public SocketChannel createBlockingSocketChannel(
			final InetSocketAddress endpoint,
			final Configure.STREAM_SERVICE_TYPE type) throws IOException,
			InterruptedException, ExecutionException {

		return (SocketChannel) this.getNetworkChannel(
				STREAM_SERVICE_TYPE.blocking, endpoint, null, null);
	}

	public DatagramChannel createUnicastSocketChannel(
			final StandardProtocolFamily protocolFamily,
			final InetSocketAddress endpoint) throws IOException,
			InterruptedException, ExecutionException {

		return (DatagramChannel) this.getNetworkChannel(
				STREAM_SERVICE_TYPE.unicast, endpoint,
				StandardProtocolFamily.INET, null);
	}

	public DatagramChannel createMulticastSocketChannel(
			final StandardProtocolFamily protocolFamily,
			final InetAddress multicastGroup, final InetSocketAddress endpoint)
			throws IOException, InterruptedException, ExecutionException {

		return (DatagramChannel) this.getNetworkChannel(
				STREAM_SERVICE_TYPE.multicast, endpoint,
				StandardProtocolFamily.INET, multicastGroup);
	}

	private NetworkChannel getNetworkChannel(
			final STREAM_SERVICE_TYPE serviceType,
			final SocketAddress endpoint,
			final StandardProtocolFamily protocolFamily,
			final InetAddress multicastGroup) throws IOException,
			InterruptedException, ExecutionException {

		NetworkChannel channel = null;
		NetworkInterface networkInterface = null;

		switch (serviceType) {
			case blocking :
			case nonblocking :
				channel = SocketChannel.open();
				break;
			case async :
				channel = AsynchronousSocketChannel.open();
				break;
			case unicast :
				channel = DatagramChannel.open(protocolFamily);
				break;
			case multicast :
				// check if the group address is multicast
				if (!multicastGroup.isMulticastAddress()) {
					throw new IllegalStateException(
							"This is not  multicast address!");
				} else {
					final String networkInterfaceName = NetworkServiceHelper
							.getNetworkInterfaceName(InetAddress.getLocalHost()
									.getHostAddress());
					// join multicast group on this interface, and also use this
					// interface for outgoing multicast datagrams
					// get the network interface used for multicast
					networkInterface = NetworkInterface
							.getByName(networkInterfaceName);
					channel = DatagramChannel.open(protocolFamily);
				}

				break;
			default :
				break;
		}
		
		return this.connect(serviceType, channel, endpoint, multicastGroup,
				networkInterface);
	}

	private NetworkChannel connect(final STREAM_SERVICE_TYPE serviceType,
			final NetworkChannel channel, final SocketAddress endpoint,
			final InetAddress multicastGroup,
			final NetworkInterface networkInterface) throws IOException,
			InterruptedException, ExecutionException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// display a connecting message while ... waiting clients
		LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"connecting ..." + endpoint);

		if (channel.isOpen()) {
			// set some options
			if ((channel instanceof DatagramChannel)
					&& serviceType.equals(STREAM_SERVICE_TYPE.multicast)) {
				NetworkServiceHelper.setMulticastChannelOption(
						(DatagramChannel) channel, networkInterface.getName(),
						serviceType);
			} else {
				NetworkServiceHelper.setChannelOption(channel, serviceType);
			}

			if (channel instanceof AsynchronousSocketChannel) {
				((AsynchronousSocketChannel) channel).connect(endpoint).get();
			} else if (channel instanceof SocketChannel) {
				((SocketChannel) channel).connect(endpoint);
			} else if ((channel instanceof DatagramChannel)
					&& serviceType.equals(STREAM_SERVICE_TYPE.unicast)) {
				((DatagramChannel) channel).connect(endpoint);
			} else if ((channel instanceof DatagramChannel)
					&& serviceType.equals(STREAM_SERVICE_TYPE.multicast)) {
				((DatagramChannel) channel).connect(endpoint);
				((MulticastChannel) channel).join(multicastGroup,
						networkInterface);
			}

			return channel;
		} else {
			throw new IllegalStateException("채널이 열려있지 않습니다.");
		}
	}
}
