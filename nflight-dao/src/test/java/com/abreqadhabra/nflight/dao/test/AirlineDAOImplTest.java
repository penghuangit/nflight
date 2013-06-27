package com.abreqadhabra.nflight.dao.test;

import com.abreqadhabra.nflight.dao.AirlineDAO;
import com.abreqadhabra.nflight.dao.DAOFactory;
import com.abreqadhabra.nflight.dao.dto.Airline;

public class AirlineDAOImplTest extends DAOTestBase {

	private AirlineDAO airlineDAO = null;

	public AirlineDAOImplTest() throws Exception{
		DAOFactory daoFactory = DAOFactory
				.getDAOFactory(DAOFactory.DATABASE_TYPE_DERBY);
		this.airlineDAO = daoFactory.getAirlineDAO();

	}
	
	/*
	public void insert(Airline airline) throws Exception {
		// TODO Auto-generated method stub

	}

	public void updateByPrimaryKey(Airline airline) throws Exception {
		// TODO Auto-generated method stub

	}

	public void deleteByPrimaryKey(String airlineCode) throws Exception {
		// TODO Auto-generated method stub

	}

	public void deleteAll() throws Exception {
		// TODO Auto-generated method stub

	}

	public Airline findByPrimaryKey(String airlineCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Airline[] matchByAirlineName(String airlineName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
*/
	// 件数を確認する
	public void testFindAll() throws Exception {
		Airline[] airlines = airlineDAO.findAll();
		assertEquals(14, airlines.length);
	}

}
