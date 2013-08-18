package com.abreqadhabra.nflight.application.container.network.client;

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

import com.abreqadhabra.nflight.application.common.launcher.Configure;
import com.abreqadhabra.nflight.application.common.launcher.Configure.SERVICE_TYPE;
import com.abreqadhabra.nflight.application.service.network.socket.SocketServiceHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class SocketFactory {
	private static Class<SocketFactory> THIS_CLAZZ = SocketFactory.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public AsynchronousSocketChannel createAsyncSocketChannel(
			SocketAddress endpoint) throws IOException, InterruptedException,
			ExecutionException {

		return (AsynchronousSocketChannel) this.getNetworkChannel(
				SERVICE_TYPE.network_async, endpoint, null, null);
	}

	public SocketChannel createBlockingSocketChannel(
			InetSocketAddress endpoint, Configure.SERVICE_TYPE type)
			throws IOException, InterruptedException, ExecutionException {

		return (SocketChannel) this.getNetworkChannel(
				SERVICE_TYPE.network_blocking, endpoint, null, null);
	}

	public DatagramChannel createUnicastSocketChannel(
			StandardProtocolFamily protocolFamily, InetSocketAddress endpoint)
			throws IOException, InterruptedException, ExecutionException {

		return (DatagramChannel) this.getNetworkChannel(
				SERVICE_TYPE.network_unicast, endpoint,
				StandardProtocolFamily.INET, null);
	}

	public DatagramChannel createMulticastSocketChannel(
			StandardProtocolFamily protocolFamily, InetAddress multicastGroup,
			InetSocketAddress endpoint) throws IOException,
			InterruptedException, ExecutionException {

		return (DatagramChannel) this.getNetworkChannel(
				SERVICE_TYPE.network_multicast, endpoint,
				StandardProtocolFamily.INET, multicastGroup);
	}

	private NetworkChannel getNetworkChannel(SERVICE_TYPE serviceType,
			SocketAddress endpoint, StandardProtocolFamily protocolFamily,
			InetAddress multicastGroup) throws IOException,
			InterruptedException, ExecutionException {

		NetworkChannel channel = null;
		NetworkInterface networkInterface = null;

		switch (serviceType) {
			case network_blocking :
			case network_nonblocking :
				channel = SocketChannel.open();
				break;
			case network_async :
				channel = AsynchronousSocketChannel.open();
				break;
			case network_unicast :
				channel = DatagramChannel.open(protocolFamily);
				break;
			case network_multicast :
				// check if the group address is multicast
				if (!multicastGroup.isMulticastAddress()) {
					throw new IllegalStateException(
							"This is not  multicast address!");
				} else {
					String networkInterfaceName = SocketServiceHelper
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

	private NetworkChannel connect(SERVICE_TYPE serviceType,
			NetworkChannel channel, SocketAddress endpoint,
			InetAddress multicastGroup, NetworkInterface networkInterface)
			throws IOException, InterruptedException, ExecutionException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// display a connecting message while ... waiting clients
		LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"connecting ..." + endpoint);

		if (channel.isOpen()) {
			// set some options
			if ((channel instanceof DatagramChannel)
					&& serviceType.equals(SERVICE_TYPE.network_multicast)) {
				SocketServiceHelper.setMulticastChannelOption(
						(DatagramChannel) channel, networkInterface.getName(),
						serviceType);
			} else {
				SocketServiceHelper.setChannelOption(channel, serviceType);
			}

			if (channel instanceof AsynchronousSocketChannel) {
				((AsynchronousSocketChannel) channel).connect(endpoint).get();
			} else if (channel instanceof SocketChannel) {
				((SocketChannel) channel).connect(endpoint);
			} else if ((channel instanceof DatagramChannel)
					&& serviceType.equals(SERVICE_TYPE.network_unicast)) {
				((DatagramChannel) channel).connect(endpoint);
			} else if ((channel instanceof DatagramChannel)
					&& serviceType.equals(SERVICE_TYPE.network_multicast)) {
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
