package com.abreqadhabra.nflight.application.service.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.NetworkChannel;
import java.nio.channels.SelectionKey;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ServerSocketFactory;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class NetworkServiceHelper {
	private static final Class<NetworkServiceHelper> THIS_CLAZZ = NetworkServiceHelper.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private static final Configure configure = new ConfigureImpl(
			Configure.FILE_SOCKET_OPTION_PROPERTIES);

	public static void setChannelOption(final NetworkChannel socketChannel) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			final Set<SocketOption<?>> options = socketChannel
					.supportedOptions();

			LOGGER.logp(Level.FINER, CLAZZ_NAME, METHOD_NAME, "소켓 채널의 지원 옵션:  "
					+ options);

			final StringBuffer sb = new StringBuffer(socketChannel + ": ");

			for (final SocketOption<?> option : options) {
				final String optionName = option.name();
				final String optionValue = configure
						.get(Configure.PREFIX_KEY_PROPERTIES_SOCKET_OPTION
								+ optionName.toLowerCase().trim());
				if (optionValue == null) {
					continue;
				}
				if (option.type() == Integer.class) {
					final SocketOption<Integer> stdSocketOption = (SocketOption<Integer>) option;
					socketChannel.setOption(stdSocketOption,
							Integer.parseInt(optionValue));
					sb.append(optionName
							+ "="
							+ socketChannel.getOption(stdSocketOption)
									.toString());
				} else if (option.type() == Boolean.class) {
					final SocketOption<Boolean> stdSocketOption = (SocketOption<Boolean>) option;
					socketChannel.setOption(stdSocketOption,
							Boolean.parseBoolean(optionValue));
					sb.append(optionName
							+ "="
							+ socketChannel.getOption(stdSocketOption)
									.toString());
				}
				sb.append(",");
			}

			LOGGER.logp(Level.CONFIG, CLAZZ_NAME, METHOD_NAME, sb.toString());

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static ByteBuffer getByteBuffer(final int capacity) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"Allocates a new byte buffer: " + capacity + " bytes");

		return ByteBuffer.allocate(capacity);
	}

	public static ByteBuffer serializeObject(Object object) {
		byte[] bytes = null;
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(
						byteArrayOutputStream);) {

			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
			bytes = byteArrayOutputStream.toByteArray();

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"serializeObject: " + object.getClass().getName() + " ["
							+ bytes.length + " bytes] " + object);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return ByteBuffer.wrap(bytes);
	}
	public static Object deserializeObject(final ByteBuffer byteBuffer) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Object readObject = null;
		byte[] bytes = byteBuffer.array();

		try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				ObjectInputStream ois = new ObjectInputStream(bais);) {
			readObject = ois.readObject();

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"deserializeObject: " + readObject.getClass().getName()
							+ " " + readObject);

		} catch (ClassNotFoundException | IOException e) {
			if (e instanceof StreamCorruptedException) {
				return null;
			} else {
				e.printStackTrace();
			}
		}

		return readObject;
	}

	public static String getReadySetString(SelectionKey selectionKey) {
		return " [ selectionKey: " + selectionKey.hashCode()
				+ " interest ops: {"
				+ getOperationSetString(selectionKey.interestOps())
				+ " }; ready ops: {"
				+ getOperationSetString(selectionKey.readyOps()) + " } ]";
	}

	private static String getOperationSetString(final int ops) {
		final StringBuffer sb = new StringBuffer();
		sb.append(((ops & SelectionKey.OP_ACCEPT) != 0 ? " OP_ACCEPT" : ""));
		sb.append(((ops & SelectionKey.OP_CONNECT) != 0 ? " OP_CONNECT" : ""));
		sb.append(((ops & SelectionKey.OP_READ) != 0 ? " OP_READ" : ""));
		sb.append(((ops & SelectionKey.OP_WRITE) != 0 ? " OP_WRITE" : ""));

		return sb.toString();
	}

	public static int findAvailableServerSocket(int seed) {
		for (int i = seed; i < seed + 200; i++) {
			try {
				ServerSocket sock = ServerSocketFactory.getDefault()
						.createServerSocket(i);
				sock.close();
				return i;
			} catch (Exception e) {
			}
		}
		throw new RuntimeException("Cannot find a free server socket");
	}

	public static int findAvailableServerSocket() {
		return findAvailableServerSocket(5678);
	}

	public static int findAvailableUdpSocket(int seed) {
		for (int i = seed; i < seed + 200; i++) {
			try {
				DatagramSocket sock = new DatagramSocket(i);
				sock.close();
				Thread.sleep(100);
				return i;
			} catch (Exception e) {
			}
		}
		throw new RuntimeException("Cannot find a free server socket");
	}


}
