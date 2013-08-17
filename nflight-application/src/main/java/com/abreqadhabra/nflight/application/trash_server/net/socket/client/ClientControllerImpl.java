package com.abreqadhabra.nflight.application.trash_server.net.socket.client;

import java.nio.ByteBuffer;

import com.abreqadhabra.nflight.application.trash_server.net.socket.MessageDTO;
import com.abreqadhabra.nflight.application.trash_server.net.socket.NetworkChannelHelper;

public class ClientControllerImpl {

	AsyncSocketClientAcceptorBAK acceptor;

	public ClientControllerImpl() {
		this.init();

		acceptor = new AsyncSocketClientAcceptorBAK(this);
	}

	private void init() {
		new Thread(new AsyncSocketClientAcceptorBAK(this)).start();

	}

	public void send(MessageDTO messageDTO) {
		
		

		ByteBuffer outputByteBuffer = NetworkChannelHelper
				.serializeObject(messageDTO);

		acceptor.send(outputByteBuffer);
	}

}
