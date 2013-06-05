package examples.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingExample {

	// Create the logger
	private static final Logger logger = Logger.getLogger(LoggingExample.class.getName());

	// private static final Logger logger = Logger.getLogger("com.abreqadhabra.nflight.example.logging.LoggingExample");

	public static void main(String[] args) {
		logger.info(LoggingExample.class.getName());
		// Configure the logger
		logger.addHandler(new ConsoleHandler());
		logger.setLevel(Level.FINE);
		// Log messages at different levels
		// There are seven different levels for logging;
		logger.severe("Level:Severe ");
		logger.warning("Level:Warning");
		logger.log(Level.WARNING, "Another way to log a warning message");
		logger.info("Level:info");
		logger.config("Level:configmessage");
		logger.fine("Level:fine");
		logger.log(Level.FINE, "Another way to log a finest message");
		logger.finer("Level:finer");
		logger.finest("Level:finest");
	}
}
