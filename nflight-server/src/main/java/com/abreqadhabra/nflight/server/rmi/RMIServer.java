package com.abreqadhabra.nflight.server.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.Constants;
import com.abreqadhabra.nflight.common.exception.NFlightUnexpectedException;
import com.abreqadhabra.nflight.common.exception.WrapperException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.server.NFlightServer;
import com.abreqadhabra.nflight.server.rmi.exception.NFlightRMIServerException;

public class RMIServer extends UnicastRemoteObject implements NFlightServer {

	private static final long serialVersionUID = 1L;

	private static final Class<RMIServer> THIS_CLAZZ = RMIServer.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private String host;
	private int port;
	private String registryName;

	public RMIServer() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		this.host = System.getProperty(Constants.Boot.KEY_BOOT_OPTION_HOST);
		this.port = Integer.parseInt(System
				.getProperty(Constants.Boot.KEY_BOOT_OPTION_PORT));

		String registryID = "rmi://" + this.host + ":" + this.port;

		this.registryName = registryID + "/"
				+ Constants.RMIServer.STR_SERVICE_REGISTRY;

		this.excute();
		
	}

	private void excute() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		String serviceCommand = System
				.getProperty(Constants.Boot.KEY_BOOT_OPTION_SERVICE_COMMAND);

		if (serviceCommand.equals(Constants.Boot.STR_SERVICE_COMMAND_STARTUP)) {
			this.startup();
		} else if (serviceCommand
				.equals(Constants.Boot.STR_SERVICE_COMMAND_SHUTDOWN)) {
			if (this.checkStatus()) {
				this.shutdown();
			} else {
				LOGGER.logp(Level.WARNING, THIS_CLAZZ.getName(), METHOD_NAME,
						"NFlight RMI Server has already been shutdown.");
			}
		} else if (serviceCommand
				.equals(Constants.Boot.STR_SERVICE_COMMAND_STATUS)) {
			boolean status = checkStatus();
			if (status == true) {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"RMI 서버가 기동 중에 있습니다.");
			} else if (status == false) {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"RMI 서버가 정지 중에 있습니다.");
			}
		} else {
			throw new NFlightRMIServerException("서버 기동이 실패하였습니다.").addContextValue("serviceCommand", serviceCommand);
		}
	}

	@Override
	public void startup() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();
		


		try {

			if (checkStatus()) {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"RMI 서버가 이미 시작 중입니다.");
				System.exit(1);
			} else {
				// Create an RMI registry on the local host and DAEMON port
				// (if one is already running just get it)
				Registry theRegistry = getRmiRegistry(this.host, this.port);
				// rebind to the registry
				Naming.rebind(registryName, this);
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"RMI 서버를 시작하였습니다.");
			}
		} catch (Exception e) {
			System.out.println("ERROR starting RMI Server");
			e.printStackTrace();
			System.exit(1);
		}


	}

	@Override
	public void shutdown() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		NFlightServer server = getRemoteServerObject(this.registryName,
				this.port);

		if (server == null) {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"데이터서버는 기동되어 있지 않습니다.");
			System.exit(1);
		}
		try {
			//UnicastRemoteObject.unexportObject
			server.shutdown();
		} catch (RemoteException e) {
			StackTraceElement[] current = e.getStackTrace();
			LOGGER.logp(Level.SEVERE, current[0].getClassName(),
					current[0].getMethodName(),
					"데이터서버를 종료할 수 없습니다." + e.getMessage());
		}

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"RMI 서버를 정지하였습니다.");
	}

	@Override
	public boolean checkStatus() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();
		// 데이터서버의 RMI 레지스트리의 포트 번호를 가져옵니다. - java -D<name>=<value> 시스템 속성

		NFlightServer ns = getRemoteServerObject(this.host, this.port);
		if (ns == null) {
			return false;
		}
		boolean isRunning = false;
		try {
			isRunning = ns.checkHealth();
		} catch (RemoteException e) {
			throw new NFlightRMIServerException("데이터서버의 상태를 확인할 수 없습니다.", e)
					.addContextValue("isRunning", isRunning);
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		}

		return isRunning;
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

	private NFlightServer getRemoteServerObject(String host, int port)
			throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		NFlightServer ns = null;
		NFlightRMIServerException nse = null;

		LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
				"Connecting NFlight RMI Server..." + registryName);
		;

		try {
			ns = (NFlightServer) Naming.lookup(registryName);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			StackTraceElement[] current = e.getStackTrace();
			LOGGER.logp(
					Level.FINER,
					current[0].getClassName(),
					current[0].getMethodName(),
					"Exception occured during connecting NFlightRMIServer."
							+ e.getMessage());
			nse = new NFlightRMIServerException(
					"Exception occured during connecting RServer.", e);
		} catch (Exception e) {
			throw new NFlightUnexpectedException(e);
		} finally {
			if (nse != null) {
				StackTraceElement[] current = nse.getStackTrace();
				LOGGER.logp(Level.FINER, current[0].getClassName(),
						current[0].getMethodName(),
						"Exception occured during connecting NFlightRMIServer."
								+ WrapperException.getStackTrace(nse));
				ns = null;
			}
		}

		return ns;
	}

	/**
	 * Get the RMIRegistry. If a registry is already active on this host and the
	 * given portNumber, then that registry is returned, otherwise a new
	 * registry is created and returned.
	 * 
	 * @param portNumber
	 *            is the port on which the registry accepts requests
	 * @param host
	 *            host for the remote registry, if null the local host is used
	 **/
	private Registry getRmiRegistry(String host, int portNumber)
			throws RemoteException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		Registry rmiRegistry = null;
		try {
			// See if a registry already exists and
			// make sure we can really talk to it.
			rmiRegistry = LocateRegistry.getRegistry(host, portNumber);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Local RMI Registry on port " + portNumber
							+ " already exists. Use it");

			String[] names = rmiRegistry.list();
			StringBuffer sb = new StringBuffer("Local RMI Registry bindings:\n");
			for (int i = 0; i < names.length; ++i) {
				sb.append("Name " + names[i] + " bound to "
						+ rmiRegistry.lookup(names[i]) + "\n");
				LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getName(), METHOD_NAME,
						sb.toString());
			}
		} catch (RemoteException e1) {
			rmiRegistry = LocateRegistry.createRegistry(portNumber);
			LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getName(), METHOD_NAME,
					"Local RMI Registry successfully created on port "
							+ portNumber);

		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rmiRegistry;

	} // END getRmiRegistry()

}
