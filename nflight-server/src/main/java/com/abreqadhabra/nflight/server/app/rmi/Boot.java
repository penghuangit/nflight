/**
 * ***************************************************************
 * NFlight - Night Flight Application Prototype is a prototype to develop
 * Java-based systems in compliance with the JVM specifications.
 * Copyright (C) 2013 Kim, Dong-Sup (dongsup dot kim at gmail dot com)
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package com.abreqadhabra.nflight.server.app.rmi;

//#MIDP_EXCLUDE_FILE

import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

/**
 * Boots the <B><em>NFlight</em></b> system, parsing command line arguments.
 * 
 * 
 */
public class Boot {

	private static final Class<Boot> THIS_CLAZZ = Boot.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	/**
	 * Fires up the <b><em>NFlight</em></b> system. This method initializes the
	 * Profile Manager and then starts the bootstrap process for the <B>
	 * <em>NFlight</em></b> prototype system.
	 */

	public static void main(String args[]) {
		final String METHOD_NAME = "void main(String[] args)";

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, "args[]:"
				+ Arrays.toString(args));
		
		try {
			// Create the Profile
	//		BootProfile bootProfile = null;
			if (args.length > 0) {
				if (args[0].startsWith("-")) {
					// Settings specified as command line arguments
					Properties prop = parseCmdLineArgs(args);
					if (prop != null) {
				//		bootProfile = new RMIProfileImpl(prop);
					} else {
						// One of the "exit-immediately" options was specified!
						return;
					}
				} else {
					// Settings specified in a property file
				//	bootProfile = new RMIProfileImpl(args[0]);
				}
			} else {
				// Settings specified in the default property file
//				bootProfile = new RMIProfileImpl(DEFAULT_FILENAME);
			}
		} catch (IllegalArgumentException iae) {

			System.err.println("Command line arguments format error. "
					+ iae.getMessage());
			iae.printStackTrace();
			printUsage();
		}
	}

	private static Properties parseCmdLineArgs(String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void printUsage() {
		System.out.println("Usage:");
		System.out
				.println("java -cp <classpath> com.abreqadhabra.nflight.server.Boot [options] [agents]");
		System.out.println("Main options:");
		System.out.println("    -container");
		System.out.println("    -gui");
		System.out.println("    -name <platform name>");
		System.out.println("    -host <main container host>");
		System.out.println("    -port <main container port>");
		System.out
				.println("    -local-host <host where to bind the local server socket on>");
		System.out
				.println("    -local-port <port where to bind the local server socket on>");
		System.out
				.println("    -conf <property file to load configuration properties from>");
		System.out
				.println("    -services <semicolon separated list of service classes>");
		System.out
				.println("    -mtps <semicolon separated list of mtp-specifiers>");
		System.out
				.println("     where mtp-specifier = [in-address:]<mtp-class>[(comma-separated args)]");
		System.out.println("    -<property-name> <property-value>");
		System.out
				.println("Agents: [-agents] <semicolon separated list of agent-specifiers>");
		System.out
				.println("     where agent-specifier = <agent-name>:<agent-class>[(comma separated args)]");
		System.out.println();
		System.out
				.println("Look at the NFlight Administrator's Guide for more details");
	}

}
