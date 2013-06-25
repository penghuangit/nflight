package com.abreqadhabra.nflight.common.logging;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggingHelper extends Logger {

    private static final Class<LoggingHelper> THIS_CLASS = LoggingHelper.class;

    private static final String LOGGING_RESOURCE_BUNDLE_NAME = "com.abreqadhabra.nflight.commons.resources.logging.LoggingMessages";
    // private static final String LOGGING_CONFIG_FILE =
    // "/com/abreqadhabra/nflight/commons/resources/config/CustomLogger.xml";
    private static final String LOGGING_CONFIG_FILE_NAME = "/com/abreqadhabra/nflight/common/resources/conf/logging.properties";

    /** Root Logger */
    private static final Logger ROOT_LOGGER = Logger.getLogger("");

    protected LoggingHelper(String name, String resourceBundleName) {
	super(name, resourceBundleName);
    }

    /**
     * Reloads a logging configuration properties file for the LogManager as
     * described in <code>java.util.logging.LogManager</code>
     * 
     * @param resourceName
     *            The name of a properties file resource that is in the
     *            classpath.
     */
    public static void reload(final String resourceName) {

    }

    public static Logger getLogger(String componentName) {

	ROOT_LOGGER.setLevel(Level.ALL);
	ROOT_LOGGER
		.info("initializing - trying to load configuration file ...");

	LogManager logManager = LogManager.getLogManager();
	logManager.reset();

	try {
	    LogManager.getLogManager().readConfiguration(
	    	THIS_CLASS.getResourceAsStream(LOGGING_CONFIG_FILE_NAME));

	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (SecurityException e) {
	    ROOT_LOGGER.log(Level.WARNING,
		    "Failed to configure log manager due to an access problem",
		    e);
	} catch (IOException e) {
	    ROOT_LOGGER.log(Level.WARNING,
		    "Failed to configure log manager due to an IO problem", e);
	} catch (Exception e) {
	    ROOT_LOGGER
		    .log(Level.WARNING,
			    "Failed to configure log manager due to an unknown problem",
			    e);
	}
	

	Logger logger = Logger.getLogger(componentName,
		LOGGING_RESOURCE_BUNDLE_NAME);

	logManager.addLogger(logger);

	ROOT_LOGGER.info("starting " + componentName);

	return logger;
    }

    public static Logger CLASS_NAME(Class<?> clazz) {
	final String LOGGING_METHOD_NAME = "getLogger(Class<?> clazz)";
	final String componentName = THIS_CLASS.getPackage().getName();
	// ROOT_LOGGER.setLevel(Level.ALL);

	ROOT_LOGGER
		.entering(THIS_CLASS.getCanonicalName(), LOGGING_METHOD_NAME);

	Logger logger = Logger.getLogger(componentName,
		LOGGING_RESOURCE_BUNDLE_NAME);

	System.out.println("logger\t" + ROOT_LOGGER.getLevel());
	ROOT_LOGGER.logp(Level.INFO, THIS_CLASS.getSimpleName(),
		LOGGING_METHOD_NAME, "1-->ROOT_LOGGER.logp ");

	ROOT_LOGGER.exiting(THIS_CLASS.getCanonicalName(), LOGGING_METHOD_NAME,
		logger);

	/*
	 * ROOT_LOGGER.logp(Level.INFO, THIS_CLASS.getName(),
	 * LOGGING_METHOD_NAME, "1-->ROOT_LOGGER.logp " +
	 * ROOT_LOGGER.getName());
	 */

	/*
	 * System.out.println("ROOT_LOGGER" + ROOT_LOGGER);
	 */

	LogManager logManager = LogManager.getLogManager();
	InputStream inputStream = THIS_CLASS
		.getResourceAsStream(LOGGING_CONFIG_FILE_NAME);

	try {
	    logManager.readConfiguration(inputStream);

	} catch (SecurityException | IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	/*
	 * try { ROOT_LOGGER.info("2-->" + ROOT_LOGGER.getName()); if
	 * (inputStream != null) { logManager.readConfiguration(inputStream); }
	 * else { ROOT_LOGGER.logp(Level.SEVERE, THIS_CLASS.getCanonicalName(),
	 * LOGGING_METHOD_NAME, "RE001: NullPointerException"); throw new
	 * CommonException(THIS_CLASS.getCanonicalName(), "RE001",
	 * "NullPointerException"); }
	 * 
	 * } catch (SecurityException se) { throw new
	 * CommonException(THIS_CLASS.getCanonicalName(), "RE001",
	 * "SecurityException", se.getCause()); } catch (IOException ioe) {
	 * throw new CommonException(THIS_CLASS.getCanonicalName(), "RE001",
	 * "IOException", ioe.getCause());
	 * 
	 * } catch (Exception e) { throw new
	 * CommonException(THIS_CLASS.getCanonicalName(), "RE001", "Exception",
	 * e.getCause()); }
	 */

	return logger;
    }

}