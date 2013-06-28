package com.abreqadhabra.nflight.db.server;


public class JavaDBUtil {

	public static final String STRING_DERBY_EMBEDED_DRIVER_NAME = "org.apache.derby.jdbc.EmbeddedDriver";

	
	public static void loadJDBCDriver() {
		// load Derby driver
		try {
			Class.forName(
					STRING_DERBY_EMBEDED_DRIVER_NAME)
					.newInstance();
			// Get a connection
		} catch (ClassNotFoundException cnfe) {
			System.err
					.println("\nUnable to load the JDBC driver "
							+ STRING_DERBY_EMBEDED_DRIVER_NAME);
			System.err.println("Please check your CLASSPATH.");
			cnfe.printStackTrace(System.err);
		} catch (InstantiationException ie) {
			System.err
					.println("\nUnable to instantiate the JDBC driver "
							+ STRING_DERBY_EMBEDED_DRIVER_NAME);
			ie.printStackTrace(System.err);
		} catch (IllegalAccessException iae) {
			System.err
					.println("\nNot allowed to access the JDBC driver "
							+ STRING_DERBY_EMBEDED_DRIVER_NAME);
			iae.printStackTrace(System.err);
		}
	}
}
