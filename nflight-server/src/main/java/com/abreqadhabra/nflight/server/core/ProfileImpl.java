package com.abreqadhabra.nflight.server.core;

import java.util.Properties;

/**
 * This class allows the JADE core to retrieve configuration-dependent classes
 * and boot parameters.
 * <p>
 * Take care of using different instances of this class when launching different
 * containers/main-containers on the same JVM otherwise they would conflict!
 * 
 * 
 */
public class ProfileImpl extends Profile {

	private Properties props = null;
	protected Properties bootProps = null;

	/**
	 * Creates a Profile implementation using the given properties to configure
	 * the platform startup process.
	 * 
	 * @param aProp
	 *            The names and values of the configuration properties to use.
	 */
	public ProfileImpl(Properties props) {
		this.props = props;
		init();
	}

	public ProfileImpl(String string) {
		// TODO Auto-generated constructor stub
	}

	private void init() {
		// TODO Auto-generated method stub

	}

	
	
	public boolean compareToProperty(String key, String option) {
		String value = props.getProperty(key);
		if (key == null) {
			return false;
		} else {
			if (option.equalsIgnoreCase(value)) {
				return true;
			} else 
				return false;
			}
	}
	
	/**
	 * Retrieve a boolean value for a configuration property. If no
	 * corresponding property is found or if its string value cannot be
	 * converted to a boolean one, a default value is returned.
	 * 
	 * @param key
	 *            The key identifying the parameter to be retrieved among the
	 *            configuration properties.
	 * @param isD
	 *            The value to return when there is no property set for the
	 *            given key, or its value cannot be converted to a boolean
	 *            value.
	 */
	public boolean getBooleanProperty(String key, boolean isDefault) {
		String value = props.getProperty(key);
		if (key == null) {
			return isDefault;
		} else {
			if ("true".equalsIgnoreCase(value)) {
				return true;
			} else if ("false".equalsIgnoreCase(value)) {
				return false;
			} else {
				return isDefault;
			}
		}
	}

}
