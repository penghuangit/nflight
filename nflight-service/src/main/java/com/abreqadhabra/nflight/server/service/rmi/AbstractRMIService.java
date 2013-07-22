package com.abreqadhabra.nflight.server.service.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.server.service.IService;
import com.abreqadhabra.nflight.server.service.ServiceDescriptor;
import com.abreqadhabra.nflight.service.core.rmi.RMIServiceHelper;
import com.abreqadhabra.nflight.service.exception.NFRemoteException;

//Strategy ConcreteStrategy  / Flyweight ConcreteFlyweight
public abstract class AbstractRMIService implements IService {

	private static final Class<AbstractRMIService> THIS_CLAZZ = AbstractRMIService.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	protected ServiceDescriptor desc;
	protected String host;
	protected int port;
	protected Registry registry;
	protected String boundName;

	public AbstractRMIService(ServiceDescriptor _desc) throws Exception {
		this.desc = _desc;
		init();
	}

	@Override
	public void init() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Strategy -> ConcreteStrategy -> BehaviourInterface() : "
						+ METHOD_NAME);

		this.host = desc.getHost(); // InetAddress.getLocalHost().getHostAddress();
		this.port = desc.getPort(); // ; super.profile.getServicePort();
		this.boundName = RMIServiceHelper.getBoundName(this.host, this.port,
				this.desc.getServiceName());
		this.registry = RMIServiceHelper.getRegistry(this.host, this.port);

	}

	@Override
	public boolean status() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Strategy -> ConcreteStrategy -> BehaviourInterface() : "
						+ METHOD_NAME);

		boolean _isRunning = false;
		if (RMIServiceHelper.isActivatedRegistry(this.registry, this.boundName)) {
			IService service = (IService) RMIServiceHelper.lookup(
					this.registry, boundName);
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

	@Override
	public void shutdown() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Strategy -> ConcreteStrategy -> BehaviourInterface() : "
						+ METHOD_NAME);

		if (RMIServiceHelper.isActivatedRegistry(this.registry, this.boundName)) {
			try {
				RMIServiceHelper.unbind(this.registry, boundName);
			} catch (Exception e) {
				throw e;
			}
		} else {
			throw new NFRemoteException(boundName + "가 레지스트리에 등록되어 있지 않습니다.");
		}
	}

	@Override
	public boolean isRunning() throws Exception {
		return true;
	}

}
