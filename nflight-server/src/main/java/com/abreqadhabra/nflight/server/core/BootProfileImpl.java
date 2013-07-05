package com.abreqadhabra.nflight.server.core;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.server.Bootstrap;
import com.abreqadhabra.nflight.server.core.exception.NFlightProfileException;

/**
 * This class allows the JADE core to retrieve configuration-dependent classes
 * and boot parameters.
 * <p>
 * Take care of using different instances of this class when launching different
 * containers/main-containers on the same JVM otherwise they would conflict!
 * 
 * 
 */
public class BootProfileImpl extends Profile {

	private static final Class<BootProfileImpl> THIS_CLAZZ = BootProfileImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	
	private Properties props = new Properties();
	protected Properties bootProps = null;

	/**
	 * Creates a Profile implementation using the given properties to configure
	 * the platform startup process.
	 * 
	 * @param aProp
	 *            The names and values of the configuration properties to use.
	 */
	public BootProfileImpl(Properties props) {
		this.props = props;
		init();
	}

	public BootProfileImpl(String string) {
		// TODO Auto-generated constructor stub
	}

	public BootProfileImpl() {
		// TODO Auto-generated constructor stub
	}

	private void init() {
		String host = props.getProperty(MAIN_HOST);
		if(host == null) {
			host = getDefaultNetworkName();
			props.setProperty(MAIN_HOST, host);
		}
	}

	public boolean compareToProperty(String key, String option) throws Exception {
		final String METHOD_NAME = "boolean compareToProperty(String key, String option)";
		
		String value = props.getProperty(key);
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, option+" :"+ key + "/" + value);
		
		if(value ==null){	
				throw new NFlightProfileException(key+": property not found").addContextValue("key", key).addContextValue("value", value);
		}else {
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
