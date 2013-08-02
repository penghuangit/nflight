package com.abreqadhabra.nflight.application.launcher;

import java.nio.file.Path;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;

public class ConfigureImpl implements Configure {
	private static final Class<ConfigureImpl> THIS_CLAZZ = ConfigureImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private Properties props = new Properties();

	public ConfigureImpl(Path path) {
		try {
			props = PropertyFile
					.readPropertyFilePath(path);
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

	@Override
	public Hashtable<Object, Object> all() {
		return (Hashtable<Object, Object>) props;
	}
}
