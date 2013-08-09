package com.abreqadhabra.nflight.application.server.service.socket.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TestClient {

	public static void main(final String[] args) throws UnknownHostException {

		final String DEFAULT_ADDRESS = InetAddress.getLocalHost()
				.getHostAddress();
		final int DEFAULT_PORT = 5555;
		final TestClient client = new TestClient();
		client.startupTestClient(DEFAULT_ADDRESS, DEFAULT_PORT);

	}

	private void startupTestClient(final String address, final int port) {

		// open Selector and ServerSocketChannel by calling the open() method
		try (Selector selector = Selector.open();
				SocketChannel sc = SocketChannel.open()) {

			StreamHelper.connect(selector, sc, address, port);

			// waiting for the connection
			while (selector.select(1000) > 0) {
				// get keys
				final Set<SelectionKey> keys = selector.selectedKeys();
				final Iterator<SelectionKey> its = keys.iterator();
				// process each key
				while (its.hasNext()) {
					final SelectionKey key = its.next();
					// remove the current key

					its.remove();
					
				      if (!key.isValid()) {
				            continue;
				          }

				          // Check what event is available and deal with it
				          if (key.isConnectable()) {
				            this.connect(key);
				          } else if (key.isReadable()) {
				            this.read(key);
				          } else if (key.isWritable()) {
				            this.write(key);
				          }
				}
				
				
			}

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private void write(final SelectionKey key) {
		System.out.println("Write");
	}

	private void connect(final SelectionKey key) {
		System.out.println("Connect");

	}

	private void read(final SelectionKey key) {
		System.out.println("Read");
	}
}
