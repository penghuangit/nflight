package com.abreqadhabra.nflight.application.service.net.stream.blocking;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.server.net.socket.NetworkChannelHelper;
import com.abreqadhabra.nflight.application.service.net.NetworkServiceHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class BlockingNetworkServiceWorker implements Runnable {
	private static final Class<BlockingNetworkServiceWorker> THIS_CLAZZ = BlockingNetworkServiceWorker.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private Configure configure;
	private final SocketChannel socket;

	public BlockingNetworkServiceWorker(Configure configure, final SocketChannel socket) {
		this.configure = configure;
		this.socket = socket;
	}

	@Override
	public void run() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			final long startN = System.nanoTime();

			if (LOGGER.isLoggable(Level.FINER)) {
				String currentThreadName = Thread.currentThread().getName();
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, "current thread is "
								+ currentThreadName);
			}
			
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "isOpen=" +
					Boolean.toString(this.socket.isOpen()) +", isConnected=" +
					Boolean.toString(this.socket.isConnected()));

			int capacity = configure
					.getInt(Configure.NONBLOCKING_INCOMING_BUFFER_CAPACITY);
			ByteBuffer incomingByteBuffer = NetworkServiceHelper.getByteBuffer(capacity);
			int numRead = socket.read(incomingByteBuffer);
			incomingByteBuffer.flip();
			if (incomingByteBuffer.hasRemaining()) {
				incomingByteBuffer.compact();
			} else {
				incomingByteBuffer.clear();
			}

			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getSimpleName(),
					METHOD_NAME,
					new String(incomingByteBuffer.array(), "UTF-8") + " ["
							+ numRead + " bytes] from "
							+ this.socket.getRemoteAddress());

			final long endN = System.nanoTime();
			final long diffN = endN - startN;

			final double elapsedTime = diffN / 1.0E09;

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"elapsed: " + Double.toString(elapsedTime) + " seconds");

		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			try {
				this.socket.close();
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "isOpen=" +
						Boolean.toString(this.socket.isOpen()) +", isConnected=" +
						Boolean.toString(this.socket.isConnected()));
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}
}
