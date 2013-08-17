package com.abreqadhabra.nflight.application.trash_server.net.socket.logic;

import java.util.concurrent.LinkedBlockingQueue;

import com.abreqadhabra.nflight.application.trash_server.logic.BusinessLogic;
import com.abreqadhabra.nflight.application.trash_server.net.socket.AsyncSocketServerAcceptor;
import com.abreqadhabra.nflight.application.trash_server.net.socket.ISocketServer;
import com.abreqadhabra.nflight.application.trash_server.net.socket.MessageDTO;

public interface IBusinessLogicHandler extends BusinessLogic {

	void setServer(ISocketServer socketServer);
	public void doLogic(AsyncSocketServerAcceptor session,
			LinkedBlockingQueue<MessageDTO> inputQueue,
			LinkedBlockingQueue<MessageDTO> outputQueue);
	public void endCallBack();
}
