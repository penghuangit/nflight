package com.abreqadhabra.nflight.app.server.service.net.sample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Apress
 */
public class SocketServer05 {

	public static void main(final String[] args) {
		final SocketServer05 main = new SocketServer05();
		main.startEchoServer();
	}

	private final Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<>();

	private final ByteBuffer buffer = ByteBuffer.allocate(2 * 1024);

	// isAcceptable returned true
	private void acceptOP(final SelectionKey key, final Selector selector)
			throws IOException {

		final ServerSocketChannel serverChannel = (ServerSocketChannel) key
				.channel();
		final SocketChannel socketChannel = serverChannel.accept();
		socketChannel.configureBlocking(false);

		System.out.println("Incoming connection from: "
				+ socketChannel.getRemoteAddress());

		// write an welcome message
		socketChannel.write(ByteBuffer.wrap("Hello!\n".getBytes("UTF-8")));

		// register channel with selector for further I/O
		this.keepDataTrack.put(socketChannel, new ArrayList<byte[]>());
		socketChannel.register(selector, SelectionKey.OP_READ);
	}

	private void doEchoJob(final SelectionKey key, final byte[] data) {
		final SocketChannel socketChannel = (SocketChannel) key.channel();
		final List<byte[]> channelData = this.keepDataTrack.get(socketChannel);
		channelData.add(data);

		key.interestOps(SelectionKey.OP_WRITE);
	}

	// isReadable returned true
	private void readOP(final SelectionKey key) {
		try {
			final SocketChannel socketChannel = (SocketChannel) key.channel();

			this.buffer.clear();

			int numRead = -1;
			try {
				numRead = socketChannel.read(this.buffer);
			} catch (final IOException e) {
				System.err.println("Cannot read error!");
			}

			if (numRead == -1) {
				this.keepDataTrack.remove(socketChannel);
				System.out.println("Connection closed by: "
						+ socketChannel.getRemoteAddress());
				socketChannel.close();
				key.cancel();
				return;
			}

			final byte[] data = new byte[numRead];
			System.arraycopy(this.buffer.array(), 0, data, 0, numRead);
			System.out.println(new String(data, "UTF-8") + " from "
					+ socketChannel.getRemoteAddress());

			// write back to client
			this.doEchoJob(key, data);
		} catch (final IOException ex) {
			System.err.println(ex);
		}
	}

	private void startEchoServer() {

		final int DEFAULT_PORT = 5555;

		// open Selector and ServerSocketChannel by calling the open() method
		try (Selector selector = Selector.open();
				ServerSocketChannel serverSocketChannel = ServerSocketChannel
						.open()) {

			// check that both of them were successfully opened
			if ((serverSocketChannel.isOpen()) && (selector.isOpen())) {

				// configure non-blocking mode
				serverSocketChannel.configureBlocking(false);

				// set some options
				serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF,
						256 * 1024);
				serverSocketChannel.setOption(
						StandardSocketOptions.SO_REUSEADDR, true);

				// bind the server socket channel to port
				serverSocketChannel.bind(new InetSocketAddress(DEFAULT_PORT));

				// register the current channel with the given selector
				serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

				// display a waiting message while ... waiting!
				System.out.println("Waiting for connections ...");

				while (true) {
					// wait for incomming events
					selector.select();

					// there is something to process on selected keys
					final Iterator keys = selector.selectedKeys().iterator();

					while (keys.hasNext()) {
						final SelectionKey key = (SelectionKey) keys.next();

						// prevent the same key from coming up again
						keys.remove();

						if (!key.isValid()) {
							continue;
						}

						if (key.isAcceptable()) {
							this.acceptOP(key, selector);
						} else if (key.isReadable()) {
							this.readOP(key);
						} else if (key.isWritable()) {
							this.writeOP(key);
						}
					}
				}

			} else {
				System.out
						.println("The server socket channel or selector cannot be opened!");
			}
		} catch (final IOException ex) {
			System.err.println(ex);
		}
	}

	// isWritable returned true
	private void writeOP(final SelectionKey key) throws IOException {

		final SocketChannel socketChannel = (SocketChannel) key.channel();

		final List<byte[]> channelData = this.keepDataTrack.get(socketChannel);
		final Iterator<byte[]> its = channelData.iterator();

		while (its.hasNext()) {
			final byte[] it = its.next();
			its.remove();
			socketChannel.write(ByteBuffer.wrap(it));
		}

		key.interestOps(SelectionKey.OP_READ);
	}
}
