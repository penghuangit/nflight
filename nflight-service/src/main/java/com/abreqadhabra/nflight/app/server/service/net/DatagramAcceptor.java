package com.abreqadhabra.nflight.app.server.service.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.WrapperException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class DatagramAcceptor extends Thread {
	private static Class<DatagramAcceptor> THIS_CLAZZ = DatagramAcceptor.class;
	private Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	DatagramChannel datagramChannel;
	int MAX_PACKET_SIZE = 65507;
	boolean isOpen;

	public DatagramAcceptor(String address, int port) throws IOException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// create a new datagram channel
		datagramChannel = DatagramChannel.open(StandardProtocolFamily.INET);

		// check if it the channel was successfully opened
		isOpen = datagramChannel.isOpen();

		if (isOpen) {
			// set some options
			datagramChannel
					.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024)
					.setOption(StandardSocketOptions.SO_SNDBUF, 4 * 1024);
			// bind the channel to local address
			datagramChannel.bind(new InetSocketAddress(address, port));
		} else {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"The channel cannot be opened!");
		}
	}

	public void disconnect() throws IOException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		datagramChannel.disconnect();
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"The channel cannot be opened : " + datagramChannel.toString());
	}

	@Override
	public void run() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			// check if it the channel was successfully opened
			if (isOpen) {

				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"Echo server was successfully opened!");

				LOGGER.logp(
						Level.FINER,
						THIS_CLAZZ.getName(),
						METHOD_NAME,
						"Echo server was binded on: "
								+ datagramChannel.getLocalAddress());

				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"Echo server is ready to echo ...");

				final ByteBuffer echoText = ByteBuffer
						.allocateDirect(MAX_PACKET_SIZE);

				// transmitting data packets
				while (true) {
					SocketAddress clientAddress = datagramChannel
							.receive(echoText);
					echoText.flip();
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
							"I have received " + echoText.limit()
									+ " bytes from " + clientAddress.toString()
									+ "! Sending them back ...");

				}

			} else {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"The channel cannot be opened!");
			}
		} catch (Exception e) {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					WrapperException.getStackTrace(e));
			try {
				disconnect();
			} catch (IOException ioe) {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						WrapperException.getStackTrace(e));
			}

		}
	}

	public void sendMessage(ByteBuffer echoText, SocketAddress clientAddress)
			throws IOException {
		datagramChannel.send(echoText, clientAddress);
		echoText.clear();
	}

}