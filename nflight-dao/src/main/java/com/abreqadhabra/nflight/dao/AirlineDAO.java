package com.abreqadhabra.nflight.dao;

import java.sql.SQLException;

import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.dao.dto.Airline;

public interface AirlineDAO {

	/**
	 * Inserts a new row in the AIRLINE table.
	 * 
	 * @return
	 * @throws NFlightException
	 * @throws Exception
	 * @throws SQLException
	 */
	public void insert(Airline airline) throws Exception;

	/**
	 * Updates a single row in the AIRLINE table.
	 * 
	 * @throws NFlightException
	 * @throws Exception
	 * 
	 * @throws SQLException
	 */
	public void updateByPrimaryKey(Airline airline) throws Exception;

	/**
	 * Deletes a single row in the AIRLINE table.
	 * 
	 * @throws NFlightException
	 */
	public void deleteByPrimaryKey(String airlineCode) throws Exception;

	public void deleteAll() throws Exception;

	/**
	 * Returns all rows from the AIRLINE table that match the criteria
	 * 'AIRLINE_CODE = :airlineCode'.
	 * 
	 * @throws NFlightException
	 */
	public Airline findByPrimaryKey(String airlineCode) throws Exception;

	/**
	 * Returns all rows from the AIRLINE table that match the criteria
	 * 'AIRLINE_NAME = :airlineName'.
	 * 
	 * @throws NFlightException
	 * @throws Exception
	 */
	public Airline[] matchByAirlineName(String airlineName) throws Exception;

	/**
	 * Returns all rows from the AIRLINE table that match the criteria ''.
	 * 
	 * @throws NFlightException
	 */
	public Airline[] findAll() throws Exception;

	public void setAutoCommit(boolean autoCommit) throws Exception;

	public void commit() throws Exception;

	public void rollback() throws Exception;

}
