package com.abreqadhabra.nflight.application.launcher;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ProfileImpl implements Profile {
	private static final Class<ProfileImpl> THIS_CLAZZ = ProfileImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	
	public ProfileImpl(Properties props) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				LoggingHelper.describe(THIS_CLAZZ));
	}

}
