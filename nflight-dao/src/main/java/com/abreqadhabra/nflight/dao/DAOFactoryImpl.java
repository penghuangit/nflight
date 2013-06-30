package com.abreqadhabra.nflight.dao;



public class DAOFactoryImpl extends DAOFactory {

	private String databaseType = null;
	private String databaseMode = null;
	
	public DAOFactoryImpl(String databaseType, String databaseMode) {
			this.databaseType = databaseType;
			this.databaseMode = databaseMode;
	}

	@Override
	public CommonDAO getCommonDAO() throws Exception {
		return new CommonDAO(this.databaseType, this.databaseMode);
	}

	@Override
	public AirlineDAO getAirlineDAO() throws Exception {
		return new AirlineDAOImpl(this.databaseType, this.databaseMode);
	}



}
