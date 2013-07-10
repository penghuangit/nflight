package com.abreqadhabra.nflight.samples.data;

import java.util.Properties;

import com.abreqadhabra.nflight.dao.DAOFactory;

public class DerbyDatabaseFactoryImpl extends DatabaseFactory {

	String databaseMode = null;
	public DerbyDatabaseFactoryImpl(String databaseMode) {
		this.databaseMode = databaseMode;
	}
	@Override
	public DAOFactory getDAOFactory(Properties DB_PROPERTIES) {
		// TODO Auto-generated method stub
		return null;
	}

/*	@Override
	public DAOFactory getDAOFactory(Properties dbProperties) {
		return DAOFactory.getDAOFactory(databaseMode, dbProperties);
	}
*/

}
