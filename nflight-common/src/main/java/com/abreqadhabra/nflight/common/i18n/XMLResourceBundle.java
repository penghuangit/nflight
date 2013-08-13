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
	private Properties properties;

	XMLResourceBundle(InputStream stream)
			throws InvalidPropertiesFormatException, IOException {
		this.properties = new Properties();
		this.properties.loadFromXML(stream);
	}

	@Override
	public Enumeration<String> getKeys() {
		Set<String> handleKeys = this.properties.stringPropertyNames();
		return Collections.enumeration(handleKeys);
	}

	@Override
	protected Object handleGetObject(String key) {
		return this.properties.getProperty(key);
	}

}