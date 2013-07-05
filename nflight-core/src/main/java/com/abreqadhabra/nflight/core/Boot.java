package com.abreqadhabra.nflight.core;

import java.io.PrintStream;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.WrapperException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.core.exception.NFlightProfileException;

public class Boot {
	private static final Class<Boot> THIS_CLAZZ = Boot.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	private static BootProfileImpl profile;

	/**
	 * Fires up the <b><em>NFlight</em></b> system. This method initializes the
	 * Profile Manager and then starts the bootstrap process for the <B>
	 * <em>NFlight</em></b> server platform.
	 */
	public static void main(String[] args) {
		final String METHOD_NAME = "void main(String[] args";

		try {
			if (args.length > 0) {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"args : " + Arrays.toString(args));
				if (args[0].startsWith("--")) {
					// Settings specified as command line arguments
					Properties props = parseCMDLineArgs(args);
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
							"props :" + props.toString());
					profile = new BootProfileImpl(props);
				} else {
					// Settings specified in a property file
					profile = new BootProfileImpl(args[0]);
				}
			} else {
				// Settings specified in the default property file
				profile = new BootProfileImpl();
			}

			
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(),
					METHOD_NAME,
					"getHostAddress: " + InetAddress.getLocalHost().getHostAddress());
			
			/*
		    properties = profile.getArgProperties();
	        if (properties.getBooleanProperty(BootProfileImpl.DUMP_KEY, false)) {
	            listProperties(System.out);
	        }

	        if (properties.getBooleanProperty(BootProfileImpl.VERSION_KEY, false)) {
	            System.out.println(Runtime.getCopyrightNotice());
	            return;
	        }

	        if (properties.getBooleanProperty(BootProfileImpl.HELP_KEY, false)) {
	            usage(System.out);
	            return;
	        }

	        if (properties.getProperty(Profile.MAIN_HOST) == null) {
	            try {
	                properties.setProperty(Profile.MAIN_HOST, InetAddress.getLocalHost().getHostName());
	            } catch (UnknownHostException uhe) {
	                System.out.print("Unknown host exception in getLocalHost(): ");
	                System.out.println(" please use '-host' and/or '-port' options to setup JADE host and port");
	                System.exit(1);
	            }
	        }

	        if (properties.getBooleanProperty(BootProfileImpl.CONF_KEY, false)) {
	            new BootGUI(this);
	            if (properties.getBooleanProperty(BootProfileImpl.DUMP_KEY, false)) {
	                listProperties(System.out);
	            }
	        }
	        
	        */
			
			
		} catch (Exception e) {
			StackTraceElement[] current = e.getStackTrace();
			if (e instanceof WrapperException) {
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(),
						"\n" + WrapperException.getStackTrace(e));
				printUsage(System.out);
				System.exit(-1);
			} else {
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(),
						"\n" + WrapperException.getStackTrace(e));
				printUsage(System.out);
				System.exit(-1);
			}
		}
	}

	public static Properties parseCMDLineArgs(String[] args) throws Exception {
		final String METHOD_NAME = "Properties parseCMDLineArgs(String[] args)";

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
					if (key.equalsIgnoreCase(BootProfile.OPTION_GUI_KEY)) {
						argsProps.setProperty(key, value);
					} else if (key
							.equalsIgnoreCase(BootProfile.OPTION_SERVICE_KEY)) {
						value = (String) options.next();
						if (checkNotLeadingHyphens(key, value)) {
							argsProps.setProperty(key, value);
						}
					} else if (key
							.equalsIgnoreCase(BootProfile.OPTION_HOST_KEY)) {
						value = (String) options.next();
						if (checkNotLeadingHyphens(key, value)) {
							argsProps.setProperty(key, value);
						}
					} else if (key
							.equalsIgnoreCase(BootProfile.OPTION_PORT_KEY)) {
						value = (String) options.next();
						if (checkNotLeadingHyphens(key, value)) {
							argsProps.setProperty(key, value);
						}
					} else if (key
							.equalsIgnoreCase(BootProfile.OPTION_CONF_KEY)) {
						value = (String) options.next();
						if (checkNotLeadingHyphens(key, value)) {
							argsProps.clear();
							argsProps = PropertyFile.readPropertyFile(value);
							LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(),
									METHOD_NAME,
									"Command line argument will be ignored");
							return argsProps;
						}
					}
					// Default handling for all other properties
					else {
						throw new IllegalArgumentException(
								"No value specified for property \"" + key);
					}
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
							key + "=" + value);
				} // Consistency check
				else {
					throw new IllegalArgumentException(
							"No value specified for property \"" + key);
				}
			}
		} catch (Exception e) {
			throw new NFlightProfileException(
					"Command line arguments format error. ", e);
		}

		return argsProps;
	}

	private static boolean checkNotLeadingHyphens(String key, String value) {
		if (value.startsWith("--")) {
			throw new IllegalArgumentException("No " + key
					+ "name specified after \"--" + key + "\" option");
		} else {
			return true;
		}
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

    /**
     * Show usage information.
     * @param out The print stream to output to.
     */
    public static void printUsage(PrintStream out) {
        out.println("Usage: java -cp <classpath> com.abreqadhabra.nflight.core.Boot [--options]");
        out.println("");
        out.println("where options are:");
        out.println("  -host <host name>\tHost where RMI registry for the platform is located");
        out.println("  -port <port number>\tThe port where RMI registry for the platform resides");
        out.println("  -gui\t\t\tIf specified, a new Remote Management Agent is created.");
        out.println("  -conf <file name>\tStarts NFlight using the configuration properties read in the specified file.");
        out.println("  -version\t\tIf specified, current JADE version number and build date is printed.");
        out.println("  -service <service name>\tThe symbolic platform name specified only for the main container.");
        out.println("  -help\t\t\tPrints out usage informations.");
        out.println("");
        out.println("Examples:");
        out.println("  Connect to RMI Server, starting an service named 'rmiclient'");
        out.println("  \tjava com.abreqadhabra.nflight.core.Boot --service rmiclient --host 192.168.0.1 -- port 9999");
        out.println("");
        out.println("  Connect to Socket Server, starting an service named 'socketclient'");
        out.println("  \tjava com.abreqadhabra.nflight.core.Boot --service socketclient --host 192.168.0.1 -- port 9999");
        out.println("");
        out.println("  Visit at the https://github.com/abreqadhabra for more details.");
        out.println("");
        System.exit(0);
    }
}
