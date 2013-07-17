package com.abreqadhabra.nflight.samples.data;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.util.IOStream;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.dao.DAOFactory;

public abstract class DatabaseFactory {

	// 로그 출력을 위한 선언
	static Logger logger = Logger.getLogger(DatabaseFactory.class
			.getCanonicalName());

	// List of DAO types supported by the factory

	private static final String DATABASE_TYPE = "database.type";
	private static final String DATABASE_MODE = "database.mode";
	private static final String DB_PROPERTY_FILE_NAME = "com/abreqadhabra/nflight/dao/resources/config/db.properties";
	private static  Properties  DB_PROPERTIES;
	public static final String DATABASE_TYPE_DERBY = "derby";
	public static final String DATABASE_TYPE_ORACLE = "oracle";

	/**
	 * This method instantiates the DAO class that is used in this applications
	 * deployment environment to access the data.
	 */
	public abstract DAOFactory getDAOFactory(Properties DB_PROPERTIES);

	public static DatabaseFactory getDatabaseFactory() throws Exception {

		DB_PROPERTIES = PropertyFile.readPropertyFile(IOStream.getCodebase(DAOFactory.class.getName())
					+ DB_PROPERTY_FILE_NAME); 
					
		String databaseType = DB_PROPERTIES.getProperty(DATABASE_TYPE);
		String databaseMode = DB_PROPERTIES.getProperty(DATABASE_MODE);

		logger.log(Level.INFO, databaseType + " 데이터베이스는 " + databaseType
				+ " 모드로 동작합니다.");

		switch (databaseType) {
		case DATABASE_TYPE_DERBY:
			return new DerbyDatabaseFactoryImpl(databaseMode);
		case DATABASE_TYPE_ORACLE:
			// return new
			// OracleDAOFactory(Constant.DAOFactory.DATABASE_TYPE_DERBY);
		default:
			return null;
		}
	}

	public DAOFactory getDAOFactory(int whichDatabaseFactory,
			String whichDAOFactory) {
		// TODO Auto-generated method stub
		return null;
	}

}