package com.abreqadhabra.nflight.app.server.service.net.sample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;

/**
 * 
 * @author Apress
 */
public class SocketServer02 {

	public static void main(final String[] args) {
		final int LOCAL_PORT = 5555;
		final String LOCAL_IP = "127.0.0.1"; // modify this to your local IP
		final int MAX_PACKET_SIZE = 65507;

		final ByteBuffer echoText = ByteBuffer.allocateDirect(MAX_PACKET_SIZE);

		// create a new datagram channel
		try (DatagramChannel datagramChannel = DatagramChannel
				.open(StandardProtocolFamily.INET)) {

			// check if it the channel was successfully opened
			if (datagramChannel.isOpen()) {

				System.out.println("Echo server was successfully opened!");
				// set some options
				datagramChannel.setOption(StandardSocketOptions.SO_RCVBUF,
						4 * 1024);
				datagramChannel.setOption(StandardSocketOptions.SO_SNDBUF,
						4 * 1024);
				// bind the channel to local address
				datagramChannel
						.bind(new InetSocketAddress(LOCAL_IP, LOCAL_PORT));
				System.out.println("Echo server was binded on: "
						+ datagramChannel.getLocalAddress());
				System.out.println("Echo server is ready to echo ...");

				// transmitting data packets
				while (true) {

					final SocketAddress clientAddress = datagramChannel
							.receive(echoText);

					echoText.flip();
					System.out.println("I have received " + echoText.limit()
							+ " bytes from " + clientAddress.toString()
							+ "! Sending them back ...");
					datagramChannel.send(echoText, clientAddress);
					echoText.clear();
				}
			} else {
				System.out.println("The channel cannot be opened!");
			}
		} catch (final Exception ex) {
			if (ex instanceof ClosedChannelException) {
				System.err.println("The channel was unexpected closed ...");
			}
			if (ex instanceof SecurityException) {
				System.err.println("A security exception occured ...");
			}
			if (ex instanceof IOException) {
				System.err.println("An I/O error occured ...");
			}

			System.err.println("\n" + ex);
		}
	}
}
