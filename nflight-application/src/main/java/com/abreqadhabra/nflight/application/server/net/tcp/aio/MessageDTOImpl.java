package com.abreqadhabra.nflight.application.server.net.tcp.aio;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class MessageDTOImpl implements MessageDTO {
	private static final Class<MessageDTOImpl> THIS_CLAZZ = MessageDTOImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private static final long serialVersionUID = 1828759513269899461L;

	private String type;
	private String name;
	private String message;
	private ByteBuffer content;

	@Override
	public MessageDTO transfer(ByteBuffer buffer) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		MessageDTOImpl msg = new MessageDTOImpl();
		byte[] bs02 = new byte[buffer.limit()];
		buffer.get(bs02);
		String content = new String(bs02, Charset.forName("UTF-8"));
		//String[] strs = content.split("[:]");
		String[] strs = content.split(",");
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				content + "->" +Arrays.toString(strs) + "/"+ strs.length);
		msg.setType(strs[0]);
		msg.setName(strs[1]);
		msg.setMessage(strs[2]);
		msg.setContent(ByteBuffer.wrap(bs02));
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				msg.getMessage());
		return msg;
	}

	@Override
	public ByteBuffer getContent() {
		return this.content;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public void setContent(ByteBuffer content) {
		this.content = content;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TextMessageImpl [type=").append(this.type)
				.append(", name=").append(this.name).append(", message=")
				.append(this.message).append(", content=").append(this.content)
				.append("]");
		return builder.toString();
	}

}
