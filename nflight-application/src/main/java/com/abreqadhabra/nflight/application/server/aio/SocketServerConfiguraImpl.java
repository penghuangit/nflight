package com.abreqadhabra.nflight.application.server.aio;

import java.util.Properties;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.Configuration;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;

public class SocketServerConfiguraImpl implements Configure {
	private static final Class<SocketServerConfiguraImpl> THIS_CLAZZ = SocketServerConfiguraImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private Properties props = new Properties();

	public SocketServerConfiguraImpl() {
		try {
			props = PropertyFile
					.readPropertyFilePath(Configuration.FILE_SOCKET_SERVER_PROPERTIES);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
