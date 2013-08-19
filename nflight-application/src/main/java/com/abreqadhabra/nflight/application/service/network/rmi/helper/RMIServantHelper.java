package com.abreqadhabra.nflight.application.service.network.rmi.helper;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.service.network.rmi.exception.RMIServantException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class RMIServantHelper {
	private static Class<RMIServantHelper> THIS_CLAZZ = RMIServantHelper.class;
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
	 * @throws NFlightRemoteException
	 * @throws Exception
	 **/
	public static Registry getRegistry(String host, int port)
			throws NFlightRemoteException {
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
				throw new RMIServantException(
						"Local RMI Registry creation failure", re1)
						.addContextValue("host", host).addContextValue("port",
								port);
			}
		}

		return _registry;
	}

	public static String getBoundName(String host, int port, String objName) {
		return "rmi://" + host + ":" + port + "/" + objName;
	}

	private static Remote lookup(Registry registry, String boundName)
			throws Exception {
		Remote _obj = null;

		try {
			_obj = registry.lookup(boundName);
		} catch (RemoteException re) {
			throw new RMIServantException(
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

}
