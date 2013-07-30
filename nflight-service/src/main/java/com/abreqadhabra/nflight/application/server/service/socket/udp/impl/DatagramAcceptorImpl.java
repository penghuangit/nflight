package com.abreqadhabra.nflight.application.server.service.socket.udp.impl;

import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

import com.abreqadhabra.nflight.application.server.service.socket.AbstractAcceptor;
import com.abreqadhabra.nflight.application.server.service.socket.Attachment;

public class DatagramAcceptorImpl extends AbstractSocketAcceptor {

	@Override
	public void read(SelectionKey key) throws IOException {
		DatagramChannel channel = (DatagramChannel) key.channel();
		Attachment attachment = (Attachment) key.attachment();
		attachment.setSocketAddress(channel.receive(attachment
				.getRequestByteBuffer()));

		System.out.println(new String(
				attachment.getRequestByteBuffer().array(), "UTF-8"));

	}

	@Override
	public void write(SelectionKey key) throws IOException {
		DatagramChannel channel = (DatagramChannel) key.channel();
		Attachment attachment = (Attachment) key.attachment();
		channel.send(attachment.getResponseByteBuffer(),
				attachment.getSocketAddress());

		System.out.println(new String(attachment.getResponseByteBuffer()
				.array(), "UTF-8"));

	}

}
