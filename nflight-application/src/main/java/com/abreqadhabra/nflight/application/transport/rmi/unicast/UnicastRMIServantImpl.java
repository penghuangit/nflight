package com.abreqadhabra.nflight.application.transport.rmi.unicast;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.concurrency.AbstractRunnable;
import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.transport.exception.RMIServiceException;
import com.abreqadhabra.nflight.application.transport.rmi.RMIServiceHelper;
import com.abreqadhabra.nflight.application.transport.rmi.Servant;
import com.abreqadhabra.nflight.application.transport.rmi.ServantShutdownHook;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.exception.UnexpectedRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class UnicastRMIServantImpl extends AbstractRunnable implements Servant {
	private static Class<UnicastRMIServantImpl> THIS_CLAZZ = UnicastRMIServantImpl.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private InetAddress addr;
	private int port;

	private Registry registry;
	private String boundName;
	private String serviceName;

	public UnicastRMIServantImpl(InetAddress addr, int port)
			throws NFlightRemoteException {
		super.setShutdownHookThread(new ServantShutdownHook(this).getThread());
		this.addr = addr;
		this.port = port;
		this.registry = RMIServiceHelper.getRegistry(
				this.addr.getHostAddress(), this.port);
		this.serviceName = Configure.SERVICE_TYPE.rmi_unicast.toString();
		this.boundName = RMIServiceHelper.getBoundName(addr.getHostAddress(),
				port, this.serviceName);
	}

	@Override
	public void start() throws NFlightRemoteException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			if (RMIServiceHelper.isActivatedRegistry(this.registry,
					this.boundName)) {
				throw new RMIServiceException(this.boundName
						+ "가 레지스트리에 이미 등록되어 있습니다.");
			} else {
				Remote stub = UnicastRemoteObject.exportObject(this, 0);
				RMIServiceHelper.rebind(this.registry, this.boundName, stub);
				LOGGER.logp(
						Level.FINER,
						THIS_CLAZZ.getName(),
						METHOD_NAME,
						stub + " Stub bound in registry."
								+ Arrays.toString(this.registry.list()));
			}
		} catch (RemoteException e) {
			throw new RMIServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedRemoteException(e);
		}
	}

	@Override
	public void stop() throws NFlightRemoteException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			LOGGER.logp(Level.SEVERE, CLAZZ_NAME, METHOD_NAME, "Stopping...");
			this.registry.unbind(this.boundName);
			LOGGER.logp(Level.SEVERE, CLAZZ_NAME, METHOD_NAME, "Stopped");
		} catch (IOException e) {
			throw new RMIServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedRemoteException(e);
		} finally {
			super.interrupt();
		}
	}

	@Override
	public String sayHello() throws NFlightRemoteException {
		return "Hello, world!";
	}
}
