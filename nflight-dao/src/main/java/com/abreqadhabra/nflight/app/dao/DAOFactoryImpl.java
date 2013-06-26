package com.abreqadhabra.nflight.app.dao;

import com.abreqadhabra.nflight.common.exception.NFlightException;

public class DAOFactoryImpl extends DAOFactory {

	private String databaseType = null;

	public DAOFactoryImpl(String databaseType) {
		this.databaseType = databaseType;
	}

	@Override
	public AirlineDAO getAirlineDAO() throws NFlightException {
		return new AirlineDAOImpl(this.databaseType);
	}

}
