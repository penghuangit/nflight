package com.abreqadhabra.nflight.application.trash_server.net.socket.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.trash_server.net.socket.MessageDTOImpl;
import com.abreqadhabra.nflight.application.trash_server.net.socket.NetworkChannelHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class AsyncSocketClientAcceptorBAK implements Runnable {
	private static Class<AsyncSocketClientAcceptorBAK> THIS_CLAZZ = AsyncSocketClientAcceptorBAK.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private ClientControllerImpl controller;

	InetSocketAddress socketAddress;

	private ByteBuffer buffer;

	AsynchronousSocketChannel asyncSocketChannel;

	public AsyncSocketClientAcceptorBAK(ClientControllerImpl controller) {
		super();
		this.controller = controller;
		try {
			this.socketAddress = new InetSocketAddress(InetAddress
					.getLocalHost().getHostAddress(), 9999);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.buffer = ByteBuffer.allocate(1024);

		this.asyncSocketChannel = this.connect();

	}

	@Override
	public void run() {

		this.receive();

	}

	private AsynchronousSocketChannel connect() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		AsynchronousSocketChannel asyncSocketChannel = null;
		try {

			asyncSocketChannel = AsynchronousSocketChannel.open();

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"asyncSocketChannel. isOpen:" + asyncSocketChannel.isOpen());

			Future<Void> future = asyncSocketChannel
					.connect(this.socketAddress);
			future.get();

			try {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME,

						asyncSocketChannel + "----------> Connect Channel: "
								+ asyncSocketChannel.getRemoteAddress());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (IOException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return asyncSocketChannel;
	}

	public void receive() {
		Thread.currentThread().getStackTrace()[1].getMethodName();

		this.buffer.clear();

		// connect this channel's socket
		this.asyncSocketChannel.read(this.buffer, this.controller,
		// new ReadHandler(this, asyncSocketChannel ));
				new CompletionHandler<Integer, ClientControllerImpl>() {

					@Override
					public void completed(Integer result,
							ClientControllerImpl attachment) {
						String METHOD_NAME = Thread.currentThread()
								.getStackTrace()[1].getMethodName();

						try {
							LOGGER.logp(
									Level.FINER,
									THIS_CLAZZ.getSimpleName(),
									METHOD_NAME,

									AsyncSocketClientAcceptorBAK.this.asyncSocketChannel
											+ "----------> reading Message: "
											+ AsyncSocketClientAcceptorBAK.this.asyncSocketChannel
													.getRemoteAddress());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
								METHOD_NAME,
								"result: " + Integer.toString(result));

						if (result > 1) {
							LOGGER.info(AsyncSocketClientAcceptorBAK.this.asyncSocketChannel
									.toString());
							AsyncSocketClientAcceptorBAK.this.buffer.flip();
							byte[] bs = new byte[AsyncSocketClientAcceptorBAK.this.buffer
									.limit()];
							AsyncSocketClientAcceptorBAK.this.buffer.get(bs);

							String content = new String(bs, Charset
									.forName("UTF-8"));
							content.split(":");

							MessageDTOImpl msg = new MessageDTOImpl();
							msg = (MessageDTOImpl) NetworkChannelHelper
									.deserializeObject(AsyncSocketClientAcceptorBAK.this.buffer);

							// LOGGER.info("msg.deserializeObject  --->"
							// + x.getClass().getName());

							// msg.setType(strs[0]);
							// msg.setName(strs[1]);
							// msg.setMessage(strs[2]);
							// msg.setContent(ByteBuffer.wrap(bs));

							if (msg.getType() == null) {
								LOGGER.info(msg.toString());
							} else {

								if (msg.getType().equals("fresh")) {
									String name = msg.getMessage();
									String[] names = name.split("@");
									LOGGER.info("name size  --->"
											+ names.length);
									// DefaultListModel model =
									// clientPanel.model;
									// model.clear();
									for (String s : names) {
										LOGGER.info("name   --->" + s);
									}
								} else if (msg.getType().equals("chat")) {
									// if (count % 10 == 0) {
									// clientPanel.jta01.setText("");
									// }
									// String oldMsg =
									// clientPanel.jta01.getText();
									// clientPanel.jta01.setText(oldMsg
									// + msg.getName() + " :"
									// + msg.getMessage() + "\n");
									//
								}

							}
							AsyncSocketClientAcceptorBAK.this.receive();
						} else if (result < 1) {
							return;
						}

					}

					@Override
					public void failed(Throwable exc,
							ClientControllerImpl attachment) {

					}
				});
	}

	public void send(ByteBuffer byteBuffer) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		// AsynchronousSocketChannel asyncSocketChannel = this.connect();
		try {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					this.asyncSocketChannel.getLocalAddress()
							+ "----------> writing Message: "
							+ this.asyncSocketChannel.getRemoteAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.asyncSocketChannel.write(byteBuffer);

		this.close(this.asyncSocketChannel);
		// receive();
	}

	private void close(AsynchronousSocketChannel asyncSocketChannel) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					asyncSocketChannel.getLocalAddress()
							+ "----------> close channel: "
							+ asyncSocketChannel.getRemoteAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			asyncSocketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
