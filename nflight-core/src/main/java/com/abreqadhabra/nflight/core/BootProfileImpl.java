package com.abreqadhabra.nflight.core;

import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.core.exception.NFlightProfileException;

public class BootProfileImpl implements BootProfile {
	private static final Class<BootProfileImpl> THIS_CLAZZ = BootProfileImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static final String DEFAULT_BOOT_PROPERTIES_FILE_NAME = "/com/abreqadhabra/nflight/core/config/boot.properties";
	public static final String SERVICE_RMI_CLIENT = "rmiclient";
	public static final String SERVICE_RMI_SERVER = "rmiserver";
	public static final String SERVICE_SOCKET_CLIENT = "socketclient";
	public static final String SERVICE_SOCKET_SERVER = "socketserver";
	public static final String SERVICE_DATA_SERVER = "dataserver";
	public static final String ARGUMENT_SEPARATOR = ":";
	public static final String SERVICE_COMMAND_STARTUP = "startup";
	public static final String SERVICE_COMMAND_STATUS = "status";
	public static String SERVICE_COMMAND_SHUTDOWN = "shutdown";
	public static final String  SERVICE_RMI_REGISTRY = "NFlight";
	
	Properties bootProps = null;

	public BootProfileImpl() throws Exception {
		this(DEFAULT_BOOT_PROPERTIES_FILE_NAME);
	}

	public BootProfileImpl(Properties props) {
		this.bootProps = props;
		init();
	}

	public BootProfileImpl(String fileName) throws Exception {
		this.bootProps = PropertyFile.readPropertyFile(fileName);
		init();
	}

	private void init() {
		final String METHOD_NAME = "init()";

		String service = bootProps.getProperty(OPTION_SERVICE_KEY);
		// Cursor on the given string: marks the parser
		// position
		int cursor = 0;
		int separatorPos = service.indexOf(ARGUMENT_SEPARATOR, cursor);
		String serviceName = service.substring(cursor, separatorPos);
		String serviceCommand = service.substring(separatorPos + 1,
				service.length());
		bootProps.setProperty(OPTION_SERVICE_NAME_KEY, serviceName);
		bootProps.setProperty(OPTION_SERVICE_COMMAND_KEY, serviceCommand);
		bootProps.remove(OPTION_SERVICE_KEY);

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, "args : "
				+ bootProps.toString());

	}


	public boolean compareToProperty(String sourceKey, String targetValue)
			throws Exception {
		final String METHOD_NAME = "boolean compareToProperty(String sourceKey, String targetValue)";

		String sourceValue = bootProps.getProperty(sourceKey);
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, sourceKey
				+ ":" + sourceValue + "/" + targetValue);
		if (sourceValue == null) {
			throw new NFlightProfileException(sourceKey
					+ ": property not found")
					.addContextValue("sourceKey", sourceKey)
					.addContextValue("sourceValue", sourceValue)
					.addContextValue("targetValue", targetValue);
		} else {
			if (targetValue.equalsIgnoreCase(sourceValue)) {
				return true;
			} else {
				return false;
			}
		}
	}

	public String getHost() {
		return bootProps.getProperty(OPTION_HOST_KEY);
	}

	public int getPort() {
		return new Integer(bootProps.getProperty(OPTION_PORT_KEY)).intValue();
	}

	public boolean isGui() {
		return new Boolean(bootProps.getProperty(OPTION_GUI_KEY)).booleanValue();
	}


}
