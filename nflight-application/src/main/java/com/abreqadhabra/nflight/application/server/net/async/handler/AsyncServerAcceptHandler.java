package com.abreqadhabra.nflight.application.server.net.async.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.server.net.async.AsyncServerImpl;
import com.abreqadhabra.nflight.application.server.net.socket.MessageDTO;
import com.abreqadhabra.nflight.application.server.net.socket.MessageDTOImpl;
import com.abreqadhabra.nflight.application.server.net.socket.NetworkChannelHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class AsyncServerAcceptHandler
		implements
			CompletionHandler<AsynchronousSocketChannel, Void> {
	private static final Class<AsyncServerAcceptHandler> THIS_CLAZZ = AsyncServerAcceptHandler.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	Configure configure;
	AsynchronousServerSocketChannel asyncServerSocketChannel;
	AsyncServerImpl asyncServerImpl;

	public AsyncServerAcceptHandler(Configure configure,
			AsynchronousServerSocketChannel asyncServerSocketChannel,
			AsyncServerImpl asyncServerImpl) {
		this.asyncServerSocketChannel = asyncServerSocketChannel;
		this.configure = configure;
		this.asyncServerImpl = asyncServerImpl;
	}

	@Override
	public void completed(AsynchronousSocketChannel result, Void attachment) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		asyncServerSocketChannel.accept(null, this);

		read(result);

	}

	private void read(AsynchronousSocketChannel result) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {

			int capacity = this.configure
					.getInt(Configure.ACCEPTOR_READ_BUFFER_CAPACITY);
			ByteBuffer readByteBuffer = NetworkChannelHelper
					.getByteBuffer(capacity);

			while (result.read(readByteBuffer).get() != -1) {
				readByteBuffer.flip();

				if (readByteBuffer.hasRemaining()) {
					readByteBuffer.compact();
				} else {
					readByteBuffer.clear();
				}

			}

			LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"Incoming connection from: " + result.getRemoteAddress()
							+ "[" + readByteBuffer.array().length + " bytes]");

			Object deserializedObject = NetworkChannelHelper
					.deserializeObject(readByteBuffer);

			LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"deserializeObject: " + deserializedObject);

			MessageDTOImpl messageDTO = null;

			if (deserializedObject instanceof MessageDTO) {
				messageDTO = (MessageDTOImpl) deserializedObject;
			}

			LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"messageDTO: " + messageDTO);

		} catch (InterruptedException | ExecutionException | IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void failed(Throwable exc, Void attachment) {
		asyncServerSocketChannel.accept(null, this);

		throw new UnsupportedOperationException("Cannot accept connections!");
	}

}
