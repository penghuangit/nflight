package com.abreqadhabra.nflight.examples.common.logging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.util.IOStream;


public class CustomLoggerExample {
	private static Class<CustomLoggerExample> THIS_CLAZZ = CustomLoggerExample.class;
	
	private static String resourceBundleName = "com.abreqadhabra.nflight.examples.common.conf.LoggingMessages";
	private static String configFile = "com/abreqadhabra/nflight/examples/common/conf/logging.properties";

	public static void initializeLogging(String componentName)
			throws SecurityException, IOException {

		Logger logger = Logger.getLogger(componentName, resourceBundleName);

		LogManager logManager = LogManager.getLogManager();
		
		Path configPath = IOStream.getFilePath(THIS_CLAZZ.getName(),configFile);
		
		logManager.readConfiguration(Files.newInputStream(configPath, StandardOpenOption.READ));

		// Set up a custom Handler (see MyCustomHandler example)

		Path logFilePath = IOStream.getFilePath(THIS_CLAZZ.getName(), "log");
		
		if (Files.notExists(logFilePath, new LinkOption[] {LinkOption.NOFOLLOW_LINKS})) {
			Files.createDirectory(logFilePath);
		}

		FileHandler fileHandler = new FileHandler(logFilePath.toString()
				+ "/" + THIS_CLAZZ.getSimpleName()+".%u.%g.log");
		
		logger.info(logFilePath.toString()
				+ "/" + THIS_CLAZZ.getSimpleName()+".%u.%g.log");
/*
		/
		 * // Set up a custom Filter (see MyCustomFilter example) Vector<Level>
		 * acceptableLevels = new Vector<Level>();
		 * acceptableLevels.add(Level.INFO); acceptableLevels.add(Level.SEVERE);
		 * Filter filter = new CustomFilter(acceptableLevels);
		 * 
		 * // Set up a custom Formatter (see MyCustomFormatter example)
		 * Formatter formatter = new CustomFormatter();
		 * 
		 * // Connect the filter and formatter to the handler
		 * handler.setFilter(filter); handler.setFormatter(formatter);
		 */
		// Connect the handler to the logger
		logger.addHandler(fileHandler);

		// avoid sending events logged to com.myCompany showing up in WebSphere
		// Application Server logs
		logger.setUseParentHandlers(false);

	}

	public static void main(String[] args) {

		try {
			initializeLogging(CustomLoggerExample.class.getCanonicalName());
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Logger logger = Logger.getLogger(CustomLoggerExample.class
				.getCanonicalName());

		logger.log(Level.INFO, "MSG_KEY_01", "가나다");

		logger.log(Level.SEVERE, "MSG_KEY_01", "가나다");
		logger.info("This is a test INFO message");
		logger.warning("This is a test WARNING message");
		logger.logp(Level.SEVERE, "MyCustomLogging", "main",
				"This is a test SEVERE message");
	}
}