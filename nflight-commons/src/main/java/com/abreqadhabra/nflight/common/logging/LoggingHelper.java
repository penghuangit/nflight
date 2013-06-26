package com.abreqadhabra.nflight.common.logging;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import java.util.logging.ConsoleHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggingHelper extends Logger {
    
    private static final Class<LoggingHelper> THIS_CLAZZ = LoggingHelper.class;
    /** Root Logger */
    private static final Logger ROOT_LOGGER = Logger.getLogger("");
    
    private static final String DEFAULT_RESOURCE_BUNDLE_NAME = "com.abreqadhabra.nflight.commons.resources.logging.LoggingMessages";

    
    public static Logger getLogger(Class<?> clazz) {
	return getLogger(clazz, null);
    }

    public static Logger getLogger(Class<?> clazz, String resourceBundleName) {
	if (resourceBundleName == null) {
	    resourceBundleName = DEFAULT_RESOURCE_BUNDLE_NAME;
	}
	String loggerName = getPackageName(clazz, 4);
	
	
	Logger _logger = Logger.getLogger(loggerName, resourceBundleName);

	LogManager logManager = LogManager.getLogManager();
	InputStream inputStream = THIS_CLAZZ
		.getResourceAsStream(LOGGING_CONFIG_FILE_NAME);

	try {
	    logManager.readConfiguration(inputStream);
	    inputStream.close();
	} catch (FileNotFoundException e) {
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
	logManager.addLogger(_logger);

	return logManager.getLogger(loggerName);
    }


    private static String getPackageName(Class<?> clazz, int depth) {
	final String METHOD_NAME = "getLoggerName";

	String packageName = clazz.getPackage().getName();
	
	StringBuffer sb = new StringBuffer(); 
		
	String [] strArray =  packageName.split("\\.");
		 
	System.out.println(strArray.length);
	
	if (strArray.length >= depth) {
	    for (int i = 0; i < depth; i++) {
		sb.append(strArray[i]);

		if(i < depth -1){
		    sb.append(".");
		}
	    }
	}
	//packageName.substring(0, endIndex)
	
	System.out.println(sb.toString());

	
	ROOT_LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME, sb.toString());
	
	return sb.toString();
    }












    private static final String LOGGING_RESOURCE_BUNDLE_NAME = "com.abreqadhabra.nflight.commons.resources.logging.LoggingMessages";
    // private static final String LOGGING_CONFIG_FILE =
    // "/com/abreqadhabra/nflight/commons/resources/config/CustomLogger.xml";
    private static final String LOGGING_CONFIG_FILE_NAME = "/com/abreqadhabra/nflight/common/resources/conf/logging.properties";



    protected LoggingHelper(String name, String resourceBundleName) {
	super(name, resourceBundleName);
    }


    
    public static void initializeLogging(String componentName) {

	// Get the logger that you want to attach a custom Handler to
	String defaultResourceBundleName = LOGGING_RESOURCE_BUNDLE_NAME;
	Logger logger = Logger.getLogger(componentName,
		defaultResourceBundleName);

	// Set up a custom Handler (see MyCustomHandler example)
	Handler handler = null;

	// handler = new CustomHandler("MyOutputFile.log");
	handler = new ConsoleHandler();

	// Set up a custom Filter (see MyCustomFilter example)
	Vector acceptableLevels = new Vector();
	acceptableLevels.add(Level.ALL);
	acceptableLevels.add(Level.CONFIG);
	acceptableLevels.add(Level.FINE);
	acceptableLevels.add(Level.FINER);
	acceptableLevels.add(Level.FINEST);
	acceptableLevels.add(Level.INFO);
	acceptableLevels.add(Level.WARNING);
	acceptableLevels.add(Level.SEVERE);

	Filter filter = new CustomFilter(acceptableLevels);

	// Set up a custom Formatter (see MyCustomFormatter example)
	Formatter formatter = new CustomFormatter();

	// Connect the filter and formatter to the handler
	handler.setFilter(filter);
	handler.setFormatter(formatter);

	// Connect the handler to the logger
	logger.addHandler(handler);

	// avoid sending events logged to com.myCompany showing up in WebSphere
	// Application Server logs
	logger.setUseParentHandlers(false);

    }
    

    public static Logger getLogger(String componentName) {

	ROOT_LOGGER.setLevel(Level.ALL);
	ROOT_LOGGER
		.info("initializing - trying to load configuration file ...");

	LogManager logManager = LogManager.getLogManager();

	
	final InputStream inputStream = THIS_CLAZZ.getResourceAsStream(LOGGING_CONFIG_FILE_NAME);

	
	// System.out.println(inputStream);
	 
/*	  // Load the properties
	Properties props = new Properties ();
	try {
	    props.load(inputStream);
		 System.out.println(props.toString());

	} catch (IOException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
        */
        
	try {
	    logManager.readConfiguration(inputStream);

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

//	initializeLogging(componentName);
	Logger logger = Logger.getLogger(componentName, LOGGING_RESOURCE_BUNDLE_NAME);

	logManager.addLogger(logger);

	
	System.out.println("ROOT_LOGGER:" + ROOT_LOGGER.getLevel());

	ROOT_LOGGER.info("starting " + componentName);

	return logger;
    }

    public static Logger CLASS_NAME(Class<?> clazz) {
	final String LOGGING_METHOD_NAME = "getLogger(Class<?> clazz)";
	final String componentName = THIS_CLAZZ.getPackage().getName();
	// ROOT_LOGGER.setLevel(Level.ALL);

	ROOT_LOGGER
		.entering(THIS_CLAZZ.getCanonicalName(), LOGGING_METHOD_NAME);

	Logger logger = Logger.getLogger(componentName,
		LOGGING_RESOURCE_BUNDLE_NAME);

	System.out.println("logger\t" + ROOT_LOGGER.getLevel());
	ROOT_LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(),
		LOGGING_METHOD_NAME, "1-->ROOT_LOGGER.logp ");

	ROOT_LOGGER.exiting(THIS_CLAZZ.getCanonicalName(), LOGGING_METHOD_NAME,
		logger);

	/*
	 * ROOT_LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(),
	 * LOGGING_METHOD_NAME, "1-->ROOT_LOGGER.logp " +
	 * ROOT_LOGGER.getName());
	 */

	/*
	 * System.out.println("ROOT_LOGGER" + ROOT_LOGGER);
	 */

	LogManager logManager = LogManager.getLogManager();
	InputStream inputStream = THIS_CLAZZ
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
	 * else { ROOT_LOGGER.logp(Level.SEVERE, THIS_CLAZZ.getCanonicalName(),
	 * LOGGING_METHOD_NAME, "RE001: NullPointerException"); throw new
	 * CommonException(THIS_CLAZZ.getCanonicalName(), "RE001",
	 * "NullPointerException"); }
	 * 
	 * } catch (SecurityException se) { throw new
	 * CommonException(THIS_CLAZZ.getCanonicalName(), "RE001",
	 * "SecurityException", se.getCause()); } catch (IOException ioe) {
	 * throw new CommonException(THIS_CLAZZ.getCanonicalName(), "RE001",
	 * "IOException", ioe.getCause());
	 * 
	 * } catch (Exception e) { throw new
	 * CommonException(THIS_CLAZZ.getCanonicalName(), "RE001", "Exception",
	 * e.getCause()); }
	 */

	return logger;
    }

}