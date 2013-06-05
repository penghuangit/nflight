package examples.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class CustomLogHandler extends Handler {

	String logMessages = new String("");
	DateFormat df = DateFormat.getDateTimeInstance();

	public void publish(LogRecord logRecord) {
		Date date = new Date(logRecord.getMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss:SSS");

		String dateString = dateFormat.format(date);
		logMessages = logRecord.getSequenceNumber() + " " + dateString + " "
				+ logRecord.getLevel() + " " + logRecord.getMessage() + " "
				+ logRecord.getSourceClassName() + " "
				+ logRecord.getSourceMethodName() + "\n";
		System.out.println(logMessages);
	}

	public void flush() {
	}

	public void close() {
		System.out.println("\nEnd of log");
	}

}
