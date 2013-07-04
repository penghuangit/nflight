package com.abreqadhabra.nflight.server;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.WrapperException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.server.core.CommandProcessor;
import com.abreqadhabra.nflight.server.core.Profile;
import com.abreqadhabra.nflight.server.core.ProfileImpl;

public class Bootstrap {

	private static final Class<Bootstrap> THIS_CLAZZ = Bootstrap.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static final String DEFAULT_FILENAME = "leap.properties";

	/**
	 * Fires up the <b><em>NFlight</em></b> system. This method initializes the
	 * Profile Manager and then starts the bootstrap process for the <B>
	 * <em>NFlight</em></b> server platform.
	 */
	public static void main(String[] args) {
		final String METHOD_NAME = "void main(String[] args";

		// Create the Profile
		ProfileImpl profile = new ProfileImpl();
		try {
			if (args.length > 0) {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"args : " + Arrays.toString(args));
				if (args[0].startsWith("--")) {
					// Settings specified as command line arguments
					Properties props = parseCMDLineArgs(args);
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
							"props :" + props.toString());
					profile = new ProfileImpl(props);
				} else {
					// Settings specified in a property file
					profile = new ProfileImpl(args[0]);
				}
			} else {
				// Settings specified in the default property file
				profile = new ProfileImpl(DEFAULT_FILENAME);
			}
			// Start a new NFlight runtime system
			CommandProcessor.instance().setCloseVM(true);

			
			if (profile.compareToProperty(Profile.SERVER, Profile.RMI_SERVER)) {
				CommandProcessor.instance().createRMIContainer(profile);
			} else if (profile.compareToProperty(Profile.SERVER,
					Profile.SOCKET_SERVER)) {
				// CommandProcessor.instance().createSocketContainer(profile);
			} else if (profile.compareToProperty(Profile.SERVER,
					Profile.SOCKET_SERVER)) {
				// CommandProcessor.instance().createDataServerContainer(profile);
			}
		} catch (Exception e) {
			StackTraceElement[] current = e.getStackTrace();
			if (e instanceof WrapperException) {
				WrapperException we = (WrapperException) e;
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(), "\n" + we.getStackTrace(e));
				printUsage();
				System.exit(-1);
			} else if (e instanceof IllegalArgumentException) {
				LOGGER.logp(
						Level.SEVERE,
						current[0].getClassName(),
						current[0].getMethodName(),
						"\n" + "Command line arguments format error. "
								+ e.getMessage());
				printUsage();
				System.exit(-1);
			} else {
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(), e.getMessage());
			}
		}
	}

	private static void printUsage() {
		System.err
				.println("Usage: java com.abreqadhabra.nflight.server.Boot <filename>");
	}

	private static Properties parseCMDLineArgs(String[] args) {
		final String METHOD_NAME = "Properties parseCMDLineArgs(String[] args)";

		Properties props = new Properties();

		if (args == null) {
			args = new String[0];
		}
		// an iterator for the command line tokens
		Iterator<String> iter = Arrays.asList(args).iterator();

		// process each command line token
		while (iter.hasNext()) {
			// get the next command line token
			String option = (String) iter.next();
			// handle long option --foo or --foo bar
			if (option.startsWith("--")) {
				String value = null;
				if (option.equalsIgnoreCase(Profile.CMD)) {
					value = (String) iter.next();
					if (value.startsWith("--")) {
						throw new IllegalArgumentException(
								"No cmd name specified after \"-cmd\" option");
					} else {
						props.setProperty(option, value);
					}
				} else if (option.equalsIgnoreCase(Profile.GUI)) {
					value = "true";
					props.setProperty(option, value);
				} else if (option.equalsIgnoreCase(Profile.SERVER)) {
					value = (String) iter.next();
					if (value.startsWith("--")) {
						throw new IllegalArgumentException(
								"No server name specified after \"-server\" option");
					} else {
						props.setProperty(option, value);
					}
				} else if (option.equalsIgnoreCase(Profile.HOST)) {
					value = (String) iter.next();
					if (value.startsWith("--")) {
						throw new IllegalArgumentException(
								"No host name specified after \"-host\" option");
					} else {
						props.setProperty(option, value);
					}
				} else if (option.equalsIgnoreCase(Profile.PORT)) {
					value = (String) iter.next();
					if (value.startsWith("--")) {
						throw new IllegalArgumentException(
								"No port name specified after \"-port\" option");
					} else {
						props.setProperty(option, value);
					}
				}
				// Default handling for all other properties
				else {
					throw new IllegalArgumentException(
							"No value specified for property \"" + option);
				}
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						option + "=" + value);
			} // Consistency check
			else {
				throw new IllegalArgumentException(
						"No value specified for property \"" + option);
			}
		}

		return props;
	}

}
