package com.abreqadhabra.nflight.application.server.net.async.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.server.net.async.AsyncServerImpl;
import com.abreqadhabra.nflight.application.server.net.async.logic.IBusinessLogic;
import com.abreqadhabra.nflight.application.server.net.socket.MessageDTO;
import com.abreqadhabra.nflight.application.server.net.socket.MessageDTOImpl;
import com.abreqadhabra.nflight.application.server.net.socket.NetworkChannelHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.dao.dto.Airline;

public class AsyncServerAcceptHandler
		implements
			CompletionHandler<AsynchronousSocketChannel, Void> {
	private static final Class<AsyncServerAcceptHandler> THIS_CLAZZ = AsyncServerAcceptHandler.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	Configure configure;
	IBusinessLogic logic;
	AsynchronousServerSocketChannel asyncServerSocketChannel;
	AsyncServerImpl asyncServerImpl;

	public AsyncServerAcceptHandler(final Configure configure,
			final IBusinessLogic logic,
			final AsynchronousServerSocketChannel asyncServerSocketChannel,
			final AsyncServerImpl asyncServerImpl) {
		this.configure = configure;
		this.logic = logic;
		this.asyncServerSocketChannel = asyncServerSocketChannel;
		this.asyncServerImpl = asyncServerImpl;
	}

	@Override
	public void completed(final AsynchronousSocketChannel result,
			final Void attachment) {
		Thread.currentThread().getStackTrace()[1].getMethodName();

		this.asyncServerSocketChannel.accept(null, this);

		this.receive(result);

	}

	private void receive(final AsynchronousSocketChannel asyncSocketChannel) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {

			final int capacity = this.configure
					.getInt(Configure.ACCEPTOR_READ_BUFFER_CAPACITY);
			final ByteBuffer readByteBuffer = NetworkChannelHelper
					.getByteBuffer(capacity);

			while (asyncSocketChannel.read(readByteBuffer).get() != -1) {
				readByteBuffer.flip();

				if (readByteBuffer.hasRemaining()) {
					readByteBuffer.compact();
				} else {
					readByteBuffer.clear();
				}

			}

			LOGGER.logp(
					Level.INFO,
					THIS_CLAZZ.getSimpleName(),
					METHOD_NAME,
					"Incoming connection from: "
							+ asyncSocketChannel.getRemoteAddress() + "["
							+ readByteBuffer.array().length + " bytes]");

			final Object deserializedObject = NetworkChannelHelper
					.deserializeObject(readByteBuffer);

			LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"deserializeObject: " + deserializedObject);

			MessageDTO receiveMessage = null;

			if (deserializedObject instanceof MessageDTO) {
				receiveMessage = (MessageDTOImpl) deserializedObject;
			}

			LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"messageDTO: " + receiveMessage);

			// 로직데이터 echo 테스트
			try {
				final Airline[] airlines = this.logic.getAirlines();

				
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
						"dataObject: " + Arrays.toString(airlines));
				
				final MessageDTO sendMessage = new MessageDTOImpl();

				sendMessage.setAirlines(airlines);

				this.send(asyncSocketChannel, sendMessage);

			} catch (final Exception e) {
				e.printStackTrace();
			}

		} catch (InterruptedException | ExecutionException | IOException e) {
			e.printStackTrace();
		}

	}

	private void send(final AsynchronousSocketChannel asyncSocketChannel,
			final MessageDTO sendMessage) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		final ByteBuffer src = NetworkChannelHelper
				.serializeObject(sendMessage);

		try {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"Complete sending the data to the remote: " + asyncSocketChannel.getRemoteAddress());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		asyncSocketChannel.write(src, null, new AsyncServerSendHandler());

	}

	@Override
	public void failed(final Throwable exc, final Void attachment) {
		this.asyncServerSocketChannel.accept(null, this);

		throw new UnsupportedOperationException("Cannot accept connections!");
	}

}
