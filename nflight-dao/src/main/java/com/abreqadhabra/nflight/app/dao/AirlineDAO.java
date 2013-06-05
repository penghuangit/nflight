package com.abreqadhabra.nflight.app.dao;

import java.sql.SQLException;

import com.abreqadhabra.nflight.app.dao.dto.Airline;

public interface AirlineDAO {

    /**
     * Inserts a new row in the AIRLINE table.
     * 
     * @return
     * @throws SQLException
     */
    public void insert(Airline airline);

    /**
     * Updates a single row in the AIRLINE table.
     * 
     * @throws SQLException
     */
    public void updateByPrimaryKey(Airline airline);

    /**
     * Deletes a single row in the AIRLINE table.
     */
    public void deleteByPrimaryKey(String airlineCode);

    public void deleteAll();

    /**
     * Returns all rows from the AIRLINE table that match the criteria
     * 'AIRLINE_CODE = :airlineCode'.
     */
    public Airline findByPrimaryKey(String airlineCode);

    /**
     * Returns all rows from the AIRLINE table that match the criteria
     * 'AIRLINE_NAME = :airlineName'.
     */
    public Airline[] matchByAirlineName(String airlineName);

    /**
     * Returns all rows from the AIRLINE table that match the criteria ''.
     */
    public Airline[] findAll();

    public void setAutoCommit(boolean autoCommit);

    public void commit();

    public void rollback();

}
