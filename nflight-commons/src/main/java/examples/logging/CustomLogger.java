package examples.logging;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class CustomLogger extends Logger {

	protected CustomLogger(String name, String resourceBundleName) {
		super(name, resourceBundleName);
	}


	@SuppressWarnings("rawtypes")
	public static Logger getLogger(Class clazz, String configurationPath,
			String fileType) throws Exception {

		LogManager logManager = getCustomLogManager(clazz, configurationPath,
				fileType);

		String componentName = clazz.getSimpleName();

		// Get the logger which we wish to attach a custom Handler to String
		Logger logger = logManager.getLogger(componentName);// ,
															// resourceBundleName);

		// Set up a custom Handler (see MyCustomHandler example)
		// Handler handler = new CustomLoggingHandler("MyOutputFile.log");

		return logger;

	}

	@SuppressWarnings("rawtypes")
	public static LogManager getCustomLogManager(Class clazz,
			String configurationPath, String resourceType) throws Exception {

		LogManager logManager = LogManager.getLogManager();

		String fileExtension = null;

		if ("xml".equals(resourceType)) {
			fileExtension = ".xml";
		} else if ("xml".equals(resourceType)) {
			fileExtension = ".properties";
		} else {
			throw new Exception("Invalid resource file type");
		}

		try {
			String resourceName = configurationPath + clazz.getSimpleName()
					+ fileExtension;

			System.out.println(resourceName);

			InputStream inputStream = clazz.getResourceAsStream(resourceName);
			logManager.readConfiguration(inputStream);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return logManager;
	}

	public static void main(String[] args) {

		String configurationPath = "/com/abreqadhabra/nflight/commons/resources/config/";

		System.out.println(configurationPath);

		try {

			Logger logger = Logger
					.getLogger(CustomLogger.class.getSimpleName(),
							"com.abreqadhabra.nflight.commons.resources.logging.LoggingMessages");

			LogRecord logRecord = new LogRecord(Level.SEVERE, "MSG_KEY_01");

			logger.log(Level.SEVERE, "MSG_KEY_01", "가나다");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}