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
			this.props = PropertyFile.readPropertyFilePath(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String get(String key) {
		return this.props.getProperty(key).trim();
	}

	@Override
	public void set(String key, String value) {
		this.props.setProperty(key, value);
	}

	@Override
	public Hashtable<Object, Object> getAll() {
		return this.props;
	}

	@Override
	public int getInt(String key) {
		return Integer.parseInt(this.get(key));
	}
}
