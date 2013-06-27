package com.abreqadhabra.nflight.dao.test;

import java.sql.Connection;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;

import com.abreqadhabra.nflight.dao.DAOFactory;

public abstract class DAOTestBase extends DatabaseTestCase {

	private static String ORIGINAL_DATASET_FILE = "/com/abreqadhabra/nflight/dao/resources/test/initial-sample-data.xml";

	@Override
	protected IDatabaseConnection getConnection() throws Exception {
		DAOFactory daoFactory = DAOFactory
				.getDAOFactory(DAOFactory.DATABASE_TYPE_DERBY);
		Connection conn = daoFactory.getCommonDAO().getConnection();
		return new DatabaseConnection(conn);
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		return new XmlDataSet(getClass().getResourceAsStream(ORIGINAL_DATASET_FILE));
	}

}