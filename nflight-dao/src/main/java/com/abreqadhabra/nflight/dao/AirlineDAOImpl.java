package com.abreqadhabra.nflight.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.dao.dto.Airline;


public class AirlineDAOImpl extends CommonDAO implements AirlineDAO {

	private static final Class<AirlineDAOImpl> THIS_CLAZZ = AirlineDAOImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	
	private static final String AIRLINE_DAO_INSERT = ".sql.AirlineDAOImpl.insert(Airline)";
	private static final String AIRLINE_DAO_DELETE_ALL = ".sql.AirlineDAOImpl.deleteAll()";
	private static final String AIRLINE_DAO_DELETE_BY_PRIMARY_KEY = ".sql.AirlineDAOImpl.deleteByPrimaryKey(String)";
	private static final String AIRLINE_DAO_FIND_ALL = ".sql.AirlineDAOImpl.findAll()";
	private static final String AIRLINE_DAO_FIND_BY_PRIMARYKEY = ".sql.AirlineDAOImpl.findByPrimaryKey(String)";
	private static final String AIRLINE_DAO_MATCH_BY_AIRLINE_NAME = ".sql.AirlineDAOImpl.matchByAirlineName(String)";
	private static final String AIRLINE_DAO_UPDATE_BY_PRIMARY_KEY = ".sql.AirlineDAOImpl.updateByPrimaryKey(Airline)";

	public AirlineDAOImpl(String databaseType, String databaseMode) throws Exception {

		super(databaseType, databaseMode);

	}

	@Override
	public void commit() throws Exception {
		super.commit();

	}

	@Override
	public void deleteAll() throws Exception {
		String sql = super.getPropertyByDatabaseType(AIRLINE_DAO_DELETE_ALL);
		super.executeUpdateByDynamicQuery(sql, null);
	}

	@Override
	public void deleteByPrimaryKey(String airlineCode) throws Exception {
		String sql = super
				.getPropertyByDatabaseType(AIRLINE_DAO_DELETE_BY_PRIMARY_KEY);
		super.executeUpdateByDynamicQuery(sql, new String[] { airlineCode });
	}

	@Override
	public Airline[] findAll() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		String sql = super.getPropertyByDatabaseType(AIRLINE_DAO_FIND_ALL);
		List<Airline> results = super.findByDynamicQuery(sql, null,
				Airline.class);
		Airline airlines[] = new Airline[results.size()];
		results.toArray(airlines);

	    if (LOGGER.isLoggable(Level.FINER)) {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					sql, airlines);
	    }
	    
		return airlines[0] == null ? null : airlines;
	}

	@Override
	public Airline findByPrimaryKey(String airlineCode) throws Exception {
		String sql = super
				.getPropertyByDatabaseType(AIRLINE_DAO_FIND_BY_PRIMARYKEY);

		List<Airline> results = super.findByDynamicQuery(sql,
				new String[] { airlineCode }, Airline.class);
		Airline airlines[] = new Airline[results.size()];
		results.toArray(airlines);

		return airlines[0] == null ? new Airline() : airlines[0];
	}

	@Override
	public void insert(Airline airline) throws Exception {
		String sql = super.getPropertyByDatabaseType(AIRLINE_DAO_INSERT);

		super.executeUpdateByDynamicQuery(
				sql,
				new String[] { airline.getAirlineCode(),
						airline.getAirlineName() });
	}

	@Override
	public Airline[] matchByAirlineName(String airlineName) throws Exception {
		String sql = super
				.getPropertyByDatabaseType(AIRLINE_DAO_MATCH_BY_AIRLINE_NAME);
		List<Airline> results = super.findByDynamicQuery(sql,
				new String[] { airlineName }, Airline.class);
		Airline airlines[] = new Airline[results.size()];
		results.toArray(airlines);

		return airlines[0] == null ? new Airline[] {} : airlines;
	}

	@Override
	public void rollback() throws Exception {
		super.commit();
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws Exception {
		super.setAutoCommit(autoCommit);
	}

	@Override
	public void updateByPrimaryKey(Airline airline) throws Exception {
		String sql = super
				.getPropertyByDatabaseType(AIRLINE_DAO_UPDATE_BY_PRIMARY_KEY);
		super.executeUpdateByDynamicQuery(
				sql,
				new String[] { airline.getAirlineCode(),
						airline.getAirlineName(), airline.getAirlineCode() });
	}


}
