package com.abreqadhabra.nflight.application.trash_server.net.socket;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.dao.dto.Airline;

public class MessageDTOImpl implements MessageDTO {
	private transient static Class<MessageDTOImpl> THIS_CLAZZ = MessageDTOImpl.class;
	private transient static Logger LOGGER = LoggingHelper
			.getLogger(THIS_CLAZZ);

	private static long serialVersionUID = 1L;

	private String type;
	private String name;
	private String message;
	private transient ByteBuffer content;
	private Airline[] airlines;

	// @Override
	// public MessageDTO transfer(ByteBuffer buffer) {
	// String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
	// .getMethodName();
	//
	// MessageDTOImpl msg = new MessageDTOImpl();
	// byte[] bs02 = new byte[buffer.limit()];
	// buffer.get(bs02);
	// String content = new String(bs02, Charset.forName("UTF-8"));
	// // String[] strs = content.split("[:]");
	// String[] strs = content.split("[:]");
	// LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
	// content + "->" + Arrays.toString(strs) + "/" + strs.length);
	// msg.setType(strs[0]);
	// msg.setName(strs[1]);
	// msg.setMessage(strs[2]);
	// msg.setContent(ByteBuffer.wrap(bs02));
	// LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
	// msg.getMessage());
	// return msg;
	// }

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
	public void setAirlines(Airline[] airlines) {
		this.airlines = airlines;
	}

	@Override
	public Airline[] getAirlines() {
		return airlines;

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MessageDTOImpl [type=");
		builder.append(type);
		builder.append(", name=");
		builder.append(name);
		builder.append(", message=");
		builder.append(message);
		builder.append(", airlines=");
		builder.append(Arrays.toString(airlines));
		builder.append("]");
		return builder.toString();
	}

	



}
