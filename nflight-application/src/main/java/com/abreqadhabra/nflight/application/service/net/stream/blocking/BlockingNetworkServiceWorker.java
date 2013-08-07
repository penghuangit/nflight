package com.abreqadhabra.nflight.application.service.net.stream.blocking;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.net.socket.NetworkChannelHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class BlockingNetworkServiceWorker implements Runnable {
	private static final Class<BlockingNetworkServiceWorker> THIS_CLAZZ = BlockingNetworkServiceWorker.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private final SocketChannel socket;

	public BlockingNetworkServiceWorker(final SocketChannel socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			final long startN = System.nanoTime();

			final Thread t = Thread.currentThread();
			final String name = "nflight-" + t.getName();
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					name);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "isOpen=" +
					Boolean.toString(this.socket.isOpen()) +", isConnected=" +
					Boolean.toString(this.socket.isConnected()));

			final ByteBuffer dst = NetworkChannelHelper.getByteBuffer(1024);

			final int numRead = this.socket.read(dst);

			final byte[] data = new byte[numRead];
			System.arraycopy(dst.array(), 0, data, 0, numRead);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					new String(data, "UTF-8") + " [" + numRead
							+ " bytes] from " + this.socket.getRemoteAddress());

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
