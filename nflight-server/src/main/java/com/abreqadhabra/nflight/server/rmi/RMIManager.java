package com.abreqadhabra.nflight.server.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.server.rmi.exception.NFlightRMIServerException;
import com.abreqadhabra.nflight.server.rmi.scoket.SecureSocketFactory;

public class RMIManager {
	
	private static final Class<RMIManager> THIS_CLAZZ = RMIManager.class;
	private static final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();
	
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
	
	private boolean isRunning;
	private Registry registry;
	private List<String> registryList;

	public RMIManager(String host, int port) throws Exception {
		this.registry = getLocalRegistry(host, port);
		this.registryList = getRegistryList(host, port);
	}
	
	public Registry getLocalRegistry() {
		return this.registry;
	}
	
	public List<String> getRegistryList() {
		return this.registryList;
	}
	
	/**
	 * Get the RMIRegistry. If a registry is already active on this host and the
	 * given port, then that registry is returned, otherwise a new
	 * registry is created and returned.
	 * 
	 * @param host
	 *            host for the remote registry, if null the local host is used
	 * @param port
	 *            is the port on which the registry accepts requests
	 * @throws Exception
	 **/
	public Registry getLocalRegistry(String host, int port) throws Exception {
		final String METHOD_NAME = "--2-----" + Thread.currentThread().getStackTrace()[1].getMethodName();
		
		Registry _localRegistry = null;
		SecureSocketFactory socketFactory = new SecureSocketFactory();
		try {
			_localRegistry = LocateRegistry.getRegistry(host, port,
					socketFactory);
			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getName(),
					METHOD_NAME,
					"already exist registry is returned."
							+ Arrays.toString(_localRegistry.list()));
		} catch (RemoteException re) {
			try {
				_localRegistry = LocateRegistry.createRegistry(port,
						socketFactory, socketFactory);
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"New registry(using custom socket factories) is created and returned.");
			} catch (RemoteException re1) {
				throw new NFlightRMIServerException(
						"Local RMI Registry creation failure", re1)
						.addContextValue("host", host).addContextValue(
								"port", port);
			}
		}
		
		return _localRegistry;
	}
	
	
	public List<String> getRegistryList(String host, int port) throws Exception {
		final String METHOD_NAME = "--1-----"+ new Throwable().getStackTrace()[0].getMethodName();

		List<String> _registryList = null;
		try {
			Registry registry = getLocalRegistry(host, port);
			_registryList = Arrays.asList(registry.list());
			this.registryList = _registryList;
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Found " + registryList.size() + " registiries");
		} catch (RemoteException e) {
			throw new NFlightRMIServerException(
					"Cannot connect to RMI registry: " + host + ":"
							+ Integer.toBinaryString(port), e);
		} catch (Exception e) {
			throw e;
		}

		return _registryList;
	}




	

}