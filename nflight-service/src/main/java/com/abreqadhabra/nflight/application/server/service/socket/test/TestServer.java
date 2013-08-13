package com.abreqadhabra.nflight.application.server.service.socket.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.abreqadhabra.nflight.application.server.service.socket.tcp.common.Message;

public class TestServer {

	public static void main(String[] args) throws ClassNotFoundException {

		int DEFAULT_PORT = 5555;

		TestServer server = new TestServer();
		server.startupTestServer(DEFAULT_PORT);

	}

	private void startupTestServer(int port) {

		// open Selector and ServerSocketChannel by calling the open() method
		try (Selector selector = Selector.open();
				ServerSocketChannel ssc = ServerSocketChannel.open()) {

			// StreamHelper.bind(selector, ssc, port);

			while (true) {
				// wait for incomming events
				try {
					selector.select();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// there is something to process on selected keys
				Iterator<SelectionKey> keys = selector.selectedKeys()
						.iterator();
				while (keys.hasNext()) {
					SelectionKey key = keys.next();
					// prevent the same key from coming up again
					keys.remove();
					this.processSelectionKey(key, selector);
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	// isAcceptable returned true
	private void accept(SelectionKey key, Selector selector)
			throws IOException {

		ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
		SocketChannel sc = ssc.accept();
		sc.configureBlocking(false);

		System.out
				.println("Incoming connection from: " + sc.getRemoteAddress());

		// write an welcome message
		sc.write(ByteBuffer.wrap("Hello!\n".getBytes("UTF-8")));

		// register channel with selector for further I/O
		// keepDataTrack.put(socketChannel, new ArrayList<byte[]>());
		sc.register(selector, SelectionKey.OP_READ);
	}

	public void processSelectionKey(SelectionKey key,
			Selector selector) {
		// Since the ready operations are cumulative,
		// need to check readiness for each operation

		try {
			// Get channel with connection request
			if (key.isConnectable()) {
				System.out.println("isConnectable");
				SocketChannel sc = (SocketChannel) key.channel();
				boolean success = sc.finishConnect();
				if (!success) {
					// An error occurred; handle it
					// Unregister the channel with this selector
					key.cancel();
				}
			}
			if (key.isAcceptable()) {
				System.out.println("isAcceptable");
				key.channel();
				this.accept(key, selector);
			}
			if (key.isReadable()) {
				System.out.println("isReadable");
				// Get channel with bytes to read
				this.read(key);
				// See Reading from a SocketChannel
			}
			if (key.isValid() && key.isWritable()) {
				System.out.println("isWritable");
				this.write(key, new Message("write"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// isWritable returned true
	public void write(SelectionKey key, Object object) {
		SocketChannel sc = (SocketChannel) key.channel();
		StreamHelper.write(sc, object);
		key.interestOps(SelectionKey.OP_WRITE);
	}

	// isReadable returned true
	public void read(SelectionKey key) {
		try {
			SocketChannel sc = (SocketChannel) key.channel();
			StreamHelper.read(sc);
			key.interestOps(SelectionKey.OP_READ);
		} catch (IOException e) {
			// Handle error with channel and unregister
			key.cancel();
			e.printStackTrace();
		}
	}
}
