package com.abreqadhabra.nflight.application.service.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.NetworkChannel;
import java.nio.channels.SelectionKey;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ServerSocketFactory;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.launcher.Configure.STREAM_SERVICE_TYPE;
import com.abreqadhabra.nflight.application.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class NetworkServiceHelper {
	private static final Class<NetworkServiceHelper> THIS_CLAZZ = NetworkServiceHelper.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private static final Configure configure = new ConfigureImpl(
			Configure.FILE_CHANNEL_OPTION_PROPERTIES);

	public static void setMulticastChannelOption(DatagramChannel socketChannel,
			String networkInterfaceName, STREAM_SERVICE_TYPE type) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		configure.set(Configure.CHANNEL_OPTION_IP_MULTICAST_IF,
				networkInterfaceName);

		LOGGER.logp(
				Level.FINER,
				CLAZZ_NAME,
				METHOD_NAME,
				Configure.CHANNEL_OPTION_IP_MULTICAST_IF
						+ ": "
						+ configure
								.get(Configure.CHANNEL_OPTION_IP_MULTICAST_IF));

		setChannelOption(socketChannel, type);

	}

	public static void setChannelOption(final NetworkChannel socketChannel,
			STREAM_SERVICE_TYPE type) {
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

				String optionKey = Configure.PREFIX_KEY_PROPERTIES_CHANNEL_OPTION
						+ type.toString() + "." + optionName.toLowerCase();

				LOGGER.logp(Level.FINER, CLAZZ_NAME, METHOD_NAME,
						"optionKey:  " + optionKey);

				final String optionValue = configure.get(optionKey);
				if (optionValue == null || optionValue.length() == 0) {
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
				} else if (option.type() == NetworkInterface.class) {
					final SocketOption<NetworkInterface> stdSocketOption = (SocketOption<NetworkInterface>) option;

					// join multicast group on this interface, and also use this
					// interface for outgoing multicast datagrams
					// get the network interface used for multicast
					final NetworkInterface networkInterface = NetworkInterface
							.getByName(optionValue);

					socketChannel.setOption(stdSocketOption, networkInterface);
					sb.append(optionName
							+ "="
							+ socketChannel.getOption(stdSocketOption)
									.toString());
				} else {

					LOGGER.logp(Level.FINER, CLAZZ_NAME, METHOD_NAME,
							"option.type():  " + option.type());
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

	public static String getNetworkInterfaceName(final String localAddress)
			throws SocketException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		String networkInterfaceName = null;
		StringBuffer sb = new StringBuffer();
		Enumeration<NetworkInterface> nets = NetworkInterface
				.getNetworkInterfaces();
		for (NetworkInterface netint : Collections.list(nets)) {
			Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
			if (LOGGER.isLoggable(Level.FINER)) {
				sb.append("\n" + netint.getName() + ": "
						+ netint.getDisplayName());
			}
			for (InetAddress inetAddress : Collections.list(inetAddresses)) {
				if (LOGGER.isLoggable(Level.FINER)) {
					sb.append("\n\tInetAddress=" + inetAddress.toString());
				}
				if (localAddress.equals(inetAddress.getHostAddress())) {
					networkInterfaceName = netint.getName();

				}
			}
		}

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				sb.toString());

		LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getName(), METHOD_NAME,
				"Network Interface Name is " + networkInterfaceName);

		return networkInterfaceName;
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

	public static boolean isAvailableStreamPort(int seed) {
		try {
			ServerSocket sock = ServerSocketFactory.getDefault()
					.createServerSocket(seed);
			sock.close();
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean isAvailableDatagramPort(int seed) {
		try {
			DatagramSocket sock = new DatagramSocket(seed);
			sock.close();
			Thread.sleep(100);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static int findAvailableStreamPort() {
		return findAvailableStreamPort(5678);
	}

	public static int findAvailableDatagramPort() {
		return findAvailableDatagramPort(5678);
	}

	public static int findAvailableStreamPort(int seed) {
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

	public static int findAvailableDatagramPort(int seed) {
		for (int i = seed; i < seed + 200; i++) {
			try {
				DatagramSocket sock = new DatagramSocket(i);
				sock.close();
				Thread.sleep(100);
				return i;
			} catch (Exception e) {
			}
		}
		throw new RuntimeException(
				" already in use port: Cannot find a free server socket");
	}

	public static int validatedStreamPort(int port) {

		if (!isAvailableStreamPort(port)) {
			port = extracted(port);
		}
		return port;
	}

	private static int extracted(int port) {
		return NetworkServiceHelper.findAvailableStreamPort();
	}

}
