package com.abreqadhabra.nflight.application.trash_server.net.async.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.Config;
import com.abreqadhabra.nflight.application.trash_server.Configure;
import com.abreqadhabra.nflight.application.trash_server.net.async.AsyncServerImpl;
import com.abreqadhabra.nflight.application.trash_server.net.async.logic.IBusinessLogic;
import com.abreqadhabra.nflight.application.trash_server.net.socket.MessageDTO;
import com.abreqadhabra.nflight.application.trash_server.net.socket.MessageDTOImpl;
import com.abreqadhabra.nflight.application.trash_server.net.socket.NetworkChannelHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.dao.dto.Airline;

public class AsyncServerAcceptHandler
		implements
			CompletionHandler<AsynchronousSocketChannel, Void> {
	private static Class<AsyncServerAcceptHandler> THIS_CLAZZ = AsyncServerAcceptHandler.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	Configure configure;
	IBusinessLogic logic;
	AsynchronousServerSocketChannel asyncServerSocketChannel;
	AsyncServerImpl asyncServerImpl;

	public AsyncServerAcceptHandler(Configure configure, IBusinessLogic logic,
			AsynchronousServerSocketChannel asyncServerSocketChannel,
			AsyncServerImpl asyncServerImpl) {
		this.configure = configure;
		this.logic = logic;
		this.asyncServerSocketChannel = asyncServerSocketChannel;
		this.asyncServerImpl = asyncServerImpl;
	}

	@Override
	public void completed(AsynchronousSocketChannel result, Void attachment) {
		Thread.currentThread().getStackTrace()[1].getMethodName();

		this.asyncServerSocketChannel.accept(null, this);

		this.receive(result);

	}

	private void receive(AsynchronousSocketChannel asyncSocketChannel) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {

			int capacity = Config
					.getInt(Configure.ASYNC_INCOMING_BUFFER_CAPACITY);
			ByteBuffer readByteBuffer = NetworkChannelHelper
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

			Object deserializedObject = NetworkChannelHelper
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
				Airline[] airlines = this.logic.getAirlines();

				LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, "dataObject: " + Arrays.toString(airlines));

				MessageDTO sendMessage = new MessageDTOImpl();

				sendMessage.setAirlines(airlines);

				this.send(asyncSocketChannel, sendMessage);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (InterruptedException | ExecutionException | IOException e) {
			e.printStackTrace();
		}

	}

	private void send(AsynchronousSocketChannel asyncSocketChannel,
			MessageDTO sendMessage) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		ByteBuffer src = NetworkChannelHelper.serializeObject(sendMessage);

		try {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"Complete sending the data to the remote: "
							+ asyncSocketChannel.getRemoteAddress());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		asyncSocketChannel.write(src, null, new AsyncServerSendHandler());

	}

	@Override
	public void failed(Throwable exc, Void attachment) {
		this.asyncServerSocketChannel.accept(null, this);

		throw new UnsupportedOperationException("Cannot accept connections!");
	}

}
