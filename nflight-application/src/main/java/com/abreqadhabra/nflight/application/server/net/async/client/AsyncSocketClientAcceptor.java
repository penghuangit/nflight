package com.abreqadhabra.nflight.application.server.net.async.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.net.socket.MessageDTO;
import com.abreqadhabra.nflight.application.server.net.socket.NetworkChannelHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class AsyncSocketClientAcceptor implements Runnable {
	private static final Class<AsyncSocketClientAcceptor> THIS_CLAZZ = AsyncSocketClientAcceptor.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	ClientController controller;
	private InetSocketAddress socketAddress;
	private AsynchronousSocketChannel asyncSocketChannel;
	private Future<Void> future;
	private ByteBuffer buffer;

	public AsyncSocketClientAcceptor(ClientController controller) {
		super();
		this.controller = controller;
		try {
			this.socketAddress = new InetSocketAddress(InetAddress
					.getLocalHost().getHostAddress(), 9999);
		} catch (final UnknownHostException e) {
			e.printStackTrace();
		}

		buffer = ByteBuffer.allocate(1024);

	}

	@Override
	public void run() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			asyncSocketChannel = AsynchronousSocketChannel.open();
			future = asyncSocketChannel.connect(socketAddress);
			future.get();

			LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					asyncSocketChannel.getLocalAddress() + " ---> "
							+ asyncSocketChannel.getRemoteAddress());

			read();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void read() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		buffer.clear();
		asyncSocketChannel.read(buffer, this,
				new CompletionHandler<Integer, AsyncSocketClientAcceptor>() {

					public void completed(Integer result,
							AsyncSocketClientAcceptor attachment) {
						if (result > 1) {
							buffer.flip();
							byte[] bs = new byte[buffer.limit()];
							buffer.get(bs);

							MessageDTO messageDTO = (MessageDTO) NetworkChannelHelper
									.deserializeObject(buffer);

							LOGGER.logp(Level.FINER,
									THIS_CLAZZ.getSimpleName(), METHOD_NAME,
									"messageDTO: " + messageDTO);

							read();
						} else if (result < 1) {
							return;
						}
					}
					public void failed(Throwable exc,
							AsyncSocketClientAcceptor attachment) {

					}
				});
	}

	public void send(ByteBuffer outputByteBuffer) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			int result = asyncSocketChannel.write(outputByteBuffer).get();

			LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					asyncSocketChannel.getLocalAddress() + " ---> "
							+ asyncSocketChannel.getRemoteAddress()
							+ " result: " + result);
		} catch (InterruptedException | ExecutionException | IOException e) {
			e.printStackTrace();
		}

	}

}
