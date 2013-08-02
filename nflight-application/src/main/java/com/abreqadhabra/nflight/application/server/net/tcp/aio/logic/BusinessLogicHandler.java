package com.abreqadhabra.nflight.application.server.net.tcp.aio.logic;

import java.util.concurrent.LinkedBlockingQueue;

import com.abreqadhabra.nflight.application.server.logic.BusinessLogic;
import com.abreqadhabra.nflight.application.server.net.tcp.SocketServer;
import com.abreqadhabra.nflight.application.server.net.tcp.aio.MessageDTO;
import com.abreqadhabra.nflight.application.server.net.tcp.aio.SocketServerSession;

public interface BusinessLogicHandler extends BusinessLogic {

	void setServer(SocketServer socketServer);
	public void doLogic(SocketServerSession session,
			LinkedBlockingQueue<MessageDTO> inputQueue,
			LinkedBlockingQueue<MessageDTO> outputQueue);
	public void endCallBack();
}
