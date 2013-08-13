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

	public static void main(String[] args) throws UnknownHostException {

		String DEFAULT_ADDRESS = InetAddress.getLocalHost()
				.getHostAddress();
		int DEFAULT_PORT = 5555;
		TestClient client = new TestClient();
		client.startupTestClient(DEFAULT_ADDRESS, DEFAULT_PORT);

	}

	private void startupTestClient(String address, int port) {

		// open Selector and ServerSocketChannel by calling the open() method
		try (Selector selector = Selector.open();
				SocketChannel sc = SocketChannel.open()) {

			StreamHelper.connect(selector, sc, address, port);

			// waiting for the connection
			while (selector.select(1000) > 0) {
				// get keys
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> its = keys.iterator();
				// process each key
				while (its.hasNext()) {
					SelectionKey key = its.next();
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

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void write(SelectionKey key) {
		System.out.println("Write");
	}

	private void connect(SelectionKey key) {
		System.out.println("Connect");

	}

	private void read(SelectionKey key) {
		System.out.println("Read");
	}
}
