package com.abreqadhabra.nflight.application.server.net.socket.logic;

import java.util.concurrent.LinkedBlockingQueue;

import com.abreqadhabra.nflight.application.server.logic.BusinessLogic;
import com.abreqadhabra.nflight.application.server.net.socket.ISocketServer;
import com.abreqadhabra.nflight.application.server.net.socket.MessageDTO;
import com.abreqadhabra.nflight.application.server.net.socket.AsyncSocketServerAcceptor;

public interface IBusinessLogicHandler extends BusinessLogic {

	void setServer(ISocketServer socketServer);
	public void doLogic(AsyncSocketServerAcceptor session,
			LinkedBlockingQueue<MessageDTO> inputQueue,
			LinkedBlockingQueue<MessageDTO> outputQueue);
	public void endCallBack();
}
