package com.abreqadhabra.nflight.application.server.aio;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class SocketServerConfiguraImpl implements Configure {
	private static final Class<SocketServerConfiguraImpl> THIS_CLAZZ = SocketServerConfiguraImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private Properties props = new Properties();

	public SocketServerConfiguraImpl() {
		try {
			props.load(new FileInputStream("com.abreqadhabra.nflight.application.server.aio/conf/SocketServerConfig.properties"));
		} catch (IOException e) {
			LOGGER.severe("...");
		}
	}
	
	@Override
	public String get(String key) {
		return props.getProperty(key);
	}
	@Override
	public void set(String key, String value) {
		props.setProperty(key, value);
	}

}
