package com.abreqadhabra.nflight.dao;

public class DAOFactoryImpl extends DAOFactory {

	private String databaseType = null;

	public DAOFactoryImpl(String databaseType) {
		this.databaseType = databaseType;
	}

	@Override
	public CommonDAO getCommonDAO() throws Exception {
		return new CommonDAO(this.databaseType);
	}

	@Override
	public AirlineDAO getAirlineDAO() throws Exception {
		return new AirlineDAOImpl(this.databaseType);
	}

}
