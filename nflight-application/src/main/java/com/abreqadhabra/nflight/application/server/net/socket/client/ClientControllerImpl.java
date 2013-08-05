package com.abreqadhabra.nflight.application.server.net.socket.client;

import java.nio.ByteBuffer;

import com.abreqadhabra.nflight.application.server.net.socket.MessageDTO;
import com.abreqadhabra.nflight.application.server.net.socket.NetworkChannelHelper;

public class ClientControllerImpl {

	AsyncSocketClientAcceptor acceptor;

	public ClientControllerImpl() {
		this.init();

		acceptor = new AsyncSocketClientAcceptor(this);
	}

	private void init() {
		new Thread(new AsyncSocketClientAcceptor(this)).start();

	}

	public void send(MessageDTO messageDTO) {
		
		

		ByteBuffer outputByteBuffer = NetworkChannelHelper
				.serializeObject(messageDTO);

		acceptor.send(outputByteBuffer);
	}

}
