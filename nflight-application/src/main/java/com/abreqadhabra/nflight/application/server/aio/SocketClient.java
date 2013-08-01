package com.abreqadhabra.nflight.application.server.aio;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

public class SocketClient {

	public static void main(String[] args) throws Exception {

		for (int i = 0; i < 20; i++) {
			AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
			Future<?> future = client.connect(new InetSocketAddress(InetAddress
					.getLocalHost().getHostAddress(), 9999));
			future.get();
			client.write(ByteBuffer.wrap(((i + 1) + ": Hello !").getBytes()))
					.get();
			client.close();
			Thread.sleep(2000);
		}

	}

}
