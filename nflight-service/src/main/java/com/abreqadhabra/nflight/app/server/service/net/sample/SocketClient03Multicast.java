package com.abreqadhabra.nflight.app.server.service.net.sample;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * 
 * @author Apress
 */
public class SocketClient03Multicast {

	public static void main(final String[] args) {

		final int DEFAULT_PORT = 5555;
		final int MAX_PACKET_SIZE = 65507;
		final String GROUP = "225.4.5.6";

		CharBuffer charBuffer = null;
		final Charset charset = Charset.defaultCharset();
		final CharsetDecoder decoder = charset.newDecoder();
		final ByteBuffer datetime = ByteBuffer.allocateDirect(MAX_PACKET_SIZE);

		// create a new channel
		try (DatagramChannel datagramChannel = DatagramChannel
				.open(StandardProtocolFamily.INET)) {

			final InetAddress group = InetAddress.getByName(GROUP);
			// check if the group socketAddress is multicast
			if (group.isMulticastAddress()) {
				// check if the channel was successfully created
				if (datagramChannel.isOpen()) {

					// get the network interface used for multicast
					final NetworkInterface networkInterface = NetworkInterface
							.getByName("eth3");

					// set some options
					datagramChannel.setOption(
							StandardSocketOptions.SO_REUSEADDR, true);
					// bind the channel to the local socketAddress
					datagramChannel.bind(new InetSocketAddress(DEFAULT_PORT));
					// join the multicast group and get ready to receive
					// datagrams
					final MembershipKey key = datagramChannel.join(group,
							networkInterface);

					// wait for datagrams
					while (true) {

						if (key.isValid()) {

							datagramChannel.receive(datetime);
							datetime.flip();
							charBuffer = decoder.decode(datetime);
							System.out.println(charBuffer.toString());
							datetime.clear();
						} else {
							break;
						}
					}

				} else {
					System.out.println("The channel cannot be opened!");
				}
			} else {
				System.out.println("This is not  multicast socketAddress!");
			}

		} catch (final IOException ex) {
			System.err.println(ex);
		}

	}
}
