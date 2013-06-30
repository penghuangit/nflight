package com.abreqadhabra.nflight.db.server;

import java.util.Properties;

import com.abreqadhabra.nflight.dao.DAOFactory;

public class DerbyDatabaseFactoryImpl extends DatabaseFactory {

	String databaseMode = null;
	public DerbyDatabaseFactoryImpl(String databaseMode) {
		this.databaseMode = databaseMode;
	}

	@Override
	public DAOFactory getDAOFactory(Properties dbProperties) {
		return DAOFactory.getDAOFactory(databaseMode, dbProperties);
	}


}
