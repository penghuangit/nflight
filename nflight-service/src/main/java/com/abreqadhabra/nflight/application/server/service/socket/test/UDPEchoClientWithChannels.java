package com.abreqadhabra.nflight.application.server.service.socket.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public class UDPEchoClientWithChannels {

	public static int DEFAULT_PORT = 5555;

	private static int LIMIT = 100;

	public static void main(String[] args) throws Exception {

		int port = DEFAULT_PORT;

		InetAddress DEFAULT_ADDRESS = InetAddress.getLocalHost();


		SocketAddress remote= new InetSocketAddress(DEFAULT_ADDRESS.getHostAddress(), port);

		

		try {

			DatagramChannel channel = DatagramChannel.open();

			channel.configureBlocking(false);

			channel.connect(remote);

			Selector selector = Selector.open();

			channel.register(selector, SelectionKey.OP_READ
					| SelectionKey.OP_WRITE);

			ByteBuffer buffer = ByteBuffer.allocate(4);

			int n = 0;

			int numbersRead = 0;

			while (true) {

				// wait one minute for a connection

				selector.select(60000);

				Set readyKeys = selector.selectedKeys();

				if (readyKeys.isEmpty() && n == LIMIT) {

					// All packets have been written and it doesn't look like
					// any

					// more are will arrive from the network

					break;

				}

				else {

					Iterator iterator = readyKeys.iterator();

					while (iterator.hasNext()) {

						SelectionKey key = (SelectionKey) iterator.next();

						iterator.remove();

						if (key.isReadable()) {

							buffer.clear();

							channel.read(buffer);

							buffer.flip();

							int echo = buffer.getInt();

							System.out.println("Read: " + echo);

							numbersRead++;

						}

						if (key.isWritable()) {

							buffer.clear();

							buffer.putInt(n);

							buffer.flip();

							channel.write(buffer);

							System.out.println("Wrote: " + n);

							n++;

							if (n == LIMIT) {

								// All packets have been written; switch to
								// read-only mode

								key.interestOps(SelectionKey.OP_READ);

							} // end if

						} // end while

					} // end else

				} // end while

			} // end while

			System.out.println("Echoed " + numbersRead + " out of " + LIMIT +

			" sent");

			System.out.println("Success rate: " + 100.0 * numbersRead / LIMIT +

			"%");

		} // end try

		catch (IOException ex) {

			System.err.println(ex);

		} // end catch

	} // end main

}
