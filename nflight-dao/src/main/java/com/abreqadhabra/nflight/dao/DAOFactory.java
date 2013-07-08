package com.abreqadhabra.nflight.dao;

import java.util.Properties;




public abstract class DAOFactory {

	// There will be a method for each DAO that can be
	// created. The concrete factories will have to
	// implement these methods. public

	public static final String DATABASE_MODE_LOCAL = "local";
	public static final String DATABASE_MODE_REMOTE = "remote";
	public static final String DATABASE_TYPE_DERBY ="derby";
	public static final String DATABASE_TYPE_ORACLE ="oracle";
	
	public abstract CommonDAO getCommonDAO()throws Exception;

    public abstract AirlineDAO getAirlineDAO() throws Exception;
    
	public static DAOFactory getDAOFactory(String databaseMode, Properties dbProperties) {
		switch (databaseMode) {
		case DATABASE_MODE_LOCAL:
			//return new DAOFactoryImpl(databaseMode, dbProperties);
		case DATABASE_MODE_REMOTE:
			// return new
			// OracleDAOFactory(Constant.DAOFactory.DATABASE_TYPE_DERBY);
		default:
			return null;


	}
	}


}
