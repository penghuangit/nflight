package com.abreqadhabra.nflight.commons.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

public class XMLResourceBundle extends ResourceBundle {
	private Properties properties;

	XMLResourceBundle(InputStream stream)
			throws InvalidPropertiesFormatException, IOException {
		properties = new Properties();
		properties.loadFromXML(stream);
	}

	@Override
	protected Object handleGetObject(String key) {
		return properties.getProperty(key);
	}

	@Override
	public Enumeration<String> getKeys() {
		Set<String> handleKeys = properties.stringPropertyNames();
		return Collections.enumeration(handleKeys);
	}

}