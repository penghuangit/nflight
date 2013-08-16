package com.abreqadhabra.nflight.application.server.net.socket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.NetworkChannel;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class NetworkChannelHelper {
	private static Class<NetworkChannelHelper> THIS_CLAZZ = NetworkChannelHelper.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private static Configure configure = new ConfigureImpl(THIS_CLAZZ,
			Configure.FILE_CHANNEL_OPTION_PROPERTIES);

	public static void setChannelOption(NetworkChannel socketChannel) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			Set<SocketOption<?>> options = socketChannel
					.supportedOptions();

			LOGGER.logp(Level.FINER, CLAZZ_NAME, METHOD_NAME, "소켓 채널의 지원 옵션:  "
					+ options);

			StringBuffer sb = new StringBuffer(socketChannel + ": ");

			for (SocketOption<?> option : options) {
				String optionName = option.name();
				String optionValue = configure
						.get(Configure.PREFIX_KEY_PROPERTIES_CHANNEL_OPTION
								+ optionName.toLowerCase().trim());
				if (optionValue == null) {
					continue;
				}
				if (option.type() == Integer.class) {
					SocketOption<Integer> stdSocketOption = (SocketOption<Integer>) option;
					socketChannel.setOption(stdSocketOption,
							Integer.parseInt(optionValue));
					sb.append(optionName
							+ "="
							+ socketChannel.getOption(stdSocketOption)
									.toString());
				} else if (option.type() == Boolean.class) {
					SocketOption<Boolean> stdSocketOption = (SocketOption<Boolean>) option;
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

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ByteBuffer getByteBuffer(int capacity) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"Allocates a new byte buffer: " + capacity + " bytes");

		return ByteBuffer.allocate(capacity);
	}

	public static ByteBuffer serializeObject(Object object) {
		byte[] bytes = null;
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
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
	public static Object deserializeObject(ByteBuffer byteBuffer) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Object readObject = null;
		byte[] bytes = byteBuffer.array();

		try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				ObjectInputStream ois = new ObjectInputStream(bais);) {
			readObject = ois.readObject();

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"deserializeObject: " + readObject.getClass().getName() + " " + readObject);
			
		} catch (ClassNotFoundException | IOException e) {
			if (e instanceof StreamCorruptedException) {
				return null;
			} else {
				e.printStackTrace();
			}
		}

		return readObject;
	}
}
