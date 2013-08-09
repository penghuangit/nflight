package com.abreqadhabra.nflight.application.server.net.socket.client.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.net.socket.MessageDTOImpl;
import com.abreqadhabra.nflight.application.server.net.socket.client.AsyncSocketClientAcceptorBAK;
import com.abreqadhabra.nflight.application.server.net.socket.client.ClientControllerImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ReadHandler
		implements
			CompletionHandler<Integer, ClientControllerImpl> {
	private static final Class<ReadHandler> THIS_CLAZZ = ReadHandler.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	AsyncSocketClientAcceptorBAK acceptor;
	AsynchronousSocketChannel receiveAsyncSocketChannel;
	private final ByteBuffer buffer;

	public ReadHandler(AsyncSocketClientAcceptorBAK acceptor,
			AsynchronousSocketChannel receiveAsyncSocketChannel) {

		this.acceptor = acceptor;
		this.receiveAsyncSocketChannel = receiveAsyncSocketChannel;
		this.buffer = ByteBuffer.allocate(1024);

	}

	@Override
	public void completed(Integer result, ClientControllerImpl attachment) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,

			this.receiveAsyncSocketChannel + "----------> reading Message: "
					+ this.receiveAsyncSocketChannel.getRemoteAddress());
		} catch (final IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"result: " + Integer.toString(result));

		if (result > 1) {
			LOGGER.info(this.receiveAsyncSocketChannel.toString());
			this.buffer.flip();
			final byte[] bs = new byte[this.buffer.limit()];
			this.buffer.get(bs);

			final String content = new String(bs, Charset.forName("UTF-8"));
			// LOGGER.info(content);
			final String[] strs = content.split(":");

			MessageDTOImpl msg = new MessageDTOImpl();
		//	msg = (MessageDTOImpl) msg.deserializeObject(bs);

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
					final String name = msg.getMessage();
					final String[] names = name.split("@");
					LOGGER.info("name size  --->" + names.length);
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
			acceptor.receive();

		} else if (result < 1) {
			return;
		}

	}

	@Override
	public void failed(Throwable exc, ClientControllerImpl attachment) {
	}

}
