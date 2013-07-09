package com.abreqadhabra.nflight.server.rmi;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.Constants;
import com.abreqadhabra.nflight.common.exception.NFlightUnexpectedException;
import com.abreqadhabra.nflight.common.exception.WrapperException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.server.NFlightServer;
import com.abreqadhabra.nflight.server.rmi.exception.NFlightRMIServerException;
import com.abreqadhabra.nflight.server.rmi.scoket.SecureSocketFactory;

public class RMIServerImpl /* extends UnicastRemoteObject */implements
		NFlightServer {

	private static final Class<RMIServerImpl> THIS_CLAZZ = RMIServerImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private Registry registry;

	private String host;
	private int port;
	private String registryName;

	private static final long serialVersionUID = 1L;

	public RMIServerImpl() throws Exception {
		try {
			this.host = InetAddress.getLocalHost().getHostAddress();// System.getProperty(Constants.Boot.KEY_BOOT_OPTION_HOST);
			this.port = 8888; // Integer.parseInt(System.getProperty(Constants.Boot.KEY_BOOT_OPTION_PORT));
			String registryID = "rmi://" + this.host + ":" + this.port;
			this.registryName = registryID + "/"
					+ Constants.RMIServer.STR_SERVICE_REGISTRY;
		} catch (UnknownHostException e) {
			throw new NFlightRMIServerException(e)
					.addContextValue("host", this.host)
					.addContextValue("port", this.port)
					.addContextValue("registryName", this.registryName);
		}
		execute();
	}

	private void execute() {
		final String METHOD_NAME = "void execute()";

		String serviceCommand = System
				.getProperty(Constants.Boot.KEY_BOOT_OPTION_SERVICE_COMMAND);

		try {
			if (serviceCommand
					.equals(Constants.Boot.STR_SERVICE_COMMAND_STARTUP)) {
				this.startup();
			} else if (serviceCommand
					.equals(Constants.Boot.STR_SERVICE_COMMAND_SHUTDOWN)) {
				if (this.checkStatus()) {
					this.shutdown();
				} else {
					LOGGER.logp(Level.WARNING, THIS_CLAZZ.getName(),
							METHOD_NAME,
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
				throw new NFlightRMIServerException("서버 기동이 실패하였습니다.")
						.addContextValue("serviceCommand", serviceCommand);
			}
		} catch (Exception e) {
			StackTraceElement[] current = e.getStackTrace();
			if (e instanceof WrapperException) {
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(),
						"\n" + WrapperException.getStackTrace(e));
				System.exit(-1);
			} else {
				LOGGER.logp(Level.SEVERE, current[0].getClassName(),
						current[0].getMethodName(),
						"\n" + WrapperException.getStackTrace(e));
				System.exit(-1);
			}
		}
	}

	@Override
	public void startup() throws Exception {
		final String METHOD_NAME = "startup()";

		try {
			/*
			 * Create remote object and export it to use custom secure socket
			 * factories.
			 */
			SecureSocketFactory socketFactory = new SecureSocketFactory();
			RMIClientSocketFactory csf = socketFactory;
			RMIServerSocketFactory ssf = socketFactory;
			NFlightServer stub = (NFlightServer) UnicastRemoteObject
					.exportObject(this, 0, csf, ssf);
			/*
			 * Create a registry and bind stub in registry.
			 */
			registry = getRMIRegistry(this.host, this.port);
			registry.rebind(registryName, stub);		

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"RMIServerImpl bound in registry");
		} catch (RemoteException re) {
			throw new NFlightRMIServerException(
					"Local RMI Registry creation failure", re)
					.addContextValue("host", host)
					.addContextValue("port", port)
					.addContextValue("registryName", registryName);
		} catch (Exception e) {
			if (e instanceof WrapperException) {
				throw e;
			} else {
				NFlightUnexpectedException ure = new NFlightUnexpectedException(
						e);
				ure.setContextValue("host", host);
				ure.setContextValue("port", port);
				ure.setContextValue("registryName", registryName);
				throw ure;
			}
		}
	}

	/**
	 * If an RMI registry is already active on this host at the given
	 * portNumber, then that registry is returned, otherwise a new registry is
	 * created and returned.
	 */
	/**
	 * Get the RMIRegistry. If a registry is already active on this host and the
	 * given portNumber, then that registry is returned, otherwise a new
	 * registry is created and returned.
	 * 
	 * @param portNumber
	 *            is the port on which the registry accepts requests
	 * @param host
	 *            host for the remote registry, if null the local host is used
	 * @throws Exception
	 **/
	private Registry getRMIRegistry(String host, int portNumber)
			throws Exception {
		final String METHOD_NAME = "Registry getRmiRegistry(String host, int portNumber)";

		Registry rmiRegistry = null;
		try {
			// See if a registry already exists and
			// make sure we can really talk to it.
			rmiRegistry = LocateRegistry.getRegistry(host, portNumber);

			if (LOGGER.isLoggable(Level.CONFIG)) {
				StringBuffer sb = new StringBuffer(
						"Local RMI Registry bindings:");
				String[] names = rmiRegistry.list();
				for (int i = 0; i < names.length; i++) {
					sb.append("\n[" + i + "] " + names[i] + "\n"
							+ rmiRegistry.lookup(names[i]));
				}
				LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getName(), METHOD_NAME,
						"Local RMI Registry on port " + portNumber
								+ " already exists. Use it\n" + sb.toString());
			}
		} catch (RemoteException re1) {
			try {
				rmiRegistry = LocateRegistry.createRegistry(portNumber);
				LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getName(), METHOD_NAME,
						"Local RMI Registry successfully created on port "
								+ portNumber);
			} catch (RemoteException re2) {
				throw new NFlightRMIServerException(
						"Local RMI Registry creation failure", re2)
						.addContextValue("host", host).addContextValue(
								"portNumber", portNumber);
			}
		}

		return rmiRegistry;
	} // END getRmiRegistry()

	@Override
	public void shutdown() throws Exception {
		final String METHOD_NAME = "shutdown()";
		
		try {
			registry = getRMIRegistry(this.host, this.port);
			if (isArraysContains(this.registry.list(), this.registryName)) {
				// Remove the RMI remote object from the RMI registry
				this.registry.unbind(this.registryName);
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"Remove the RMI remote object from the RMI registry");
			} else {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						 this.registryName + ": not found in registry");
			}
			this.exit();
		} catch (RemoteException re) {
			throw new NFlightRMIServerException("RMI does not exist on host "
					+ this.host + " or is not listening on port " + this.port,
					re);
		} catch (NotBoundException nbe) {
			throw new NFlightUnexpectedException("ignored, should not occur: "
					+ this.registryName, nbe);
		}
	}

	private boolean isArraysContains(Object[] arrays, String value) {
		return Arrays.asList(arrays).contains(value);		
	}

	/**
	 * <p>[機　能] データサーバを終了する。</p>
	 * <p>[説　明] データサーバを終了する。</p>
	 * <p>[備　考] なし</p>
	 * @throws RemoteException
	 */
	
	public void exit() {
		final String METHOD_NAME = "exit()";

		// 3초간 대기후 어플리케이션을 종료합니다.
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException ie) {
					// 이 예외는 발생하지 않습니다.
					LOGGER.logp(
							Level.FINER,
							THIS_CLAZZ.getName(),
							METHOD_NAME,
							"Thread was interrupted\n"
									+ WrapperException.getStackTrace(ie));
				}
				System.gc();
				System.runFinalization();			
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
						"system exit");
				System.exit(0);
			}
		}).start();
	}

	@Override
	public boolean checkStatus() throws Exception {
		
		//https://code.google.com/p/rmiregistrymonitor/source/browse/trunk/src/jj/rmirm/monitor/RMIMonitor.java?r=5
		
		return true;
	}

	@Override
	public boolean checkHealth() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
