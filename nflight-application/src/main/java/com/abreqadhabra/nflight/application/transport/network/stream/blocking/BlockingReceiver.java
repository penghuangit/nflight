package com.abreqadhabra.nflight.application.transport.network.stream.blocking;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.transport.network.NetworkServiceHelper;
import com.abreqadhabra.nflight.common.launcher.Configure;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class BlockingReceiver implements Runnable {
	private static Class<BlockingReceiver> THIS_CLAZZ = BlockingReceiver.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private Configure configure;
	private SocketChannel socket;

	public BlockingReceiver(Configure configure,
			SocketChannel socket) {
		this.configure = configure;
		this.socket = socket;
	}

	@Override
	public void run() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			long startN = System.nanoTime();

			if (LOGGER.isLoggable(Level.FINER)) {
				String currentThreadName = Thread.currentThread()
						.getName();
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, "current thread is " + currentThreadName);
			}

			int capacity = this.configure
					.getInt(Configure.BLOCKING_INCOMING_BUFFER_CAPACITY);
			ByteBuffer incomingByteBuffer = NetworkServiceHelper
					.getByteBuffer(capacity);
			int numRead = this.socket.read(incomingByteBuffer);
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

			long endN = System.nanoTime();
			long diffN = endN - startN;

			double elapsedTime = diffN / 1.0E09;

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"elapsed: " + Double.toString(elapsedTime) + " seconds");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				this.socket.close();
				LOGGER.logp(
						Level.FINER,
						THIS_CLAZZ.getSimpleName(),
						METHOD_NAME,
						"isOpen=" + Boolean.toString(this.socket.isOpen())
								+ ", isConnected="
								+ Boolean.toString(this.socket.isConnected()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
