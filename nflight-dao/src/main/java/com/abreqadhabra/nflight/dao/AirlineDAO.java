package com.abreqadhabra.nflight.dao;

import java.sql.SQLException;

import com.abreqadhabra.nflight.common.exception.WrapperException;
import com.abreqadhabra.nflight.dao.dto.Airline;

public interface AirlineDAO {

	/**
	 * Inserts a new row in the AIRLINE table.
	 * 
	 * @return
	 * @throws WrapperException
	 * @throws Exception
	 * @throws SQLException
	 */
	public void insert(Airline airline) throws Exception;

	/**
	 * Updates a single row in the AIRLINE table.
	 * 
	 * @throws WrapperException
	 * @throws Exception
	 * 
	 * @throws SQLException
	 */
	public void updateByPrimaryKey(Airline airline) throws Exception;

	/**
	 * Deletes a single row in the AIRLINE table.
	 * 
	 * @throws WrapperException
	 */
	public void deleteByPrimaryKey(String airlineCode) throws Exception;

	public void deleteAll() throws Exception;

	/**
	 * Returns all rows from the AIRLINE table that match the criteria
	 * 'AIRLINE_CODE = :airlineCode'.
	 * 
	 * @throws WrapperException
	 */
	public Airline findByPrimaryKey(String airlineCode) throws Exception;

	/**
	 * Returns all rows from the AIRLINE table that match the criteria
	 * 'AIRLINE_NAME = :airlineName'.
	 * 
	 * @throws WrapperException
	 * @throws Exception
	 */
	public Airline[] matchByAirlineName(String airlineName) throws Exception;

	/**
	 * Returns all rows from the AIRLINE table that match the criteria ''.
	 * 
	 * @throws WrapperException
	 */
	public Airline[] findAll() throws Exception;

	public void setAutoCommit(boolean autoCommit) throws Exception;

	public void commit() throws Exception;

	public void rollback() throws Exception;

}
