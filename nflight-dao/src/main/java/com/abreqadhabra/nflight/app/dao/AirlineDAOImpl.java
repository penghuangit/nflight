package com.abreqadhabra.nflight.app.dao;

import java.util.List;

import com.abreqadhabra.nflight.app.dao.dto.Airline;
import com.abreqadhabra.nflight.app.dao.exception.DAORuntimeException;

public class AirlineDAOImpl extends GenericDAO implements AirlineDAO {

	private static final String AIRLINE_DAO_INSERT = ".sql.AirlineDAOImpl.insert(Airline)";
	private static final String AIRLINE_DAO_DELETE_ALL = ".sql.AirlineDAOImpl.deleteAll()";
	private static final String AIRLINE_DAO_DELETE_BY_PRIMARY_KEY = ".sql.AirlineDAOImpl.deleteByPrimaryKey(String)";
	private static final String AIRLINE_DAO_FIND_ALL = ".sql.AirlineDAOImpl.findAll()";
	private static final String AIRLINE_DAO_FIND_BY_PRIMARYKEY = ".sql.AirlineDAOImpl.findByPrimaryKey(String)";
	private static final String AIRLINE_DAO_MATCH_BY_AIRLINE_NAME = ".sql.AirlineDAOImpl.matchByAirlineName(String)";
	private static final String AIRLINE_DAO_UPDATE_BY_PRIMARY_KEY = ".sql.AirlineDAOImpl.updateByPrimaryKey(Airline)";

	public AirlineDAOImpl(String databaseType) throws DAORuntimeException {

		super(databaseType);

	}

	@Override
	public void commit() {
		super.commit();

	}

	@Override
	public void deleteAll() {
		String sql = super.getPropertyByDatabaseType(AIRLINE_DAO_DELETE_ALL);
		super.executeUpdateByDynamicQuery(sql, null);
	}

	@Override
	public void deleteByPrimaryKey(String airlineCode) {
		String sql = super
				.getPropertyByDatabaseType(AIRLINE_DAO_DELETE_BY_PRIMARY_KEY);
		super.executeUpdateByDynamicQuery(sql, new String[] { airlineCode });
	}

	@Override
	public Airline[] findAll() {
		String sql = super.getPropertyByDatabaseType(AIRLINE_DAO_FIND_ALL);
		List<Airline> results = super.findByDynamicQuery(sql, null,
				Airline.class);
		Airline airlines[] = new Airline[results.size()];
		results.toArray(airlines);

		return airlines[0] == null ? null : airlines;
	}

	@Override
	public Airline findByPrimaryKey(String airlineCode) {
		String sql = super
				.getPropertyByDatabaseType(AIRLINE_DAO_FIND_BY_PRIMARYKEY);

		List<Airline> results = super.findByDynamicQuery(sql,
				new String[] { airlineCode }, Airline.class);
		Airline airlines[] = new Airline[results.size()];
		results.toArray(airlines);

		return airlines[0] == null ? new Airline() : airlines[0];
	}

	@Override
	public void insert(Airline airline) {
		String sql = super.getPropertyByDatabaseType(AIRLINE_DAO_INSERT);

		super.executeUpdateByDynamicQuery(
				sql,
				new String[] { airline.getAirlineCode(),
						airline.getAirlineName() });
	}

	@Override
	public Airline[] matchByAirlineName(String airlineName) {
		String sql = super
				.getPropertyByDatabaseType(AIRLINE_DAO_MATCH_BY_AIRLINE_NAME);
		List<Airline> results = super.findByDynamicQuery(sql,
				new String[] { airlineName }, Airline.class);
		Airline airlines[] = new Airline[results.size()];
		results.toArray(airlines);

		return airlines[0] == null ? new Airline[] {} : airlines;
	}

	@Override
	public void rollback() {
		super.commit();
	}

	@Override
	public void setAutoCommit(boolean autoCommit) {
		super.setAutoCommit(autoCommit);
	}

	@Override
	public void updateByPrimaryKey(Airline airline) {
		String sql = super
				.getPropertyByDatabaseType(AIRLINE_DAO_UPDATE_BY_PRIMARY_KEY);
		super.executeUpdateByDynamicQuery(
				sql,
				new String[] { airline.getAirlineCode(),
						airline.getAirlineName(), airline.getAirlineCode() });
	}

}
