package com.abreqadhabra.nflight.common.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;

import com.abreqadhabra.nflight.commons.constant.Env;

/**
 * MyCustomHandler outputs contents to a specified file
 */
public class CustomHandler extends FileHandler {

	FileOutputStream fileOutputStream;
	PrintWriter printWriter;

	public CustomHandler(String fileName) throws SecurityException,
			IOException {
		super();

		File loggingPath = new File(Env.CONFIG.BASE_LOCATION + "log");

		System.out.println(Env.CONFIG.BASE_LOCATION + "log");
		if (!loggingPath.exists()) {
			loggingPath.mkdir();
		}

		fileName = loggingPath.getAbsolutePath() + "/" + fileName;

		// initialize the file
		try {
			fileOutputStream = new FileOutputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		printWriter = new PrintWriter(fileOutputStream);

	}

	@Override
	public void publish(LogRecord record) {
		// ensure that this LogRecord should be logged by this Handler
		if (!isLoggable(record))
			return;

		// Output the formatted data to the file
		printWriter.println(getFormatter().format(record));
	}

	@Override
	public void flush() {
		printWriter.flush();
	}

	@Override
	public void close() throws SecurityException {
		printWriter.close();
	}

}