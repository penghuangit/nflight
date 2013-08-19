package com.abreqadhabra.nflight.application.service.network.rmi.helper;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.service.network.rmi.exception.RMIServantException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.exception.UnexpectedRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class UnicastRMIServantHelper extends RMIServantHelper {
	private static Class<UnicastRMIServantHelper> THIS_CLAZZ = UnicastRMIServantHelper.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	/*
	 * public Remote exportRemoteObject(Remote obj) throws Exception { try { //
	 * Create remote object and export it to // use custom secure socket obj =
	 * UnicastRemoteObject.exportObject(obj, 0, socketFactory, socketFactory); }
	 * catch (RemoteException e) { throw new NFlightRemoteException(
	 * "Cannot export to Remote Object: " + obj.getClass().getName(), e); }
	 * 
	 * return obj; }
	 */
	public static boolean isActivatedRegistry(Registry registry, String name)
			throws NFlightRemoteException {
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

	/*
	 * rebind stub in registry.
	 */
	public static void rebind(Registry registry, String name, Remote obj)
			throws NFlightRemoteException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			registry.rebind(name, obj);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"RMIServerImpl bound in registry: " + name);
		} catch (RemoteException e) {
			throw new RMIServantException("Cannot bound to Remote Object: "
					+ name, e);
		}
	}

	private static List<String> getBoundNameList(Registry registry)
			throws NFlightRemoteException {
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
			throw new RMIServantException("Cannot connect to RMI registry: "
					+ registry.toString(), e);
		}

		return _boundNameList;
	}

	/*
	 * Remove the RMI remote object from the RMI registry
	 */
	public static void unbind(Registry registry, String boundName)
			throws NFlightRemoteException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			registry.unbind(boundName);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Remove the RMI remote object from the RMI registry:"
							+ boundName);
		} catch (RemoteException re) {
			throw new RMIServantException(
					"Remote communication with the registry failed", re)
					.addContextValue("boundName", boundName);
		} catch (NotBoundException nbe) {
			throw new UnexpectedRemoteException(boundName
					+ " is not currently bound", nbe);
		}
	}

}
