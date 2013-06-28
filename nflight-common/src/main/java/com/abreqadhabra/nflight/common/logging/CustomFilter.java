package com.abreqadhabra.nflight.common.logging;

import java.util.Vector;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

/**
 * CustomFilter rejects any LogRecords whose Level is not contained in the
 * configured list of Levels.
 */
public class CustomFilter implements Filter {

	private Vector<?> acceptableLevels;

	public CustomFilter(Vector<?> acceptableLevels) {
		super();
		this.acceptableLevels = acceptableLevels;
	}

	public boolean isLoggable(LogRecord record) {
		return (acceptableLevels.contains(record.getLevel()));
	}

}