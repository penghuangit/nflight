package com.abreqadhabra.nflight.application.server.service.socket.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;

import com.abreqadhabra.nflight.application.server.service.socket.AbstractAcceptor;
import com.abreqadhabra.nflight.application.server.service.socket.Attachment;

public class StreamAcceptorImpl extends AbstractAcceptor {

	@Override
	public void read(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void read(SelectionKey key) throws IOException {
//		SocketChannel channel = (SocketChannel) key.channel();
//
//		//we open the channel and connect
//		channel.read(data);
//		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data.array());
//		ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
//		Message message = (Message)objectInputStream.readObject();
//	}
//
//	// isWritable returned true
//	@Override
//	public void write(SelectionKey key) throws IOException {
//		SocketChannel channel = (SocketChannel) key.channel();
//
//		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//		objectOutputStream.writeObject(outgoingMessage);
//		objectOutputStream.flush();
//		channel.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
//
//	
	}

	


