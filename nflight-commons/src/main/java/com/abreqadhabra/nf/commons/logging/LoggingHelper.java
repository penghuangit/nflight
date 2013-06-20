package com.abreqadhabra.nf.commons.logging;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggingHelper extends Logger {

    private static final Class THIS_CLASS = LoggingHelper.class;

    private static final String LOGGING_RESOURCE_BUNDLE_NAME = "com.abreqadhabra.nflight.commons.resources.logging.LoggingMessages";
    private static final String LOGGING_CONFIG_FILE = "/com/abreqadhabra/nflight/commons/resources/config/CustomLogger.xml";

    private static Logger LOGGER = Logger.getLogger(THIS_CLASS
	    .getCanonicalName());

    protected LoggingHelper(String name, String resourceBundleName) {
	super(name, resourceBundleName);
    }

    public static Logger getLogger(Class<?> clazz) {       
	final String LOGGING_METHOD_NAME = "getLogger(Class<?> clazz)";
	final String componentName = THIS_CLASS.getPackage().getName();
	LOGGER.setLevel(Level.ALL);
	
	LOGGER.entering(THIS_CLASS.getCanonicalName(), LOGGING_METHOD_NAME);

	Logger logger = Logger.getLogger(componentName,
		LOGGING_RESOURCE_BUNDLE_NAME);
	
	System.out.println("logger\t" + LOGGER.getLevel());
	LOGGER.logp(Level.INFO, THIS_CLASS.getSimpleName(), LOGGING_METHOD_NAME,
		"1-->LOGGER.logp ");
	
	LOGGER.exiting(THIS_CLASS.getCanonicalName(), LOGGING_METHOD_NAME, logger);

	
/*	LOGGER.logp(Level.INFO, THIS_CLASS.getName(), LOGGING_METHOD_NAME,
		"1-->LOGGER.logp " + LOGGER.getName());*/


	
	

	
	/*
	System.out.println("LOGGER" + LOGGER);
*/
	


	    LogManager logManager = LogManager.getLogManager();
	    InputStream inputStream = THIS_CLASS
		    .getResourceAsStream(LOGGING_CONFIG_FILE);

		try {
		    logManager.readConfiguration(inputStream);
		    
		
		} catch (SecurityException | IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}


	    
/*		try {
	    LOGGER.info("2-->" + LOGGER.getName());
	    	    if (inputStream != null) {
		logManager.readConfiguration(inputStream);
	    } else {
		LOGGER.logp(Level.SEVERE, THIS_CLASS.getCanonicalName(),
			LOGGING_METHOD_NAME, "RE001: NullPointerException");
		throw new CommonException(THIS_CLASS.getCanonicalName(),
			"RE001", "NullPointerException");
	    }

	} catch (SecurityException se) {
	    throw new CommonException(THIS_CLASS.getCanonicalName(), "RE001",
		    "SecurityException", se.getCause());
	} catch (IOException ioe) {
	    throw new CommonException(THIS_CLASS.getCanonicalName(), "RE001",
		    "IOException", ioe.getCause());

	} catch (Exception e) {
	    throw new CommonException(THIS_CLASS.getCanonicalName(), "RE001",
		    "Exception", e.getCause());
	}*/

	return logger;
    }

}