package com.abreqadhabra.nflight.application.server.net.socket.client;

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

import com.abreqadhabra.nflight.application.server.net.socket.MessageDTOImpl;
import com.abreqadhabra.nflight.application.server.net.socket.NetworkChannelHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class AsyncSocketClientAcceptor implements Runnable {
	private static final Class<AsyncSocketClientAcceptor> THIS_CLAZZ = AsyncSocketClientAcceptor.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private final ClientControllerImpl controller;

	InetSocketAddress socketAddress;

	private final ByteBuffer buffer;

	AsynchronousSocketChannel receiveAsyncSocketChannel;

	public AsyncSocketClientAcceptor(final ClientControllerImpl controller) {
		super();
		this.controller = controller;
		try {
			this.socketAddress = new InetSocketAddress(InetAddress
					.getLocalHost().getHostAddress(), 9999);
		} catch (final UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.buffer = ByteBuffer.allocate(1024);
	}

	@Override
	public void run() {
		this.receiveAsyncSocketChannel = this.connect();

		this.receive();

	}

	private AsynchronousSocketChannel connect() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		AsynchronousSocketChannel asyncSocketChannel = null;
		try {

			asyncSocketChannel = AsynchronousSocketChannel.open();

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"asyncSocketChannel. isOpen:" + asyncSocketChannel.isOpen());

			final Future<Void> future = asyncSocketChannel
					.connect(this.socketAddress);
			future.get();

			try {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME,

						asyncSocketChannel + "----------> Connect Channel: "
								+ asyncSocketChannel.getRemoteAddress());
			} catch (final IOException e1) {
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
		this.receiveAsyncSocketChannel.read(this.buffer, this.controller, 
	//			new ReadHandler(this, receiveAsyncSocketChannel ));
				new CompletionHandler<Integer, ClientControllerImpl>() {

					@Override
					public void completed(final Integer result,
							final ClientControllerImpl attachment) {
						final String METHOD_NAME = Thread.currentThread()
								.getStackTrace()[1].getMethodName();

						try {
							LOGGER.logp(
									Level.FINER,
									THIS_CLAZZ.getSimpleName(),
									METHOD_NAME,

									AsyncSocketClientAcceptor.this.receiveAsyncSocketChannel
											+ "----------> reading Message: "
											+ AsyncSocketClientAcceptor.this.receiveAsyncSocketChannel
													.getRemoteAddress());
						} catch (final IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
								METHOD_NAME, "result: " + Integer.toString(result));

						if (result > 1) {
							LOGGER.info(AsyncSocketClientAcceptor.this.receiveAsyncSocketChannel
									.toString());
							AsyncSocketClientAcceptor.this.buffer.flip();
							final byte[] bs = new byte[AsyncSocketClientAcceptor.this.buffer
									.limit()];
							AsyncSocketClientAcceptor.this.buffer.get(bs);

							final String content = new String(bs, Charset
									.forName("UTF-8"));
						//	LOGGER.info(content);
							final String[] strs = content.split(":");

							MessageDTOImpl msg = new MessageDTOImpl();
							msg =  (MessageDTOImpl) NetworkChannelHelper.deserializeObject(buffer);

//							LOGGER.info("msg.deserializeObject  --->"
//									+ x.getClass().getName());

							// msg.setType(strs[0]);
							// msg.setName(strs[1]);
							// msg.setMessage(strs[2]);
							// msg.setContent(ByteBuffer.wrap(bs));
							
							
									
							if (msg.getType() == null) {
								LOGGER.info(msg.toString());
							} else {
								
		
								
								if (msg.getType().equals("fresh")) {
									final String name = msg.getMessage();
									final String[] names = name.split("@");
									LOGGER.info("name size  --->"
											+ names.length);
									// DefaultListModel model =
									// clientPanel.model;
									// model.clear();
									for (final String s : names) {
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
							AsyncSocketClientAcceptor.this.receive();
						} else if (result < 1) {
							return;
						}
						

					}

					@Override
					public void failed(final Throwable exc,
							final ClientControllerImpl attachment) {

					}
				});
	}

	public void send(final ByteBuffer byteBuffer) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		final AsynchronousSocketChannel asyncSocketChannel = this.connect();

		try {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					asyncSocketChannel.getLocalAddress()
							+ "----------> writing Message: "
							+ asyncSocketChannel.getRemoteAddress());
		} catch (final IOException e) {
			e.printStackTrace();
		}
		asyncSocketChannel.write(byteBuffer);

		this.close(asyncSocketChannel);
	}

	private void close(final AsynchronousSocketChannel asyncSocketChannel) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					asyncSocketChannel.getLocalAddress()
							+ "----------> close channel: "
							+ asyncSocketChannel.getRemoteAddress());
		} catch (final IOException e) {
			e.printStackTrace();
		}

		try {
			asyncSocketChannel.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}
}
