
package com.abreqadhabra.nflight.server.app.rmi;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.server.core.CommandProcessor;

/**
   Main class to start JADE as a split-container.

 */
public class MicroBoot {

	private static final Class<CommandProcessor> THIS_CLAZZ = CommandProcessor.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	/**
       Default constructor.
	 */
	public MicroBoot() {
	}

	/**
	 * Fires up the <b><em>JADE</em></b> runtime.
	 */
	public static void main(String args[]) {
		String propsFile = null;
		try {
			Properties props = parseCmdLineArgs(args);
			propsFile = props.getProperty("conf");
			if (propsFile != null) {
	//			props.load(propsFile);
			}
	//		Level.initialize(props);
			if (props.getProperty(MicroRuntime.JVM_KEY) == null) {
				//#PJAVA_EXCLUDE_BEGIN
				props.setProperty(MicroRuntime.JVM_KEY, MicroRuntime.J2SE);
				//#PJAVA_EXCLUDE_END
				/*#PJAVA_INCLUDE_BEGIN
				props.setProperty(MicroRuntime.JVM_KEY, MicroRuntime.PJAVA);
				#PJAVA_INCLUDE_END*/
			}

			MicroRuntime.startJADE(props, new Runnable() {
				public void run() {
					// Wait a bit before killing the JVM
					try {
						Thread.sleep(1000);
					}
					catch (InterruptedException ie) {
					}
					LOGGER.log(Level.INFO,"Exiting now!");
					System.exit(0);
				} 
			});
		}
		catch (IllegalArgumentException iae) {
			LOGGER.log(Level.SEVERE,"Error reading command line configuration properties. "+iae.getMessage());
			iae.printStackTrace();
			printUsage();
			System.exit(-1);
		}
/*		catch (IOException ioe) {
			LOGGER.log(Level.SEVERE,"Error reading configuration properties from file "+propsFile+".", ioe);
			printUsage();
			System.exit(-1);
		}*/
	}

	private static Properties parseCmdLineArgs(String[] args) throws IllegalArgumentException {
		Properties props = new Properties();

		int i = 0;
		while (i < args.length) {
			if (args[i].startsWith("-")) {
				// Parse next option
				String name = args[i].substring(1);
				if (++i < args.length) {
					props.setProperty(name, args[i]);
				}
				else {
					throw new IllegalArgumentException("No value specified for property \""+name+"\"");
				}
				++i;
			}
			else {
				// Get agents at the end of command line
				if (props.getProperty(MicroRuntime.AGENTS_KEY) != null) {
					if(LOGGER.isLoggable(Level.WARNING))
						LOGGER.log(Level.WARNING,"WARNING: overriding agents specification set with the \"-agents\" option");
				}
				String agents = args[i];
				props.setProperty(MicroRuntime.AGENTS_KEY, args[i]);
				if (++i < args.length) {
					if(LOGGER.isLoggable(Level.WARNING))
						LOGGER.log(Level.WARNING,"WARNING: ignoring command line argument "+args[i]+" occurring after agents specification");
					if (agents != null && agents.indexOf('(') != -1 && !agents.endsWith(")")) {
						if(LOGGER.isLoggable(Level.WARNING))
							LOGGER.log(Level.WARNING,"Note that agent arguments specifications must not contain spaces");
					}
					if (args[i].indexOf(':') != -1) {
						if(LOGGER.isLoggable(Level.WARNING))
							LOGGER.log(Level.WARNING,"Note that agent specifications must be separated by a semicolon character \";\" without spaces");
					}
				}
				break;
			}
		}

		return props;
	}

	private static void printUsage() {
		LOGGER.log(Level.ALL,"Usage:");
		LOGGER.log(Level.ALL,"java -cp <classpath> jade.MicroBoot [options] [agents]");
		LOGGER.log(Level.ALL,"Options:");
		LOGGER.log(Level.ALL,"    -conf <file-name>. Read configuration properties from the specified file name");
		LOGGER.log(Level.ALL,"    -host <host-name>. The name/address of the host where the BackEnd has to be created");
		LOGGER.log(Level.ALL,"    -port <port-number>. The port of the J2SE container active on \"host\"");
		LOGGER.log(Level.ALL,"    -<key> <value>");
		LOGGER.log(Level.ALL,"Agents: [-agents] <semicolon-separated agent-specifiers>");
		LOGGER.log(Level.ALL,"     where agent-specifier = <agent-name>:<agent-class>[(comma separated args)]\n"); 
	}

}

