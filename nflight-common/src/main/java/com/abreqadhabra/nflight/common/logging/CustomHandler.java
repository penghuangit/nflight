package com.abreqadhabra.nflight.common.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;

import com.abreqadhabra.nflight.common.util.IOStream;

/**
 * MyCustomHandler outputs contents to a specified file
 */
public class CustomHandler extends FileHandler {

	FileOutputStream fileOutputStream;
	PrintWriter printWriter;

	public CustomHandler(String fileName) throws SecurityException, IOException {
		super();

		final String codeBase = IOStream.getCodebase(CustomHandler.class
				.getName());
		final File loggingPath = new File(codeBase + "log");

		System.out.println(codeBase + "log");
		if (!loggingPath.exists()) {
			loggingPath.mkdir();
		}

		fileName = loggingPath.getAbsolutePath() + "/" + fileName;

		// initialize the file
		try {
			this.fileOutputStream = new FileOutputStream(fileName);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		this.printWriter = new PrintWriter(this.fileOutputStream);

	}

	@Override
	public void close() throws SecurityException {
		this.printWriter.close();
	}

	@Override
	public void flush() {
		this.printWriter.flush();
	}

	@Override
	public void publish(final LogRecord record) {
		// ensure that this LogRecord should be logged by this Handler
		if (!this.isLoggable(record)) {
			return;
		}

		// Output the formatted data to the file
		this.printWriter.println(this.getFormatter().format(record));
	}

}