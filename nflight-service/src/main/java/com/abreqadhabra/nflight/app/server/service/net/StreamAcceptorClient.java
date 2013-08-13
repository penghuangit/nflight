package com.abreqadhabra.nflight.app.server.service.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import com.abreqadhabra.nflight.application.server.concurrent.runnable.StreamAcceptorClientHandler;

/**
 *
 */
public class StreamAcceptorClient {

	public static void main(String[] args) throws UnknownHostException {

		int DEFAULT_PORT = 9999;
		String IP = InetAddress.getLocalHost().getHostAddress();

		// create asynchronous socket channel bound to the default group
		try (AsynchronousSocketChannel asyncSocketChannel = AsynchronousSocketChannel
				.open()) {

			if (asyncSocketChannel.isOpen()) {

				// set some options
				asyncSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF,
						128 * 1024);
				asyncSocketChannel.setOption(StandardSocketOptions.SO_SNDBUF,
						128 * 1024);
				asyncSocketChannel.setOption(
						StandardSocketOptions.SO_KEEPALIVE, true);

				ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

				// connect this channel's socket
				asyncSocketChannel.connect(new InetSocketAddress(IP,
						DEFAULT_PORT), null, new StreamAcceptorClientHandler(
						asyncSocketChannel));

				System.in.read();

			} else {
				System.out
						.println("The asynchronous socket channel cannot be opened!");
			}

		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}
