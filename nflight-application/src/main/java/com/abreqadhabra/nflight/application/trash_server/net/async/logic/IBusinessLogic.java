package com.abreqadhabra.nflight.application.trash_server.net.async.logic;

import com.abreqadhabra.nflight.application.trash_server.net.socket.ISocketServer;
import com.abreqadhabra.nflight.dao.dto.Airline;

public interface IBusinessLogic {
	Airline[] getAirlines() throws Exception;

	public void setServer(ISocketServer socketServer);
}
