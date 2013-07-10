package com.abreqadhabra.nflight.server.rmi;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
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
	RMIManager rman;
	private boolean existLocalRegustry;
	private static final long serialVersionUID = 1L;

	public RMIServerImpl() throws Exception  {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		try {
			this.host = InetAddress.getLocalHost().getHostAddress();// System.getProperty(Constants.Boot.KEY_BOOT_OPTION_HOST);
			this.port = 8888; // Integer.parseInt(System.getProperty(Constants.Boot.KEY_BOOT_OPTION_PORT));
			String registryID = "rmi://" + this.host + ":" + this.port;
			this.registryName = registryID + "/"
					+ Constants.RMIServer.STR_SERVICE_REGISTRY;
			
			this.rman = new RMIManager(host, port);
			
			this.registry = this.rman.getLocalRegistry();
			
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					rman.getRegistryList().toString());
						
			if(this.rman.getRegistryList().contains(this.registryName)) {
				this.existLocalRegustry = true;
			} else {
				this.existLocalRegustry = false;
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						this.registryName + ": not found in registry");
			}
			execute();
		} catch (UnknownHostException | RemoteException e1) {
			throw new NFlightRMIServerException(e1)
					.addContextValue("host", this.host)
					.addContextValue("port", this.port)
					.addContextValue("registryName", this.registryName);
		}catch(Exception e2){
			throw e2;
		}
	}

	private void execute() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		String serviceCommand = System
				.getProperty(Constants.Boot.KEY_BOOT_OPTION_SERVICE_COMMAND);	
		
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Execute Service Command: " + serviceCommand);
		
		try {
			if (serviceCommand
					.equalsIgnoreCase(Constants.Boot.STR_SERVICE_COMMAND_STARTUP)) {
				if (this.existLocalRegustry) {
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
							this.registryName + "이 Registry에 등록되어 있습니다.");
					this.exit();
				} else {
					this.startup();
				}
			} else if (serviceCommand
					.equalsIgnoreCase(Constants.Boot.STR_SERVICE_COMMAND_SHUTDOWN)) {
					if (this.existLocalRegustry) {
						this.shutdown();
					} else {
						LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(),
								METHOD_NAME, this.registryName + "이 Registry에 등록되어 있지 않습니다.");
						this.exit();
					}
			} else if (serviceCommand
					.equalsIgnoreCase(Constants.Boot.STR_SERVICE_COMMAND_STATUS)) {
				boolean status = checkStatus();
			} else {
				throw new NFlightRMIServerException("Service Command 실행이 실패하였습니다.")
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
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

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
/*			registry = getRMIRegistry(this.host, this.port);*/	
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

	@Override
	public void shutdown() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		try {
			// Remove the RMI remote object from the RMI registry
			this.registry.unbind(this.registryName);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Remove the RMI remote object from the RMI registry");
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

	/**
	 * <p>[機　能] データサーバを終了する。</p>
	 * <p>[説　明] データサーバを終了する。</p>
	 * <p>[備　考] なし</p>
	 * @throws RemoteException
	 */
	
	public void exit() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

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
