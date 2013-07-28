package com.abreqadhabra.nflight.application.server.concurrent.runnable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.service.IService;
import com.abreqadhabra.nflight.common.exception.WrapperException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class DatagramAcceptor implements Callable<DatagramAcceptor>,Runnable,  IService {
	private static Class<DatagramAcceptor> THIS_CLAZZ = DatagramAcceptor.class;
	private final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	DatagramChannel datagramChannel;
	int MAX_PACKET_SIZE = 65507;
	boolean isOpen;

	public DatagramAcceptor(final String address, final int port)
			throws IOException {
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

	public DatagramChannel getDatagramChannel() {
		return datagramChannel;
	}

	@Override
	public boolean isRunning() throws Exception {
		// TODO Auto-generated method stub
		return true;
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
				while (isOpen) {
					final SocketAddress clientAddress = datagramChannel
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
		} catch (final Exception e) {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					WrapperException.getStackTrace(e));
			try {
				this.disconnect();
			} catch (final IOException ioe) {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						WrapperException.getStackTrace(e));
			}

		}
	}

	@Override
	public String sayHello() throws Exception {
		return "Hello, World.";
	}

	public void sendMessage(final ByteBuffer echoText,
			final SocketAddress clientAddress) throws IOException {
		datagramChannel.send(echoText, clientAddress);
		echoText.clear();
	}

	@Override
	public void shutdown() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		datagramChannel.disconnect();
		isOpen = false;
		
	}

	@Override
	public void startup() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean status() throws Exception {
		// TODO Auto-generated method stub
		return isOpen;
	}

	@Override
	public DatagramAcceptor call() throws Exception {
		// TODO Auto-generated method stub
		return this;
	}

}