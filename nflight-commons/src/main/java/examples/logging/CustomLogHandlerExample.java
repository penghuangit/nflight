package examples.logging;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomLogHandlerExample {

	public static void main(String[] args) {

		Logger logger = Logger.getLogger(CustomLogHandlerExample.class
				.getCanonicalName());

		Handler customHandler = new CustomLogHandler();

		logger.addHandler(customHandler);

		logger.log(Level.INFO, "MSG_KEY_01", "가나다");
		logger.log(Level.SEVERE, "MSG_KEY_01", "가나다");
		logger.info("This is a test INFO message");
		logger.warning("This is a test WARNING message");
		logger.logp(Level.SEVERE, "MyCustomLogging", "main",
				"This is a test SEVERE message");
	}
}