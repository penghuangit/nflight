package examples.logging;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.commons.constant.Env;

public class CustomLoggerExample {

	public CustomLoggerExample() {
		super();
	}

	public static void initializeLogging(String componentName)
			throws SecurityException, IOException {

		// String componentName =
		// "com.abreqadhabra.nflight.example.logging.CustomLoggingExample";
		String resourceBundleName = "com.abreqadhabra.nflight.commons.resources.logging.LoggingMessages";

		String configFile = "/com/abreqadhabra/nflight/commons/resources/config/CustomLogger.xml";

		Logger logger = Logger.getLogger(componentName, resourceBundleName);

		LogManager logManager = LogManager.getLogManager();
		InputStream inputStream = CustomLoggerExample.class
				.getResourceAsStream(configFile);
		logManager.readConfiguration(inputStream);

		// Set up a custom Handler (see MyCustomHandler example)

		File loggingPath = new File(Env.CONFIG.BASE_LOCATION + "/log");

		if (!loggingPath.exists()) {
			loggingPath.mkdir();
		}

		FileHandler fileHandler = new FileHandler(loggingPath.getAbsolutePath()
				+ "/" + resourceBundleName + ".%u.%g.log");

		/*
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