package com.abreqadhabra.nflight.application.server.net.async.client;

import java.nio.ByteBuffer;

import com.abreqadhabra.nflight.application.server.net.socket.MessageDTO;
import com.abreqadhabra.nflight.application.server.net.socket.NetworkChannelHelper;

public class ClientController {

	AsyncSocketClientAcceptor acceptor;

	public ClientController() {

		acceptor = new AsyncSocketClientAcceptor(this);
		new Thread(acceptor).start();

		new ClientFrame(this);
	}

	public void send(MessageDTO messageDTO) {

		ByteBuffer outputByteBuffer = NetworkChannelHelper
				.serializeObject(messageDTO);

		acceptor.send(outputByteBuffer);
	}
}
