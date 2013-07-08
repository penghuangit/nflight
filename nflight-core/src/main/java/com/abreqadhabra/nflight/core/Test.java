package com.abreqadhabra.nflight.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import com.abreqadhabra.nflight.core.exception.NFlightProfileException;

public class Test {

	/**
	 * @param args
	 * @throws NFlightProfileException 
	 */
	public static void main(String[] args) throws NFlightProfileException {
	String arg = "rmiserver:com.abreqadhabra.nflight.server.rmi.RMIServer;startup";
	parseSpecifiers(arg, ":");
	}

	/**
	 * Parse a String reading for a set of
	 * <code>parameter(arg)</code>
	 * each delimited by a <code>;</code> and no space in between.
	 * <p>
	 * For instance
	 * <code>jade.mtp.iiop(50);http.mtp.http(8080)</code> is a valid
	 * string, while  <code>jade.mtp.iiop(50 80);http.mtp.http(8080)</code>
	 * is not valid
	 * For each object specifier, a new java object <code>Specifier</code>
	 * is added to the passed <code>out</code> List parameter.
	 */
	public static void parseSpecifiers(String str, String delimiter) throws NFlightProfileException {
		
		int       index1 = str.indexOf(':');
		int       index2 = str.indexOf(';');
		
		// Cursor on the given string: marks the parser
		// position
		int cursor = 0;
		String serviceName = str.substring(cursor, index1);
		String serviceClass = str.substring(index1 + 1, index2);
		String serviceCommand = str.substring(index2 + 1, str.length());

		System.out.println(serviceName + ":" + serviceClass +":"+ serviceCommand);

}
}
