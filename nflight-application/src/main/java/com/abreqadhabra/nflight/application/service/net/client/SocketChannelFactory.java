package com.abreqadhabra.nflight.application.service.net.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.NetworkServiceHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class SocketChannelFactory {
	private static final Class<SocketChannelFactory> THIS_CLAZZ = SocketChannelFactory.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public AsynchronousSocketChannel createAsyncSocketChannel(
			final SocketAddress endpoint) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			final AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel
					.open();
			if (socketChannel.isOpen()) {
				// display a connecting message while ... waiting clients
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, socketChannel.getRemoteAddress()
								+ ": connecting ...");
				// set some options
				NetworkServiceHelper.setChannelOption(socketChannel,
						Configure.STREAM_SERVICE_TYPE.async);
				socketChannel.connect(endpoint).get();

				return socketChannel;
			} else {
				throw new IllegalStateException("채널이 열려있지 않습니다.");
			}
		} catch (final IOException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;

	}

	public SocketChannel createBlockingSocketChannel(
			final InetSocketAddress endpoint,
			final Configure.STREAM_SERVICE_TYPE type) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			final SocketChannel socketChannel = SocketChannel.open();
			if (socketChannel.isOpen()) {
				// display a connecting message while ... waiting clients
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, socketChannel.getRemoteAddress()
								+ ": connecting ...");
				NetworkServiceHelper.setChannelOption(socketChannel, type);
				socketChannel.connect(endpoint);
				return socketChannel;
			} else {
				throw new IllegalStateException("채널이 열려있지 않습니다.");
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public DatagramChannel createUnicastSocketChannel(
			final StandardProtocolFamily protocolFamily,
			final InetSocketAddress endpoint) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			final DatagramChannel socketChannel = DatagramChannel
					.open(protocolFamily);
			if (socketChannel.isOpen()) {
				// display a connecting message while ... waiting clients
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, socketChannel.getRemoteAddress()
								+ ": connecting ...");
				NetworkServiceHelper.setChannelOption(socketChannel,
						Configure.STREAM_SERVICE_TYPE.unicast);
				socketChannel.connect(endpoint);
				return socketChannel;
			} else {
				throw new IllegalStateException("채널이 열려있지 않습니다.");
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public DatagramChannel createMulticastSocketChannel(
			final StandardProtocolFamily protocolFamily,
			final InetAddress multicastGroup, final InetSocketAddress endpoint) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {

			final String networkInterfaceName = NetworkServiceHelper
					.getNetworkInterfaceName(InetAddress.getLocalHost()
							.getHostAddress());

			// join multicast group on this interface, and also use this
			// interface for outgoing multicast datagrams
			// get the network interface used for multicast
			final NetworkInterface networkInterface = NetworkInterface
					.getByName(networkInterfaceName);

			final DatagramChannel socketChannel = DatagramChannel
					.open(protocolFamily);

			// check if the group address is multicast
			if (multicastGroup.isMulticastAddress()) {

				if (socketChannel.isOpen()) {
					// display a waiting message while ... waiting clients
					LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(),
							METHOD_NAME, endpoint + ": connecting ...");

					NetworkServiceHelper.setMulticastChannelOption(
							socketChannel, networkInterfaceName,
							Configure.STREAM_SERVICE_TYPE.multicast);

					socketChannel.bind(endpoint);

					@SuppressWarnings("unused")
					final MembershipKey key = socketChannel.join(
							multicastGroup, networkInterface);

					return socketChannel;
				} else {
					throw new IllegalStateException("채널이 열려있지 않습니다.");
				}

			} else {
				System.out.println("This is not  multicast address!");
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
