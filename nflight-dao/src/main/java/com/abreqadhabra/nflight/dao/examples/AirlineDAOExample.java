package com.abreqadhabra.nflight.dao.examples;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.CommonException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.dao.AirlineDAO;
import com.abreqadhabra.nflight.dao.DAOFactory;
import com.abreqadhabra.nflight.dao.dto.Airline;
import com.abreqadhabra.nflight.dao.exception.NFlightDAOException;

/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

public class AirlineDAOExample {
	private static final Class<AirlineDAOExample> THIS_CLAZZ = AirlineDAOExample.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	String resourceBundleName = "com.abreqadhabra.nflight.common.resources.logging.LoggingMessages";

	/*
	 * String resourceBundleName = "com.ibm.websphere.componentX.Messages";
	 * Logger logger = Logger.getLogger(componentName, resourceBundleName);
	 */

	private AirlineDAO airlineDAO = null;

	public AirlineDAOExample() throws Exception {

		this.airlineDAO = this.getAirlineDAO();

	}

	public static void main(String[] arg) {

		try {
			AirlineDAOExample airlineDAOExample = new AirlineDAOExample();
			airlineDAOExample.excute();
		} catch (Exception e) {
			StackTraceElement[] current = e.getStackTrace();
			if (e instanceof CommonException) {
				CommonException ce = (CommonException) e;
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(), "\n" + ce.getStackTrace(e));
			} else {
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(), e.getMessage());
			}

	
		}

	}

	private void excute() throws Exception  {
		
		try{
			Airline airline = new Airline();
			Airline[] airlines = null;

			airlineDAO.setAutoCommit(true);

			airline.setAirlineCode("C1");
			airline.setAirlineName("N1");
			System.out.println(airline.toString());

			this.insert(airline);

			airline.setAirlineCode("C2");
			airline.setAirlineName("N2");
			System.out.println(airline.toString());

			this.insert(airline);

			airline.setAirlineName("U1");
			System.out.println(airline.toString());

			this.update(airline);

			airlines = this.findAll();

			for (int i = 0; i < airlines.length; i++) {
				System.out.println(airlines[i]);
			}

			String airlineCode = "C1";
			airline = this.findByPrimaryKey(airlineCode);
			System.out.println(airline.toString());

			String airlineName = "N";
			airlines = this.findByAirlineNameLIKE(airlineName);

			for (int i = 0; i < airlines.length; i++) {
				System.out.println(airlines[i]);
			}

			this.delete(airlineCode);

			airlines = this.findAll();

			for (int i = 0; i < airlines.length; i++) {
				System.out.println(airlines[i]);
			}

			this.deleteAll();

			this.airlineDAO.commit();
			this.airlineDAO.setAutoCommit(false);
		}catch(Exception e){
			
			if (e instanceof NFlightDAOException) {
/*
 				try {
					this.airlineDAO.rollback();
				} catch (Exception e1) {
					throw new NFlightDAOException("rollback", e1);
				}
				*/
			}
			throw new NFlightDAOException( e);
		}

	}

	private AirlineDAO getAirlineDAO() throws Exception {
		DAOFactory daoFactory = DAOFactory
				.getDAOFactory(DAOFactory.DATABASE_TYPE_DERBY);
		return daoFactory.getAirlineDAO();
	}

	private void insert(Airline airline) throws Exception {
		airlineDAO.insert(airline);
	}

	private void update(Airline airline) throws Exception {
		airlineDAO.updateByPrimaryKey(airline);
	}

	private Airline findByPrimaryKey(String airlineCode) throws Exception {
		return airlineDAO.findByPrimaryKey(airlineCode);
	}

	private Airline[] findByAirlineNameLIKE(String airlineName)
			throws Exception {
		return airlineDAO.matchByAirlineName(airlineName);
	}

	private void delete(String airlineCode) throws Exception {
		airlineDAO.deleteByPrimaryKey(airlineCode);
	}

	private void deleteAll() throws Exception {
		airlineDAO.deleteAll();
	}

	private Airline[] findAll() throws Exception {
		return airlineDAO.findAll();
	}

}