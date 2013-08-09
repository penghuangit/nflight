package com.abreqadhabra.nflight.app.server.service.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.app.core.exception.NFRemoteException;
import com.abreqadhabra.nflight.app.core.rmi.RMIServiceHelper;
import com.abreqadhabra.nflight.app.server.service.IService;
import com.abreqadhabra.nflight.app.server.service.ServiceDescriptor;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

//Strategy ConcreteStrategy  / Flyweight ConcreteFlyweight
public abstract class AbstractServant implements IService {

	private static final Class<AbstractServant> THIS_CLAZZ = AbstractServant.class;
	private static Logger LOGGER = LoggingHelper
			.getLogger(THIS_CLAZZ);

	protected ServiceDescriptor desc;
	protected String host;
	protected String address;
	protected int port;
	protected Registry registry;
	protected String boundName;

	public AbstractServant() {
	}

	public AbstractServant(final ServiceDescriptor _desc) throws Exception {
		this.desc = _desc;
		this.init();
	}

	@Override
	public void init() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER,
				THIS_CLAZZ.getName(), METHOD_NAME,
				"Strategy -> ConcreteStrategy -> BehaviourInterface() : "
						+ METHOD_NAME);

		this.host = this.desc.getHost(); // InetAddress.getLocalHost().getHostAddress();
		this.address = this.desc.getAddress();
		this.port = this.desc.getPort(); // ; super.profile.getServicePort();
		this.boundName = RMIServiceHelper.getBoundName(this.host, this.port,
				this.desc.getServiceName());
		this.registry = RMIServiceHelper.getRegistry(this.host, this.port);

	}

	@Override
	public boolean isRunning() throws RemoteException {
		return true;
	}

	@Override
	public String sayHello() throws RemoteException {
		return "Hello, world!";
	}

	@Override
	public void shutdown() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER,
				THIS_CLAZZ.getName(), METHOD_NAME,
				"Strategy -> ConcreteStrategy -> BehaviourInterface() : "
						+ METHOD_NAME);

		if (RMIServiceHelper.isActivatedRegistry(this.registry, this.boundName)) {
			try {
				RMIServiceHelper.unbind(this.registry, this.boundName);
			} catch (final Exception e) {
				throw e;
			}
		} else {
			throw new NFRemoteException(this.boundName
					+ "가 레지스트리에 등록되어 있지 않습니다.");
		}
	}

	@Override
	public boolean status() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER,
				THIS_CLAZZ.getName(), METHOD_NAME,
				"Strategy -> ConcreteStrategy -> BehaviourInterface() : "
						+ METHOD_NAME);

		boolean _isRunning = false;
		if (RMIServiceHelper.isActivatedRegistry(this.registry, this.boundName)) {
			final IService service = (IService) RMIServiceHelper.lookup(
					this.registry, this.boundName);
			if (service != null) {
				try {
					_isRunning = service.isRunning();
				} catch (final RemoteException re) {
					throw new NFRemoteException(re.getLocalizedMessage(), re)
							.addContextValue("service", service)
							.addContextValue("boundName", this.boundName)
							.addContextValue("isRunning", _isRunning);
				}
			}
			LOGGER.logp(Level.FINER,
					THIS_CLAZZ.getName(), METHOD_NAME,
					this.boundName + " is running: " + _isRunning);

			return _isRunning;
		} else {
			LOGGER.logp(Level.FINER,
					THIS_CLAZZ.getName(), METHOD_NAME,
					this.boundName + "가 레지스트리에 등록되어 있지 않습니다.");
		}

		return _isRunning;
	}
}
