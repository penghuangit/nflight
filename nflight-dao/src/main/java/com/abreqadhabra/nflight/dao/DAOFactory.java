package com.abreqadhabra.nflight.dao;

import com.abreqadhabra.nflight.dao.oracle.OracleDAOFactory;

public abstract class DAOFactory {

	// There will be a method for each DAO that can be
	// created. The concrete factories will have to
	// implement these methods. public

	public static final String DATABASE_MODE_LOCAL = "local";
	public static final String DATABASE_MODE_REMOTE = "remote";
	public static final String DATABASE_TYPE_DERBY = "derby";
	public static final String DATABASE_TYPE_ORACLE = "oracle";

	public abstract CommonDAO getCommonDAO() throws Exception;

	public abstract AirlineDAO getAirlineDAO() throws Exception;

	public static DAOFactory getDAOFactory(final String databaseType) {
		switch (databaseType) {
			case DATABASE_TYPE_DERBY :
				return new DAOFactoryImpl(DATABASE_TYPE_DERBY);
			case DATABASE_TYPE_ORACLE :
				return new OracleDAOFactory(DATABASE_TYPE_ORACLE);
			default :
				return null;

		}
	}

}
