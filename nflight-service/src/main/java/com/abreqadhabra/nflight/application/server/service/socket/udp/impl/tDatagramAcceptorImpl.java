package com.abreqadhabra.nflight.application.server.service.socket.udp.impl;

import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.service.socket.udp.common.Attachment;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class tDatagramAcceptorImpl  {
	private static final Class<tDatagramAcceptorImpl> THIS_CLAZZ = tDatagramAcceptorImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	



	public void read(SelectionKey key) throws IOException {
		DatagramChannel channel = (DatagramChannel) key.channel();
		Attachment attachment = (Attachment) key.attachment();
		attachment.setSocketAddress(channel.receive(attachment
				.getRequestByteBuffer()));

		System.out.println(new String(
				attachment.getRequestByteBuffer().array(), "UTF-8"));

	}

	
	public void write(SelectionKey key) throws IOException {
		DatagramChannel channel = (DatagramChannel) key.channel();
		Attachment attachment = (Attachment) key.attachment();
		channel.send(attachment.getResponseByteBuffer(),
				attachment.getSocketAddress());

		System.out.println(new String(attachment.getResponseByteBuffer()
				.array(), "UTF-8"));

	}


		
	

}
