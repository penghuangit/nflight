package com.abreqadhabra.nflight.dao;

import com.abreqadhabra.nflight.common.exception.CommonException;


public abstract class DAOFactory {

	// There will be a method for each DAO that can be
	// created. The concrete factories will have to
	// implement these methods. public

	public static final String DATABASE_TYPE_DERBY = "derby";
	public static final String DATABASE_TYPE_ORACLE = "oracle";

    public abstract AirlineDAO getAirlineDAO() throws Exception;
    
	public static DAOFactory getDAOFactory(String whichFactory) {

		switch (whichFactory) {
		case DATABASE_TYPE_DERBY:
			return new DAOFactoryImpl(DATABASE_TYPE_DERBY);
		case DATABASE_TYPE_ORACLE:
			// return new
			// OracleDAOFactory(Constant.DAOFactory.DATABASE_TYPE_DERBY);
		default:
			return null;
		}
	}

}
