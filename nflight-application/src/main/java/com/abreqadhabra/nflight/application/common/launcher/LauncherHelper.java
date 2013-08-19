package com.abreqadhabra.nflight.application.common.launcher;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.exception.LauncherException;
import com.abreqadhabra.nflight.application.conf.ApplicationConfiguration;
import com.abreqadhabra.nflight.application.conf.ApplicationConfiguration.ENUM_APPLICATION_BOOT_OPTION;
import com.abreqadhabra.nflight.application.conf.ApplicationConfiguration.ENUM_APPLICATION_BOOT_PROPERTIES;
import com.abreqadhabra.nflight.application.conf.ApplicationConfiguration.ENUM_APPLICATION_SYSTEM_PROPERTIES;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;

public class LauncherHelper {
	private static Class<ServiceLauncher> THIS_CLAZZ = ServiceLauncher.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static void setSecurityManager() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINEST, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"Settings specified in the default security policy file : "
						+ ApplicationConfiguration.PATH_APPLICATION_BOOT_POLICY);

		System.setProperty(
				ENUM_APPLICATION_SYSTEM_PROPERTIES.JAVA_SECURITY_POLICY
						.toString(),
				ApplicationConfiguration.PATH_APPLICATION_BOOT_POLICY.toString());

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
	}

	protected static Properties parseCMDLineArgs(String[] cmdLineArgs)
			throws Exception {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Properties argsProps = new Properties();
		try {
			// an iterator for the command line tokens
			Iterator<String> options = Arrays.asList(cmdLineArgs).iterator();

			// process each command line token
			while (options.hasNext()) {
				// get the next command line token
				String key = options.next();
				String value = "true";
				// handle long option --foo or --foo bar
				if (key.startsWith("--")) {
					key = stripLeadingHyphens(key);
					if (key.equalsIgnoreCase(ENUM_APPLICATION_BOOT_OPTION.gui
							.toString())) {
						argsProps.setProperty(
								ENUM_APPLICATION_BOOT_OPTION.gui
										.toString(), value);
					} else {
						argsProps.setProperty(
								ENUM_APPLICATION_BOOT_OPTION.gui
										.toString(), "false");
					}

					if (key.equalsIgnoreCase(ENUM_APPLICATION_BOOT_OPTION.service
							.toString())) {
						value = options.next();
						if (checkNotLeadingHyphens(key, value)) {
							argsProps
									.setProperty(
											ApplicationConfiguration.ENUM_APPLICATION_BOOT_PROPERTIES.NFLIGHT_BOOT_OPTION_SERVICE
													.toString(), value);
						}
					} else if (key
							.equalsIgnoreCase(ENUM_APPLICATION_BOOT_OPTION.host
									.toString())) {
						value = options.next();
						if (checkNotLeadingHyphens(key, value)) {
							argsProps
									.setProperty(
											ApplicationConfiguration.ENUM_APPLICATION_BOOT_PROPERTIES.NFLIGHT_BOOT_OPTION_SERVICE_HOST
													.toString(), value);
						}
					} else if (key
							.equalsIgnoreCase(ENUM_APPLICATION_BOOT_OPTION.port
									.toString())) {
						value = options.next();
						if (checkNotLeadingHyphens(key, value)) {
							argsProps
									.setProperty(
											ENUM_APPLICATION_BOOT_PROPERTIES.NFLIGHT_BOOT_OPTION_SERVICE_PORT
													.toString(), value);
						}
					} else if (key
							.equalsIgnoreCase(ENUM_APPLICATION_BOOT_OPTION.conf
									.toString())) {

						try {
							value = options.next();
							if (checkNotLeadingHyphens(key, value)) {
								argsProps.clear();
								argsProps = PropertyFile.readPropertyFilePath(
										THIS_CLAZZ.getName(), value);
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
			throw new LauncherException(
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

}
