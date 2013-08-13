package com.abreqadhabra.nflight.common.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import com.abreqadhabra.nflight.common.util.Misc;

/**
 * MyCustomFormatter formats the LogRecord as follows: date level localized
 * message with parameters
 */
public class CustomFormatter extends Formatter {

	public CustomFormatter() {
		super();
	}

	@Override
	public String format(LogRecord record) {
		// Create a StringBuffer to contain the formatted record
		// start with the date.
		StringBuffer sb = new StringBuffer();

		sb.append("\n");
		// Get the date from the LogRecord and add it to the buffer
		String dateTime = Misc.getDateTime();
		sb.append(dateTime);
		sb.append(": ");

		// Get the level name and add it to the buffer
		String leveName = String.format("%9s", "["
				+ record.getLevel().getName() + "]");
		// System.out.printf("%-30s : %50s%n", prop.getKey(), prop.getValue());
		sb.append(leveName);
		sb.append(" ");

		// Get the formatted message (includes localization
		// and substitution of paramters) and add it to the buffer
		sb.append(this.formatMessage(record));

		return sb.toString();
	}

}