package com.abreqadhabra.nflight.app.core;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.app.core.exception.NFBootException;
import com.abreqadhabra.nflight.common.exception.WrapperException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;

public class Boot {
	private static Class<Boot> THIS_CLAZZ = Boot.class;
	private static Logger LOGGER = LoggingHelper
			.getLogger(THIS_CLAZZ);

	private static void execute(String className,
			Class<?>[] parameterTypes, Object[] initArgs)
			throws Exception {
		ClassLoader classLoader = THIS_CLAZZ.getClassLoader();
		try {
			Class<?> clazz = classLoader.loadClass(className);
			Constructor<?> constructor = null;
			if (parameterTypes.length != 0) {
				constructor = clazz.getDeclaredConstructor(parameterTypes);
			} else {
				constructor = clazz.getDeclaredConstructor();
			}
			if (initArgs.length != 0) {
				constructor.newInstance(initArgs);
			} else {
				constructor.newInstance();
			}
		} catch (ClassNotFoundException | NoSuchMethodException
				| SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new NFBootException("Class loading error. ", e);
		}
	}

	/**
	 * <p>
	 * [機　能] データサーバを終了する。
	 * </p>
	 * <p>
	 * [説　明] データサーバを終了する。
	 * </p>
	 * <p>
	 * [備　考] なし
	 * </p>
	 * 
	 * @throws RemoteException
	 */

	public static void exit() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// 3초간 대기후 어플리케이션을 종료합니다.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException ie) {
					// 이 예외는 발생하지 않습니다.
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(),
							METHOD_NAME, "Thread was interrupted\n"
									+ WrapperException.getStackTrace(ie));
				}
				System.gc();
				System.runFinalization();
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(),
						METHOD_NAME, "system exit");
				System.exit(0);
			}
		}).start();
	}

	/**
	 * Fires up the <b><em>NFlight</em></b> system. This method initializes the
	 * Profile Manager and then starts the bootstrap process for the <B>
	 * <em>NFlight</em></b> acceptor platform.
	 */
	public static void main(String[] args) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Properties props = null;

		try {
			if (args.length > 0) {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(),
						METHOD_NAME, "args : " + Arrays.toString(args));
				if (args[0].startsWith(Profile.BOOT_OPTION_PREFIX)) {
					// Settings specified as command line arguments
					props = ProfileImpl.parseCMDLineArgs(args);
					props = ProfileImpl.parseServiceSpecifiers(props);
				} else {
					// Settings specified in a property file
					props = PropertyFile.readPropertyFilePath(
							THIS_CLAZZ.getName(), args[0]);
				}
			} else {
				// Settings specified in the default property file
				props = PropertyFile
						.readPropertyFilePath(THIS_CLAZZ.getName(),
								Profile.FILE_BOOT_PROPERTIES);
			}

			ProfileImpl p = new ProfileImpl(props);

			p.setSecurityManager();

			String className = p.getServiceMainClass();
			ArrayList<Class<?>> parameterTypeList = new ArrayList<Class<?>>();
			ArrayList<Object> initArgList = new ArrayList<Object>();

			if (Profile.BOOT_OPTION_SERVICE_MAINCLASS.contains(className)) {
				Profile.BOOT_OPTION_SERVICE_MAINCLASS mainClass = Profile.BOOT_OPTION_SERVICE_MAINCLASS
						.getValue(className);
				switch (mainClass) {
				case com_abreqadhabra_nflight_app_server_ServerTest:
					// parameterTypeList.add(ProfileImpl.class);
					// initArgList.add(p);
					break;
				default:
					break;
				}

			} else {
				throw new NFBootException(
						"No value specified for Service Main Class");
			}

			execute(className, parameterTypeList.toArray(new Class[] {}),
					initArgList.toArray());

			// properties = profile.getArgProperties(); if
			// (properties.getBooleanProperty(BootProfileImpl.DUMP_KEY, false))
			// { listProperties(System.out); }
			//
			// if (properties.getBooleanProperty(BootProfileImpl.VERSION_KEY,
			// false)) { System.out.println(Runtime.getCopyrightNotice());
			// return; }
			//
			// if (properties.getBooleanProperty(BootProfileImpl.HELP_KEY,
			// false)) { usage(System.out); return; }
			//
			// if (properties.getProperty(Profile.MAIN_HOST) == null) { try {
			// properties.setProperty(Profile.MAIN_HOST, InetAddress
			// .getLocalHost().getHostName()); } catch (UnknownHostException
			// uhe) { System.out
			// .print("Unknown host exception in getLocalHost(): "); System.out
			// .println(
			// " please use '-host' and/or '-port' options to setup JADE host and port"
			// ); System.exit(1); } }
			//
			// if (properties.getBooleanProperty(BootProfileImpl.CONF_KEY,
			// false)) { new BootGUI(this); if
			// (properties.getBooleanProperty(BootProfileImpl.DUMP_KEY, false))
			// { listProperties(System.out); } }

		} catch (Exception e) {
			StackTraceElement[] current = e.getStackTrace();
			if (e instanceof WrapperException) {
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(),
						"\n" + WrapperException.getStackTrace(e));
				printUsage(System.out);
			} else {
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(),
						"\n" + WrapperException.getStackTrace(e));
				printUsage(System.out);
			}
			exit();
		}
	}

	/**
	 * Show usage information.
	 * 
	 * @param out
	 *            The print stream to output to.
	 */
	public static void printUsage(PrintStream out) {
		// out.println("Usage: java -cp <classpath> com.abreqadhabra.nflight.Boot [--options]");
		// out.println("");
		// out.println("where options are:");
		// out.println("  -host <host name>\tHost where RMI registry for the platform is located");
		// out.println("  -port <port number>\tThe port where RMI registry for the platform resides");
		// out.println("  -gui\t\t\tIf specified, a new Remote Management Agent is created.");
		// out.println("  -conf <file name>\tStarts NFlight using the configuration properties read in the specified file.");
		// out.println("  -version\t\tIf specified, current JADE version number and build date is printed.");
		// out.println("  -service <service name>\tThe symbolic platform name specified only for the main container.");
		// out.println("  -help\t\t\tPrints out usage informations.");

		StringBuffer sb = new StringBuffer();
		sb.append("    Usage: java -jar nflight-<service-name>.jar [optional parameters]");
		sb.append("");
		sb.append("    		where options are:");
		sb.append("    		  --conf <file name>	Starts NFlight Service using the configuration properties read in the specified file.");
		sb.append("    		  --version		If specified, current NFlight Application version number and build date is printed.");
		sb.append("    		  ");
		sb.append("    		  --service <service-specifier>");
		sb.append("    			where service-specifier = <service-name>:<service-type>:<service-type>:<service-command>[(semicolon-separated <property-name>=<property-value>)]");
		sb.append("    				where properties are:");
		sb.append("    					-local-host	 <host(or host socketAddress) where to bind the local acceptor socket on>");
		sb.append("    					-local-port	 <port where to bind the local acceptor socket on>");
		sb.append("    					-remote-host	 <host(or host socketAddress) where to bind the remote acceptor socket on>");
		sb.append("    					-remote-port	 <port where to bind the remote acceptor socket on>");
		sb.append("    					-gui		 If specified, GUI created.");
		sb.append("    					");
		sb.append("   ");
		sb.append("    -service-mode	tcp/udp or unicast/activatable");
		sb.append("");
		sb.append("Examples:");
		sb.append("  Connect to RMI Server, starting an service named 'rmiclient'");
		sb.append("  \tjava com.abreqadhabra.nflight.Boot --service rmiclient --host 192.168.0.1 -- port 9999");
		sb.append("");
		sb.append("  Connect to Socket Server, starting an service named 'socketclient'");
		sb.append("  \tjava com.abreqadhabra.nflight.Boot --service socketclient --host 192.168.0.1 -- port 9999");
		sb.append("");
		sb.append("  Visit at the https://github.com/abreqadhabra for more details.");
		sb.append("");

		out.println(sb.toString());
		System.exit(0);
	}
}
