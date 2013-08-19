package com.abreqadhabra.nflight.application.trash_server;

import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.IOStream;
import com.abreqadhabra.nflight.common.util.PropertyLoader;

public class ConfigureImpl implements Configure {
	
	
	private static Class<ConfigureImpl> THIS_CLAZZ = ConfigureImpl.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();

	
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private Properties props = new Properties();

	public ConfigureImpl(Class<?> clazz, Path path) {
		try {
			Path codebasePath = IOStream.getCodebasePath(clazz.getName());
			path = codebasePath.resolve(path);
			PropertyLoader.load(CLAZZ_NAME, path.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String get(String key) {
		return System.getProperty(key).trim();
	}

	public static void set(String key, String value) {
		System.setProperty(key, value);
	}

	public static Properties getProperties() {
		return System.getProperties();
	}

	public static int getInt(String key) {
		return Integer.parseInt(get(key));
	}

	public static boolean getBoolean(String key) {
		return Boolean.valueOf(get(key)).booleanValue();
	}

}
