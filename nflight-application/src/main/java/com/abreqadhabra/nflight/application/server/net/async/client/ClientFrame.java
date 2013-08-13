package com.abreqadhabra.nflight.application.server.net.async.client;

import java.util.logging.Logger;

import javax.swing.JFrame;

import com.abreqadhabra.nflight.application.server.net.socket.MessageDTO;
import com.abreqadhabra.nflight.application.server.net.socket.MessageDTOImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ClientFrame extends JFrame {
	private static Class<ClientFrame> THIS_CLAZZ = ClientFrame.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private static long serialVersionUID = 1L;

	ClientController controller;

	public ClientFrame(ClientController controller) {
		this.controller = controller;
		initComponent();
		sendTest();

	}

	private void initComponent() {
		this.setVisible(true);
	}

	private void sendTest() {
		MessageDTO messageDTO = new MessageDTOImpl();
		messageDTO.setType(THIS_CLAZZ.getName());
		controller.send(messageDTO);

	}

}
