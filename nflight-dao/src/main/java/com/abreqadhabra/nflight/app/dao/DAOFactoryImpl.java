package com.abreqadhabra.nflight.app.dao;

import com.abreqadhabra.nflight.app.dao.exception.DAORuntimeException;

public class DAOFactoryImpl extends DAOFactory {

	private String databaseType = null;

	public DAOFactoryImpl(String databaseType) {
		this.databaseType = databaseType;
	}

	@Override
	public AirlineDAO getAirlineDAO() throws DAORuntimeException {
		return new AirlineDAOImpl(this.databaseType);
	}

}
