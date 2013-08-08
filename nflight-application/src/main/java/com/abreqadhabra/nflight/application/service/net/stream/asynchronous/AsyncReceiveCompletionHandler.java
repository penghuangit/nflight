package com.abreqadhabra.nflight.application.service.net.stream.asynchronous;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.server.net.socket.NetworkChannelHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class AsyncReceiveCompletionHandler
		implements
			CompletionHandler<AsynchronousSocketChannel, Void> {

	private static final Class<AsyncReceiveCompletionHandler> THIS_CLAZZ = AsyncReceiveCompletionHandler.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	private final Configure configure;
	private final AsynchronousServerSocketChannel asyncServerSocketChannel;

	public AsyncReceiveCompletionHandler(final Configure configure,
			final AsynchronousServerSocketChannel asyncServerSocketChannel) {
		this.configure = configure;
		this.asyncServerSocketChannel = asyncServerSocketChannel;
	}

	@Override
	public void completed(final AsynchronousSocketChannel result,
			final Void attachment) {
		this.asyncServerSocketChannel.accept(null, this);

		this.receive(result);
	}

	private void receive(final AsynchronousSocketChannel socket) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			final int capacity = this.configure
					.getInt(Configure.ASYNC_INCOMING_BUFFER_CAPACITY);
			final ByteBuffer incomingByteBuffer = NetworkChannelHelper
					.getByteBuffer(capacity);

			final Integer numRead = socket.read(incomingByteBuffer).get();
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
							+ socket.getRemoteAddress());

		} catch (InterruptedException | ExecutionException | IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void failed(final Throwable exc, final Void attachment) {
		this.asyncServerSocketChannel.accept(null, this);

		throw new UnsupportedOperationException("Cannot accept connections!");
	}

}
