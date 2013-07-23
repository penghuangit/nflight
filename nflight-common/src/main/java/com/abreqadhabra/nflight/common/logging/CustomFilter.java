package com.abreqadhabra.nflight.common.logging;

import java.util.Vector;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

/**
 * CustomFilter rejects any LogRecords whose Level is not contained in the
 * configured list of Levels.
 */
public class CustomFilter implements Filter {

	private final Vector<?> acceptableLevels;

	public CustomFilter(final Vector<?> acceptableLevels) {
		super();
		this.acceptableLevels = acceptableLevels;
	}

	@Override
	public boolean isLoggable(final LogRecord record) {
		return (this.acceptableLevels.contains(record.getLevel()));
	}

}