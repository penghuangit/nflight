/**
 * 
 */
package com.abreqadhabra.nflight.db.server;

import java.io.IOException;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.dao.DAOFactory;

public class LocalDatabaseFactoryImpl extends DatabaseFactory {

	// 로그 출력을 위한 선언
	private static Logger logger = Logger.getLogger(LocalDatabaseFactoryImpl.class.getCanonicalName());
	
	/**
	 * @param dao
	 *            mode
	 * @throws IOException
	 */

	@Override
	public DAOFactory getDAOFactory(int whichDatabaseFactory,
			String whichDAOFactory) {
		return DAOFactory.getDAOFactory(whichDAOFactory); 
	}

}
