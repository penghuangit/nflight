/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.abreqadhabra.nflight.app.dao.dto;

import java.io.Serializable;

public class Airline implements Serializable {

    protected String airlineCode;
    protected String airlineName;

    public Airline() {
    }

    public String getAirlineCode() {
	return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
	this.airlineCode = airlineCode;
    }

    public String getAirlineName() {
	return airlineName;
    }

    public void setAirlineName(String airlineName) {
	this.airlineName = airlineName;
    }

    public String toString() {
	StringBuffer ret = new StringBuffer();
	ret.append(this.getClass().getCanonicalName() + ": \n");
	ret.append("airlineCode=" + airlineCode);
	ret.append(", airlineName=" + airlineName);
	return ret.toString();
    }
}
