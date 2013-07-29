package com.abreqadhabra.nflight.application.server.transport.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public final class DatagramConnectorImpl implements DatagramConnector {
	private static final Class<DatagramConnectorImpl> THIS_CLAZZ = DatagramConnectorImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	InetSocketAddress remoteSocketAddress;

	/**
	 * Creates a new instance.
	 * 
	 * @throws UnknownHostException
	 */
	public DatagramConnectorImpl() throws UnknownHostException {

		String defaultAddress = InetAddress.getLocalHost().getHostAddress();
		int defaultPort = 9999;
		this.setRemoteSocketAddress(new InetSocketAddress(defaultAddress,
				defaultPort));
	}
	
	public DatagramConnectorImpl(String remoteAddress, int remotePort)  {
		this.setRemoteSocketAddress(new InetSocketAddress(remoteAddress,
				remotePort));
	}

	@Override
	public InetSocketAddress getRemoteSocketAddress() {
		return remoteSocketAddress;
	}

	@Override
	public void setRemoteSocketAddress(InetSocketAddress remoteSocketAddress) {
		this.remoteSocketAddress = remoteSocketAddress;

	}

	@Override
	public DatagramChannel connect()
			throws IOException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		if (remoteSocketAddress == null) {
			throw new IllegalArgumentException("remoteAddress");
		}

		// create a new datagram channel
		DatagramChannel datagramChannel = DatagramChannel
				.open(StandardProtocolFamily.INET);

		// check if it the channel was successfully opened
		if (datagramChannel.isOpen()) {
			// set some options
			datagramChannel
					.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024)
					.setOption(StandardSocketOptions.SO_SNDBUF, 4 * 1024);
			// connect the channel to local address
			datagramChannel.connect(remoteSocketAddress);
		} else {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"The channel cannot be opened!");
		}
		return datagramChannel;
	}


	@Override
	public DatagramChannel bind() throws IOException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		if (remoteSocketAddress == null) {
			throw new IllegalArgumentException("remoteAddress");
		}

		// create a new datagram channel
		DatagramChannel datagramChannel = DatagramChannel
				.open(StandardProtocolFamily.INET);

		// check if it the channel was successfully opened
		if (datagramChannel.isOpen()) {
			// set some options
			datagramChannel
					.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024)
					.setOption(StandardSocketOptions.SO_SNDBUF, 4 * 1024);
			// bind the channel to local address
			datagramChannel.bind(remoteSocketAddress);
		} else {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"The channel cannot be opened!");
		}
		return datagramChannel;
	}

	
	public void close(DatagramChannel datagramChannel) throws Exception {
		datagramChannel.disconnect();
		datagramChannel.close();
	}
	
}
