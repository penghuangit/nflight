package com.abreqadhabra.nflight.application.server.service.socket.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.abreqadhabra.nflight.application.server.service.socket.tcp.common.Message;

public class StreamHelper {

	// isWritable returned true
	public static void write(SocketChannel sc, Object object) {
		try {
			ByteBuffer byteBufferWrite = StreamHelper.serializeObject(object);
			sc.write(byteBufferWrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void read(SocketChannel sc) throws IOException {
		ByteBuffer totalByteBuffer = null;

		totalByteBuffer = ByteBuffer.allocate(1024 * 1024);
		final ByteBuffer byteBufferRead = ByteBuffer.allocate(1024 * 2);
		final int byteCount = sc.read(byteBufferRead);
		// byteCount = -1 it's mean end of stream
		if (byteCount == -1) {
			// No more bytes can be read from the channel
			String remoteAddress = sc.getRemoteAddress().toString();
			sc.close();
			throw new IOException("Connection closed by: " + remoteAddress);
		} else {
			// To read the byteBufferRead, flip the buffer
			byteBufferRead.flip();
			totalByteBuffer.put(byteBufferRead);
			// Clear the buffer and read bytes from socket
			byteBufferRead.clear();
			// Read the bytes from the buffer ...;
			// see Getting Bytes from a ByteBuffer
			// To read the totalByteBuffer, flip the buffer
			totalByteBuffer.flip();
			byte[] bytes = new byte[totalByteBuffer.remaining()];
			totalByteBuffer.get(bytes, 0, bytes.length);

			System.out.println(bytes.length + " bytes from "
					+ sc.getRemoteAddress());

			final Object readObject = deserializeObject(bytes);
			Message messageObject = null;
			if (readObject instanceof Message) {
				messageObject = (Message) readObject;
				System.out.println(messageObject.getClass().getName() + " : "
						+ messageObject);
			} else {

			}
		}
	}

	public static ByteBuffer serializeObject(Object object) {
		byte[] bytes = null;
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(
						byteArrayOutputStream);) {
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
			bytes = byteArrayOutputStream.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return ByteBuffer.wrap(bytes);
	}

	private static Object deserializeObject(final byte[] bytes) {
		Object readObject = null;
		try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				ObjectInputStream ois = new ObjectInputStream(bais);) {
			readObject = ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		return readObject;
	}



	public static boolean connect(Selector selector, SocketChannel sc,
			String address, int port) {
		
		boolean isConnected = false;
		
		// check that both of them were successfully opened
		if ((sc.isOpen()) && (selector.isOpen())) {
			try {
				// configure non-blocking mode
				sc.configureBlocking(false);

				// set some options
				sc.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
				sc.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
				sc.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

				// register the current channel with the given selector
				sc.register(selector, SelectionKey.OP_CONNECT);

				// connect to remote host
				sc.connect(new java.net.InetSocketAddress(address, port));

				System.out.println("Localhost: " + sc.getLocalAddress());

				isConnected = true;

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out
					.println("The socket channel or selector cannot be opened!");
		}
		
		return isConnected;
	}
}
