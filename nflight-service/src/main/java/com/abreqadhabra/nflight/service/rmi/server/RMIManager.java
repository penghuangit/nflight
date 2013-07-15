package com.abreqadhabra.nflight.service.rmi.server;

import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.NFlightUnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.boot.Profile;
import com.abreqadhabra.nflight.service.rmi.server.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.service.rmi.server.scoket.SecureSocketFactory;
import com.abreqadhabra.nflight.service.rmi.server.servant.UnicastRemoteObjectNFlightServiceImpl;

public class RMIManager {

	private static final Class<RMIManager> THIS_CLAZZ = RMIManager.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private static final String READY = "Ready";
	private static final String CREATE_REGISTRY_OK = "Local RMI registry is running on port ";
	private static final String CREATE_REGISTRY_ERR = "Cannot run local RMI registry on port: ";
	private static final String REGISTRY_STOPPED_OK = "Registy stopped";
	private static final String REGISTRY_STOPPED_ERR = "Error while stopping local registry. Already stopped?";
	private static final String BLOCKED_BY_REMOTE_REG = "Blocked by remote registry: ";
	private static final String CANNOT_CONNECT = "Cannot connect ro RMI registry: ";
	private static final String SUCCESFULLY_UNBOUND = " succesfully unbound";
	private static final String UNBIND_ERROR = "There is no such object: ";

	private Registry registry;
	private List<String> boundNameList;
	private static SecureSocketFactory socketFactory =  new SecureSocketFactory();
	private String host;
	private int port;

	public RMIManager() throws Exception {
		this.host = InetAddress.getLocalHost().getHostAddress();
		this.port = Integer.parseInt(System
				.getProperty(Profile.PROPERTIES_BOOT.NFLIGHT_BOOT_OPTION_SERVICE_PORT.toString()));
		this.registry = getRegistry(this.host, this.port);
		this.boundNameList = getBoundNameList(this.host, this.port);
		this.socketFactory = new SecureSocketFactory();
	}

	/**
	 * Get the RMIRegistry. If a registry is already active on this host and the
	 * given port, then that registry is returned, otherwise a new registry is
	 * created and returned.
	 * 
	 * @param host
	 *            host for the remote registry, if null the local host is used
	 * @param port
	 *            is the port on which the registry accepts requests
	 * @throws Exception
	 **/
	public static Registry getRegistry(String host, int port) throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Registry _registry = null;
		try {
			_registry = LocateRegistry.getRegistry(host, port, socketFactory);
			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getName(),
					METHOD_NAME,
					"already exist registry is returned."
							+ Arrays.toString(_registry.list()));
		} catch (RemoteException re) {
			try {
				_registry = LocateRegistry.createRegistry(port, socketFactory,
						socketFactory);
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"New registry(using custom socket factories) is created and returned.");
			} catch (RemoteException re1) {
				throw new NFlightRemoteException(
						"Local RMI Registry creation failure", re1)
						.addContextValue("host", host).addContextValue("port",
								port);
			}
		}

		return _registry;
	}

	public List<String> getBoundNameList(String host, int port)
			throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		List<String> _boundNameList = null;
		try {
			_boundNameList = Arrays.asList(this.registry.list());
			this.boundNameList = _boundNameList;
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Found " + boundNameList.size() + " registiries");
		} catch (RemoteException e) {
			throw new NFlightRemoteException(
					"Cannot connect to RMI registry: " + host + ":"
							+ Integer.toBinaryString(port), e);
		}

		return _boundNameList;
	}

	public Remote lookup(String boundName) throws Exception {
		Remote _obj = null;

		try {
			_obj = registry.lookup(boundName);
		} catch (RemoteException re) {
			throw new NFlightRemoteException(
					"Exception occured during connecting Remote Object.", re)
					.addContextValue("boundName", boundName);
		} catch (NotBoundException e) {
			StackTraceElement[] current = e.getStackTrace();
			LOGGER.logp(Level.FINER, current[0].getClassName(),
					current[0].getMethodName(),
					"Ignored NotBoundException occured during connecting Remote Object. "
							+ e.getLocalizedMessage());
		}

		return _obj;
	}
	
	/*
	 * rebind stub in registry.
	 */
	public void rebind(String name, Remote obj) throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			this.registry.rebind(name, obj);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"RMIServerImpl bound in registry: " + name);
		} catch (RemoteException e) {
			throw new NFlightRemoteException(
					"Cannot bound to Remote Object: " + name, e);
		}
	}

	/*
	 * Remove the RMI remote object from the RMI registry
	 */
	public void unbind(String name) throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			this.registry.unbind(name);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Remove the RMI remote object from the RMI registry");
		} catch (RemoteException re) {
			throw new NFlightRemoteException(
					"Remote communication with the registry failed", re);
		} catch (NotBoundException nbe) {
			throw new NFlightUnexpectedException(name
					+ " is not currently bound", nbe);
		}
	}
/*
	public Remote exportRemoteObject(Remote obj) throws Exception {
		try {
			// Create remote object and export it to
			// use custom secure socket
			obj = UnicastRemoteObject.exportObject(obj, 0, socketFactory,
					socketFactory);
		} catch (RemoteException e) {
			throw new NFlightRemoteException(
					"Cannot export to Remote Object: "
							+ obj.getClass().getName(), e);
		}

		return obj;
	}
*/
	public boolean isActivatedRegistry(String name) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		if (this.boundNameList.contains(name)) {
			return true;
		} else {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, name
					+ ": not found in registry");
			return false;
		}
	}

	public String getBoundName(String objName) {
		return "rmi://" + this.host + ":" + this.port + "/" + objName;
	}

	public Remote getUnicastRemoteObjectNFlightServiceImpl() throws Exception {
		//Remote _obj = new UnicastRemoteObjectNFlightServiceImpl(0,socketFactory, socketFactory);
		Remote _obj = (Remote) new UnicastRemoteObjectNFlightServiceImpl();
		try {
			// Create remote object and export it to
			// use custom secure socket
			//_obj = UnicastRemoteObject.exportObject(_obj, 0, socketFactory, socketFactory);
			_obj = UnicastRemoteObject.toStub(_obj);
		} catch (RemoteException e) {
			throw new NFlightRemoteException("Cannot export to Remote Object: "
					+ _obj.getClass().getName(), e);
		}

		return _obj;
	}



}