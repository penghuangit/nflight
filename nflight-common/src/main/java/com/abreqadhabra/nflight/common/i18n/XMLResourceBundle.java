package com.abreqadhabra.nflight.common.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

public class XMLResourceBundle extends ResourceBundle {
	private final Properties properties;

	XMLResourceBundle(final InputStream stream)
			throws InvalidPropertiesFormatException, IOException {
		this.properties = new Properties();
		this.properties.loadFromXML(stream);
	}

	@Override
	public Enumeration<String> getKeys() {
		final Set<String> handleKeys = this.properties.stringPropertyNames();
		return Collections.enumeration(handleKeys);
	}

	@Override
	protected Object handleGetObject(final String key) {
		return this.properties.getProperty(key);
	}

}