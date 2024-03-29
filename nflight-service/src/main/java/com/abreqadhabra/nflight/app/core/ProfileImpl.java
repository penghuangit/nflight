package com.abreqadhabra.nflight.app.core;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.app.core.exception.NFBootException;
import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.IOStream;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.common.util.PropertyLoader;

public class ProfileImpl extends Profile {

	private static Class<ProfileImpl> THIS_CLAZZ = ProfileImpl.class;
	private static Logger LOGGER = LoggingHelper
			.getLogger(THIS_CLAZZ);

	private static boolean checkNotLeadingHyphens(String key,
			String value) {
		if (value.startsWith("--")) {
			throw new IllegalArgumentException("No " + key
					+ "name specified after \"--" + key + "\" option");
		} else {
			return true;
		}
	}

	public static Properties parseCMDLineArgs(String[] args)
			throws Exception {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Properties argsProps = new Properties();
		try {
			// an iterator for the command line tokens
			Iterator<String> options = Arrays.asList(args).iterator();

			// process each command line token
			while (options.hasNext()) {
				// get the next command line token
				String key = options.next();
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
						value = options.next();
						if (checkNotLeadingHyphens(key, value)) {
							argsProps
									.setProperty(
											Profile.PROPERTIES_BOOT.NFLIGHT_BOOT_OPTION_SERVICE
													.toString(), value);
						}
					} else if (key.equalsIgnoreCase(Profile.BOOT_OPTION.host
							.toString())) {
						value = options.next();
						if (checkNotLeadingHyphens(key, value)) {
							argsProps
									.setProperty(
											Profile.PROPERTIES_BOOT.NFLIGHT_BOOT_OPTION_SERVICE_HOST
													.toString(), value);
						}
					} else if (key.equalsIgnoreCase(Profile.BOOT_OPTION.port
							.toString())) {
						value = options.next();
						if (checkNotLeadingHyphens(key, value)) {
							argsProps
									.setProperty(
											Profile.PROPERTIES_BOOT.NFLIGHT_BOOT_OPTION_SERVICE_PORT
													.toString(), value);
						}
					} else if (key.equalsIgnoreCase(Profile.BOOT_OPTION.conf
							.toString())) {

						try {
							value = options.next();
							if (checkNotLeadingHyphens(key, value)) {
								argsProps.clear();
								argsProps = PropertyFile
										.readPropertyFilePath(
												THIS_CLAZZ
														.getName(), value);
								LOGGER.logp(Level.FINER,
										THIS_CLAZZ.getName(),
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
			throw new NFBootException("Command line arguments format error. ",
					e);
		}

		return argsProps;
	}

	public static Properties parseServiceSpecifiers(Properties props) {

		String service = props.getProperty(Profile.FILE_BOOT_PROPERTIES);

		int index1 = service.indexOf(':');
		int index2 = service.indexOf(';');

		// Cursor on the given string: marks the parser position
		int cursor = 0;

		service.substring(cursor, index1);
		String serviceMainClass = service.substring(index1 + 1, index2);
		String serviceCommand = service.substring(index2 + 1,
				service.length());

		props.setProperty(
				Profile.PROPERTIES_BOOT.NFLIGHT_BOOT_OPTION_SERVICE_MAINCLASS
						.toString(), serviceMainClass);
		props.setProperty(
				Profile.PROPERTIES_BOOT.NFLIGHT_BOOT_OPTION_SERVICE_COMMAND
						.toString(), serviceCommand);
		props.remove(Profile.PROPERTIES_BOOT.NFLIGHT_BOOT_OPTION_SERVICE);

		return props;
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

	private String serviceMainClass;

	private String serviceCommand;

	private String codeBase;

	private int servicePort;

	public ProfileImpl(Properties props) {

		PropertyLoader.setSystemProperties(props);

		this.setServiceMainClass(System
				.getProperty(Profile.PROPERTIES_BOOT.NFLIGHT_BOOT_OPTION_SERVICE_MAINCLASS
						.toString()));

		this.setServiceCommand(System
				.getProperty(Profile.PROPERTIES_BOOT.NFLIGHT_BOOT_OPTION_SERVICE_COMMAND
						.toString()));

		this.setCodeBase(IOStream.getCodebase(this.serviceMainClass));

		this.setServicePort(Integer.parseInt(System
				.getProperty(Profile.PROPERTIES_BOOT.NFLIGHT_BOOT_OPTION_SERVICE_PORT
						.toString())));

		// this.setBootCommand(System
		// .getProperty(Profile.BOOTCOMMAND_PROPERTIES.NFLIGHT_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_START_WINDOWS
		// .toString()));

	}

	public boolean getBooleanProperty(
			PROPERTIES_BOOT nflightBootOptionServiceName) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getCodeBase() {
		return this.codeBase;
	}

	public String getServiceCommand() {
		return this.serviceCommand;
	}

	public String getServiceMainClass() {
		return this.serviceMainClass;
	}

	public int getServicePort() {
		return this.servicePort;
	}

	public void setCodeBase(String codeBase) {
		this.codeBase = codeBase;
	}

	public void setSecurityManager() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		System.setProperty(
				Env.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString(),
				this.codeBase + Profile.FILE_BOOT_POLICY.toString());
		LOGGER
				.logp(Level.CONFIG,
						THIS_CLAZZ.getName(),
						METHOD_NAME,
						Env.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY
								+ "="
								+ System.getProperty(Env.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY
										.toString()));
		if (System.getSecurityManager() == null) {
			// System.setSecurityManager(new RMISecurityManager());
			System.setSecurityManager(new SecurityManager());
		}
	}

	public void setServiceCommand(String serviceCommand) {
		this.serviceCommand = serviceCommand;
	}

	public void setServiceMainClass(String serviceMainClass) {
		this.serviceMainClass = serviceMainClass;
	}

	public void setServicePort(int servicePort) {
		this.servicePort = servicePort;
	}

}
