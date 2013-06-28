package com.abreqadhabra.freelec.java.workshop.bookflights.dao;

import java.util.Collection;

import com.abreqadhabra.freelec.java.workshop.bookflights.model.Flight;

public interface BookFlightsDAO {

	
	public Flight  findFlightById(String id) ;
	public Collection getFlights() ;
	public void deleteFlight(String id) ;
//	public Movie createFlight(String rating, String year, String  title) ;
	public void updateFlight(String id, String rating, String year, String title) ;

	
}
