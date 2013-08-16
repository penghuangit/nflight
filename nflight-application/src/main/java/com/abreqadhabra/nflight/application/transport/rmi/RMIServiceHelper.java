package com.abreqadhabra.nflight.application.transport.rmi;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.transport.exception.RMIServiceException;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.UnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class RMIServiceHelper {
	private static Class<RMIServiceHelper> THIS_CLAZZ = RMIServiceHelper.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	
	/**
	 * Get the RMIRegistry. If a registry is already active on this host and the
	 * given port, then that registry is returned, otherwise a new registry is
	 * created and returned.
	 * 
	 * @param host
	 *            host for the remote registry, if null the local host is used
	 * @param port
	 *            is the port on which the registry accepts requests
	 * @throws NFlightException 
	 * @throws Exception
	 **/
	public static Registry getRegistry(String host, int port) throws NFlightException
			 {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Registry _registry = null;
		try {
			// _registry = LocateRegistry.getRegistry(host, port,
			// socketFactory);
			_registry = LocateRegistry.getRegistry(host, port);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"already exist registry is returned." + _registry + ":"
							+ Arrays.toString(_registry.list()));
		} catch (RemoteException re) {
			try {
				// _registry = LocateRegistry.createRegistry(port,
				// socketFactory,socketFactory);
				_registry = LocateRegistry.createRegistry(port);
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"New registry(using custom socket factories) is created and returned.");
			} catch (RemoteException re1) {
				throw new RMIServiceException(
						"Local RMI Registry creation failure", re1)
						.addContextValue("host", host).addContextValue("port",
								port);
			}
		}

		return _registry;
	}
	
	
	
	
	public static String getBoundName(String host, int port,
			String objName) {
		return "rmi://" + host + ":" + port + "/" + objName;
	}

	public static List<String> getBoundNameList(Registry registry) throws NFlightException
			{
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		List<String> _boundNameList = null;
		try {
			String[] names = registry.list();

			_boundNameList = Arrays.asList(registry.list());
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Found " + _boundNameList.size() + " registiries: "
							+ Arrays.toString(names));
		} catch (RemoteException e) {
			throw new RMIServiceException("Cannot connect to RMI registry: "
					+ registry.toString(), e);
		}

		return _boundNameList;
	}

	/*
	 * public Remote exportRemoteObject(Remote obj) throws Exception { try { //
	 * Create remote object and export it to // use custom secure socket obj =
	 * UnicastRemoteObject.exportObject(obj, 0, socketFactory, socketFactory); }
	 * catch (RemoteException e) { throw new NFlightRemoteException(
	 * "Cannot export to Remote Object: " + obj.getClass().getName(), e); }
	 * 
	 * return obj; }
	 */
	public static boolean isActivatedRegistry(Registry registry,
			String name) throws NFlightException  {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		if (getBoundNameList(registry).contains(name)) {
			return true;
		} else {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, name
					+ ": not found in registry");
			return false;
		}
	}

	public static Remote lookup(Registry registry, String boundName)
			throws Exception {
		Remote _obj = null;

		try {
			_obj = registry.lookup(boundName);
		} catch (RemoteException re) {
			throw new RMIServiceException(
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
	public static void rebind(Registry registry, String name,
			Remote obj) throws NFlightException  {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			registry.rebind(name, obj);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"RMIServerImpl bound in registry: " + name);
		} catch (RemoteException e) {
			throw new RMIServiceException("Cannot bound to Remote Object: "
					+ name, e);
		}
	}

	/*
	 * Remove the RMI remote object from the RMI registry
	 */
	public static void unbind(Registry registry, String boundName)
			throws Exception {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			registry.unbind(boundName);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Remove the RMI remote object from the RMI registry:"
							+ boundName);
		} catch (RemoteException re) {
			throw new RMIServiceException(
					"Remote communication with the registry failed", re)
					.addContextValue("boundName", boundName);
		} catch (NotBoundException nbe) {
			throw new UnexpectedException(boundName
					+ " is not currently bound", nbe);
		}
	}

}
