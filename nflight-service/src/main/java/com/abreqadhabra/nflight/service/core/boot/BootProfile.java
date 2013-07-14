package com.abreqadhabra.nflight.service.core.boot;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.common.util.PropertyLoader;
import com.abreqadhabra.nflight.service.core.boot.exception.NFlightBootException;

public class BootProfile extends Profile {

	private static final Class<BootProfile> THIS_CLAZZ = BootProfile.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private static final String BASE_LOCATION = THIS_CLAZZ
			.getProtectionDomain().getCodeSource().getLocation().getFile();

	private String serviceName;
	private String serviceMainClass;
	private String serviceCommand;
	private String bootCommand;

	public BootProfile(Properties props) {

		PropertyLoader.setSystemProperties(props);

		this.setServiceMainClass(System
				.getProperty(Profile.PROPERTIES_BOOT.NFLIGHT_BOOT_OPTION_SERVICE_MAINCLASS
						.toString()));

		this.setServiceCommand(System
				.getProperty(Profile.PROPERTIES_BOOT.NFLIGHT_BOOT_OPTION_SERVICE_COMMAND
						.toString()));

		// this.setBootCommand(System
		// .getProperty(Profile.BOOTCOMMAND_PROPERTIES.NFLIGHT_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_START_WINDOWS
		// .toString()));

	}

	public static Properties parseCMDLineArgs(String[] args) throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Properties argsProps = new Properties();
		try {
			// an iterator for the command line tokens
			Iterator<String> options = Arrays.asList(args).iterator();

			// process each command line token
			while (options.hasNext()) {
				// get the next command line token
				String key = (String) options.next();
				String value = "true";
				// handle long option --foo or --foo bar
				if (key.startsWith("--")) {
					key = stripLeadingHyphens(key);
					if (key.equalsIgnoreCase(Profile.BOOT_OPTION.gui.toString())) {
						argsProps.setProperty(
								Profile.BOOT_OPTION.gui.toString(), value);
					} else {
						argsProps.setProperty(
								Profile.BOOT_OPTION.gui.toString(), "false");
					}

					if (key.equalsIgnoreCase(Profile.BOOT_OPTION.service
							.toString())) {
						value = (String) options.next();
						if (checkNotLeadingHyphens(key, value)) {
							argsProps
									.setProperty(
											Profile.PROPERTIES_BOOT.NFLIGHT_BOOT_OPTION_SERVICE
													.toString(), value);
						}
					} else if (key.equalsIgnoreCase(Profile.BOOT_OPTION.host
							.toString())) {
						value = (String) options.next();
						if (checkNotLeadingHyphens(key, value)) {
							argsProps
									.setProperty(
											Profile.PROPERTIES_BOOT.NFLIGHT_BOOT_OPTION_SERVICE_HOST
													.toString(), value);
						}
					} else if (key.equalsIgnoreCase(Profile.BOOT_OPTION.port
							.toString())) {
						value = (String) options.next();
						if (checkNotLeadingHyphens(key, value)) {
							argsProps
									.setProperty(
											Profile.PROPERTIES_BOOT.NFLIGHT_BOOT_OPTION_SERVICE_PORT
													.toString(), value);
						}
					} else if (key.equalsIgnoreCase(Profile.BOOT_OPTION.conf
							.toString())) {

						try {
							value = (String) options.next();
							if (checkNotLeadingHyphens(key, value)) {
								argsProps.clear();
								argsProps = PropertyFile
										.readPropertyFile(value);
								LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(),
										METHOD_NAME,
										"Command line arguments will be ignored. loading properties from file: "
												+ value);
								return argsProps;
							}
						} catch (NoSuchElementException nsee) {
							throw new IllegalArgumentException(
									"No value specified for property --" + key);
						}
					}
					// Default handling for all other properties
					else {
						throw new IllegalArgumentException(
								"No value specified for property --" + key);
					}
				} // Consistency check
				else {
					throw new IllegalArgumentException(
							"No value specified for property --" + key);
				}
			}
		} catch (Exception e) {
			throw new NFlightBootException(
					"Command line arguments format error. ", e);
		}

		return argsProps;
	}

	/**
	 * Remove the hyphens from the begining of <code>str</code> and return the
	 * new String.
	 * 
	 * @param str
	 *            The string from which the hyphens should be removed.
	 * 
	 * @return the new String.
	 */
	private static String stripLeadingHyphens(String str) {
		if (str == null) {
			return null;
		}
		if (str.startsWith("--")) {
			return str.substring(2, str.length());
		} else if (str.startsWith("-")) {
			return str.substring(1, str.length());
		}

		return str;
	}

	private static boolean checkNotLeadingHyphens(String key, String value) {
		if (value.startsWith("--")) {
			throw new IllegalArgumentException("No " + key
					+ "name specified after \"--" + key + "\" option");
		} else {
			return true;
		}
	}

	public static Properties parseServiceSpecifiers(Properties props) {

		String service = props.getProperty(Profile.FILE_BOOT_PROPERTIES);

		int index1 = service.indexOf(':');
		int index2 = service.indexOf(';');

		// Cursor on the given string: marks the parser position
		int cursor = 0;

		String serviceName = service.substring(cursor, index1);
		String serviceMainClass = service.substring(index1 + 1, index2);
		String serviceCommand = service.substring(index2 + 1, service.length());

		props.setProperty(
				Profile.PROPERTIES_BOOT.NFLIGHT_BOOT_OPTION_SERVICE_MAINCLASS
						.toString(), serviceMainClass);
		props.setProperty(
				Profile.PROPERTIES_BOOT.NFLIGHT_BOOT_OPTION_SERVICE_COMMAND
						.toString(), serviceCommand);
		props.remove(Profile.PROPERTIES_BOOT.NFLIGHT_BOOT_OPTION_SERVICE);

		return props;
	}

	public static void setSecurityManager() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		System.setProperty(
				Profile.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString(),
				BASE_LOCATION + Profile.FILE_BOOT_POLICY.toString());
		LOGGER.logp(
				Level.CONFIG,
				THIS_CLAZZ.getName(),
				METHOD_NAME,
				Profile.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY
						+ "="
						+ System.getProperty(Profile.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY
								.toString()));
		if (System.getSecurityManager() == null) {
			// System.setSecurityManager(new RMISecurityManager());
			System.setSecurityManager(new SecurityManager());
		}
	}

	public boolean getBooleanProperty(
			PROPERTIES_BOOT nflightBootOptionServiceName) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceMainClass() {
		return serviceMainClass;
	}

	public void setServiceMainClass(String serviceMainClass) {
		this.serviceMainClass = serviceMainClass;
	}

	public String getServiceCommand() {
		return serviceCommand;
	}

	public void setServiceCommand(String serviceCommand) {
		this.serviceCommand = serviceCommand;
	}

	public String getBootCommand() {
		return bootCommand;
	}

	public void setBootCommand(String bootCommand) {
		this.bootCommand = bootCommand;
	}

}
