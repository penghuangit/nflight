package com.abreqadhabra.nflight.application.service.net.stream;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class BlockingNetworkClientTest {

	public static void main(final String[] args) throws UnknownHostException {

		InetAddress DEFAULT_ADDRESS = InetAddress.getLocalHost();
		int DEFAULT_PORT = 9999;
		InetSocketAddress socketAddress = new InetSocketAddress(
				DEFAULT_ADDRESS, DEFAULT_PORT);
		try {


			for (int i = 0; i < 50000; i++) {
				SocketChannel channel = SocketChannel.open();
				channel.socket().connect(socketAddress);
				channel.write(ByteBuffer.wrap(Integer.toString(i).getBytes()));
				Thread.sleep(10);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
