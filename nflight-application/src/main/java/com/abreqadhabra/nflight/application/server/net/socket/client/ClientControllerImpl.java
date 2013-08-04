package com.abreqadhabra.nflight.application.server.net.socket.client;

import com.abreqadhabra.nflight.application.server.net.socket.MessageDTOImpl;


public class ClientControllerImpl {

	AsyncSocketClientAcceptor acceptor;
	
	public ClientControllerImpl() {
		this.init();
		
		acceptor =new AsyncSocketClientAcceptor(this);
	}

	private void init() {
		new Thread(new AsyncSocketClientAcceptor(this)).start();

	}

	public void send(MessageDTOImpl msg) {
		acceptor.send(msg.getContent());
	}

	
}
