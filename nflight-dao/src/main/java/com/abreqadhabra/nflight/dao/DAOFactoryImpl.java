package com.abreqadhabra.nflight.dao;

import com.abreqadhabra.nflight.common.exception.CommonException;

public class DAOFactoryImpl extends DAOFactory {

	private String databaseType = null;

	public DAOFactoryImpl(String databaseType) {
		this.databaseType = databaseType;
	}

	@Override
	public AirlineDAO getAirlineDAO() throws Exception {
		return new AirlineDAOImpl(this.databaseType);
	}

}
