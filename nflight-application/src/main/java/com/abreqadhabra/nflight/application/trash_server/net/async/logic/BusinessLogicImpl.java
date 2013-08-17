package com.abreqadhabra.nflight.application.trash_server.net.async.logic;

import com.abreqadhabra.nflight.application.trash_server.net.socket.ISocketServer;
import com.abreqadhabra.nflight.dao.AirlineDAO;
import com.abreqadhabra.nflight.dao.DAOFactory;
import com.abreqadhabra.nflight.dao.dto.Airline;

public class BusinessLogicImpl implements IBusinessLogic {

	private Airline[] airlines;
	private DAOFactory daoFactory;
	private AirlineDAO airlineDAO;
	private ISocketServer server;

	public BusinessLogicImpl() throws Exception {
		daoFactory = DAOFactory.getDAOFactory(DAOFactory.DATABASE_TYPE_DERBY);
		airlineDAO = daoFactory.getAirlineDAO();
	}

	public Airline[] getAirlines() throws Exception {

		return airlineDAO.findAll();

	}

	
	
	public void setServer(ISocketServer socketServer) {
		this.server = socketServer;
	}
}
