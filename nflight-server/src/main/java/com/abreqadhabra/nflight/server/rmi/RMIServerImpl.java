package com.abreqadhabra.nflight.server.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.NFlightUnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.server.core.NFlightCommands;
import com.abreqadhabra.nflight.server.core.NFlightServer;
import com.abreqadhabra.nflight.server.core.exception.NFlightServerException;
import com.abreqadhabra.nflight.server.ns.rmi.ServerController;

public class RMIServerImpl extends NFlightCommands implements NFlightServer {

	public static final String NFLIGHT_SERVER_PROPERTY_RMISERVER_KEY = NFLIGHT_SERVER_PROPERTY_KEY
			+ ".rmiserver";
	public static final String NFLIGHT_SERVER_PROPERTY_RMISERVER_URL_KEY = NFLIGHT_SERVER_PROPERTY_RMISERVER_KEY
			+ ".url";

	private static final Class<RMIServerImpl> THIS_CLAZZ = RMIServerImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public RMIServerImpl() {
		super();
	}

	/**
	 * Main to start the <code> ServerApplication </code> and creates the
	 * Instance of <code> ServerController </code>. Create a RMI server with the
	 * specified port(1024-65535).
	 * 
	 * @param args
	 *            The port of naming service like rmi registry.
	 */
	public static void main(String[] args) {
		final String METHOD_NAME = "void main(String[] args)";

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, "args: "
				+ Arrays.toString(args));

		NFlightServer server = new RMIServerImpl();
		String mode = null;
		try {

			if (args.length > 0) {
				mode = args[0];
			} else {
				mode = "";
			}

			if (NFLIGHT_SYSTEM_STARTUP.equals(mode)) {
				server.startup();
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"RMI 서버를 시작하였습니다.");
			} else if (NFLIGHT_SYSTEM_SHUTDOWN.equals(mode)) {
				if (checkStatus()) {
					server.shutdown();
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
							"RMI 서버를 정지하였습니다.");
				} else {
					LOGGER.logp(Level.WARNING, THIS_CLAZZ.getName(),
							METHOD_NAME,
							"NFlight RMI Server has already been shutdown.");
				}
			} else if (NFLIGHT_SYSTEM_STATUS.equals(mode)) {
				boolean status = checkStatus();
				if (status == true) {
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
							"RMI 서버가 기동 중에 있습니다.");
				} else if (status == false) {
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
							"RMI 서버가 정지 중에 있습니다.");
				}
			} else {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"mode: " + mode);
				printUsage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * [機　能] データサーバの生死を確認する。
	 * </p>
	 * <p>
	 * [説　明] データサーバの生死を確認する。
	 * </p>
	 * <p>
	 * [備　考] なし
	 * </p>
	 * 
	 * @return boolean データサーバが生きているならば、true
	 * @throws RemoteException
	 */
	@Override
	public boolean checkHealth() throws RemoteException {
		return true;
	}

	@Override
	public void startup() throws RemoteException {
		System.out.println("RMI 서버 시작?");
		new ServerController(9999);
	}

	@Override
	public void shutdown() throws Exception {
		final String METHOD_NAME = "void shutdown()";

		// 데이터서버의 RMI 레지스트리의 포트 번호를 가져옵니다. - java -D<name>=<value> 시스템 속성
		String url = System.getProperty(
				NFLIGHT_SERVER_PROPERTY_RMISERVER_URL_KEY).trim();
		String port = url.substring(url.indexOf(':') + 1, url.lastIndexOf('/'));

		NFlightServer server = getRemoteServerObject(NFLIGHT_SERVER_LOCALHOST,
				port);

		if (server == null) {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"데이터서버는 기동되어 있지 않습니다.");
			System.exit(1);
		}
		try {
			server.shutdown();
		} catch (RemoteException e) {
			StackTraceElement[] current = e.getStackTrace();
			LOGGER.logp(Level.SEVERE, current[0].getClassName(),
					current[0].getMethodName(),
					"데이터서버를 종료할 수 없습니다." + e.getMessage());
		}
	}

	@Override
	public void exit() throws Exception {
		final String METHOD_NAME = "void exit()";

		String command = getShutdownCommand("rmiserver.command");
		if (checkStatus()) {
			LOGGER.logp(Level.WARNING, THIS_CLAZZ.getName(), METHOD_NAME,
					"NFlight RMI Server is already running.");
			super.exec(command);
		} else {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"데이터서버는 기동되어 있지 않습니다.:" + command);
		}
	}

	@Override
	public void exec() throws Exception {
		final String METHOD_NAME = "void boot()";

		String command = getStartupCommand("rmiserver.command");
		if (checkStatus()) {
			LOGGER.logp(Level.WARNING, THIS_CLAZZ.getName(), METHOD_NAME,
					"NFlight RMI Server is already running.");
		} else {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Startup background process.Command :" + command);
			super.exec(command);
		}
	}

	public static boolean checkStatus() throws Exception {
		// 데이터서버의 RMI 레지스트리의 포트 번호를 가져옵니다. - java -D<name>=<value> 시스템 속성
		String url = System.getProperty(
				NFLIGHT_SERVER_PROPERTY_RMISERVER_URL_KEY).trim();
		String port = url.substring(url.indexOf(':') + 1, url.lastIndexOf('/'));
		NFlightServer ns = getRemoteServerObject(NFLIGHT_SERVER_LOCALHOST, port);
		if (ns == null) {
			return false;
		}
		boolean isRunning = false;
		try {
			isRunning = ns.checkHealth();
		} catch (RemoteException e) {
			throw new NFlightServerException("데이터서버의 상태를 확인할 수 없습니다.", e)
					.addContextValue("isRunning", isRunning);
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		}

		return isRunning;
	}

	/**
	 * <p>
	 * [개 요] 데이터 서버에 접속합니다.
	 * </p>
	 * <p>
	 * [상 세] 데이터 서버에 접속합니다.
	 * </p>
	 * <p>
	 * [비 고]
	 * </p>
	 * 
	 * @param host
	 *            데이터서버의 호스트명
	 * @param port
	 *            데이터서버의 포트번호
	 * @return INFlightRMIServer 데이터서버에 접속 불가의 경우에는 null
	 * @throws Exception
	 * @since STEP1
	 */

	public static NFlightServer getRemoteServerObject(String host, String port)
			throws Exception {
		final String METHOD_NAME = "NFlightServer getRemoteServerObject(String host, String port)";

		NFlightServer ns = null;
		NFlightServerException nse = null;

		LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
				"Connecting NFlight RMI Server...");

		String url = "//" + host + ":" + port + "/nflight";

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, "url: "
				+ url);
		try {
			ns = (NFlightServer) Naming.lookup(url);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Connected " + url);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// StackTraceElement[] current = e.getStackTrace();
			// LOGGER.logp(Level.FINER, current[0].getClassName(),
			// current[0].getMethodName(),
			// "Exception occured during connecting NFlightRMIServer."
			// + e.getMessage());
			// nse = new
			// NFlightServerException("Exception occured during connecting NFlightRMIServer.",e);
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		} finally {
			if (nse != null) {
				StackTraceElement[] current = nse.getStackTrace();
				LOGGER.logp(Level.FINER, current[0].getClassName(),
						current[0].getMethodName(),
						"Exception occured during connecting NFlightRMIServer."
								+ nse.getStackTrace(nse));
				ns = null;
			}
		}

		return ns;
	}

	public static void printUsage() {
		System.out.println("Usage:");
		System.out
				.println("java -cp <classpath> com.abreqadhabra.nflight.server.rmi.RMIServerImpl [options] [agents]");
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
