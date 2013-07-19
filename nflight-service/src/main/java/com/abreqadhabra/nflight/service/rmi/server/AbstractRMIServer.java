package com.abreqadhabra.nflight.service.rmi.server;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.core.rmi.RegistryManager;
import com.abreqadhabra.nflight.service.core.server.AbstractServerReceiver;
import com.abreqadhabra.nflight.service.core.server.IService;
import com.abreqadhabra.nflight.service.exception.NFRemoteException;

public abstract class AbstractRMIServer extends AbstractServerReceiver {

	// private static final String READY = "Ready";
	// private static final String CREATE_REGISTRY_OK =
	// "Local RMI registry is running on port ";
	// private static final String CREATE_REGISTRY_ERR =
	// "Cannot run local RMI registry on port: ";
	// private static final String REGISTRY_STOPPED_OK = "Registy stopped";
	// private static final String REGISTRY_STOPPED_ERR =
	// "Error while stopping local registry. Already stopped?";
	// private static final String BLOCKED_BY_REMOTE_REG =
	// "Blocked by remote registry: ";
	// private static final String CANNOT_CONNECT =
	// "Cannot connect ro RMI registry: ";
	// private static final String SUCCESFULLY_UNBOUND = " succesfully unbound";
	// private static final String UNBIND_ERROR = "There is no such object: ";

	private static final Class<AbstractRMIServer> THIS_CLAZZ = AbstractRMIServer.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	protected String host;
	protected int port;
	protected Registry registry;
	protected String boundName;

	public AbstractRMIServer(ProfileImpl profile, IService service)
			throws Exception {
		super(profile, service);
	}

	@Override
	public void init() throws Exception {
		this.host = InetAddress.getLocalHost().getHostAddress();
		this.port = super.profile.getServicePort();
		this.boundName = RegistryManager.getBoundName(this.host, this.port,
				this.service.getServiceName());
		this.registry = RegistryManager.getRegistry(this.host, this.port);
	}

	@Override
	public void shutdown() throws Exception {
		if (RegistryManager.isActivatedRegistry(this.registry, this.boundName)) {
			try {
				RegistryManager.unbind(this.registry, boundName);
			} catch (Exception e) {
				throw e;
			}
		} else {
			throw new NFRemoteException(boundName + "가 레지스트리에 등록되어 있지 않습니다.");
		}
	}

	@Override
	public boolean status() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		boolean _isRunning = false;
		if (RegistryManager.isActivatedRegistry(this.registry, this.boundName)) {
			IService service = (IService) RegistryManager.lookup(this.registry,
					boundName);
			if (service != null) {
				try {
					_isRunning = service.isRunning();
				} catch (RemoteException re) {
					throw new NFRemoteException("서버의 상태를 확인할 수 없습니다.", re)
							.addContextValue("service", service)
							.addContextValue("isRunning", _isRunning);
				}
			}
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					boundName + " is running: " + _isRunning);

			return _isRunning;
		} else {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					boundName + "가 레지스트리에 등록되어 있지 않습니다.");
		}

		return _isRunning;
	}
}
