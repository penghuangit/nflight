package com.abreqadhabra.nflight.application.server.net.async.logic;

import com.abreqadhabra.nflight.application.server.net.socket.ISocketServer;
import com.abreqadhabra.nflight.dao.dto.Airline;

public interface IBusinessLogic {
	Airline[] getAirlines() throws Exception;

	public void setServer(ISocketServer socketServer);
}
