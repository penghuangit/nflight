/**
 * 
 */
package com.abreqadhabra.nflight.samples.data;

import java.io.IOException;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.dao.DAOFactory;


public class LocalDatabaseFactoryImpl extends DerbyDatabaseFactoryImpl {

	public LocalDatabaseFactoryImpl(String databaseMode) {
		super(databaseMode);
		// TODO Auto-generated constructor stub
	}

	// 로그 출력을 위한 선언
	private static Logger logger = Logger.getLogger(LocalDatabaseFactoryImpl.class.getCanonicalName());
	
	/**
	 * @param dao
	 *            mode
	 * @throws IOException
	 */

/*	@Override
	public DAOFactory getDAOFactory(int whichDatabaseFactory,
			String whichDAOFactory) {
		return DAOFactory.getDAOFactory(whichDAOFactory, null); 
	}*/

}
