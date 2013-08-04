package com.abreqadhabra.nflight.application.server.net.socket;

import java.io.IOException;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.NetworkChannel;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class SocketChannelHelper {
	private static final Class<SocketChannelHelper> THIS_CLAZZ = SocketChannelHelper.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private static final Configure configure = new ConfigureImpl(
			Configure.FILE_SOCKET_OPTION_PROPERTIES);

	public static void setSocketChannelOption(final NetworkChannel socketChannel) {
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

	public static ByteBuffer getByteBuffer(final String capacity) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"Allocates a new byte buffer :" + capacity + " bytes");

		return ByteBuffer.allocate(Integer.parseInt(capacity));
	}
}
