package com.abreqadhabra.nflight.service.rmi.server;

import java.net.InetAddress;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupDesc;
import java.rmi.activation.ActivationGroupID;
import java.rmi.activation.ActivationSystem;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.common.util.PropertyLoader;
import com.abreqadhabra.nflight.service.core.NFService;
import com.abreqadhabra.nflight.service.core.boot.BootProfile;
import com.abreqadhabra.nflight.service.core.boot.Profile;
import com.abreqadhabra.nflight.service.core.server.NFServer;
import com.abreqadhabra.nflight.service.rmi.server.exception.NFRemoteException;
import com.abreqadhabra.nflight.service.rmi.server.servant.NFServiceRMIActivatableImpl;
import com.abreqadhabra.nflight.service.rmi.server.servant.NFServiceUnicastRemoteObjectImpl;

public class NFServerRMIImpl implements NFServer {

	private static final Class<NFServerRMIImpl> THIS_CLAZZ = NFServerRMIImpl.class;
	private Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private BootProfile bootProfile;

	private RMIManager rman;
	private static final long serialVersionUID = 1L;

	public NFServerRMIImpl(BootProfile profile) throws Exception {
		this.bootProfile = profile;
		init();
	}

	private void init() throws Exception {
		this.rman = new RMIManager();

		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		String boundName = this.rman
				.getBoundName(NFServiceUnicastRemoteObjectImpl.class
						.getSimpleName());

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"boundName: " + boundName);

		boolean _isActivated = this.rman.isActivatedRegistry(boundName);

		String serviceCommand = this.bootProfile.getServiceCommand();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"serviceCommand: " + serviceCommand);

		Profile.BOOT_OPTION_SERVICE_COMMAND command = Profile.BOOT_OPTION_SERVICE_COMMAND
				.valueOf(serviceCommand);

		switch (command) {
		case startup:
			if (_isActivated) {
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
						boundName + "가 이미 Registry에 등록되어 있습니다.");
			} else {
				this.startup();
			}
			break;
		case shutdown:
			if (_isActivated) {
				this.shutdown();
			} else {
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
						boundName + "가  Registry에 등록되어 있지 않습니다.");
			}
			break;
		case status:
			if (_isActivated) {
				boolean status = this.status();
				if (status == true) {
					LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
							"서비스가 정상적으로 기동 중에 있습니다.");
				} else if (status == false) {
					LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
							"서비스를 찾을 수 없습니다.");
				}
			} else {
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
						boundName + "가  Registry에 등록되어 있지 않습니다.");
			}
			break;
		default:
			throw new NFRemoteException("Service 실행이 실패하였습니다.")
					.addContextValue("Service Command", serviceCommand);
		}
	}

	@Override
	public void startup() throws Exception {
		startupUnicastRemoteObjectService();
		startupActivatableService();
	}

	private void startupActivatableService() throws Exception {

		Properties _props = PropertyFile
				.readPropertyFile(Profile.FILE_ACTIVATION_PROPERTIES);
		_props.put(
				Profile.PROPERTIES_ACTIVATION.NFLIGHT_SERVANT_ACTIVATION_IMPL_CODEBASE
						.toString(), Profile.ACTIVATION_FILE_PREFIX
						+ bootProfile.getCodeBase());

		PropertyLoader.setSystemProperties(_props);

		String securityPolicy = bootProfile.getCodeBase() + Profile.FILE_ACTIVATION_POLICY;
		System.out.println(securityPolicy);

		System.setProperty(
				Env.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString(),
				securityPolicy);

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		String implCodebase = System
				.getProperty(Profile.PROPERTIES_ACTIVATION.NFLIGHT_SERVANT_ACTIVATION_IMPL_CODEBASE
						.toString());
		String filename = implCodebase
				+ System.getProperty(Profile.PROPERTIES_ACTIVATION.NFLIGHT_SERVANT_ACTIVATION_FILE
						.toString());
		String groupPolicy = implCodebase
				+ System.getProperty(Profile.PROPERTIES_ACTIVATION.NFLIGHT_SERVANT_ACTIVATION_POLICY
						.toString());
		String implClass = System
				.getProperty(Profile.PROPERTIES_ACTIVATION.NFLIGHT_SERVANT_ACTIVATION_IMPL_CLASS
						.toString());

		Properties props = new Properties();
		props.put("java.security.policy", groupPolicy);
		props.put("java.class.path", "no_classpath");
		props.put("examples.activation.impl.codebase", implCodebase);

		ActivationGroupDesc groupDesc = new ActivationGroupDesc(props, null);

		ActivationSystem system = ActivationGroup.getSystem();

		ActivationGroupID groupID = system.registerGroup(groupDesc);

		System.err.println("Activation group descriptor registered.");

		// Pass the file that we want to persist to as the Marshalled
		// object
		MarshalledObject<?> data = null;
		if (filename != null && !filename.equals("")) {
			data = new MarshalledObject(filename);
		}

		ActivationDesc desc = new ActivationDesc(groupID, implClass,
				implCodebase, data);

		Remote stub = Activatable.register(desc);
		System.err.println("Activation descriptor registered.");

		String host = InetAddress.getLocalHost().getHostAddress();
		int port = 9999;

		String name = "rmi://" + host + ":" + port + "/"
				+ NFServiceRMIActivatableImpl.class.getSimpleName();

		Registry registry = RMIManager.getRegistry(host, port);
		registry.rebind(name, stub);
		System.err.println("Stub bound in registry."
				+ Arrays.toString(registry.list()));

	}

	private void startupUnicastRemoteObjectService() throws Exception {

		try {
			// NFlightService uniServant = new
			// UnicastRemoteObjectNFlightServiceImpl();
			// NFlightService actServant = new ActivatableNFlightServiceImpl();

			Remote obj = this.rman.getUnicastRemoteObjectNFlightServiceImpl();
			// Remote obj = this.rman.getActivatableNFlightServiceImpl();
			String boundName = this.rman
					.getBoundName(NFServiceUnicastRemoteObjectImpl
							.getObjName());
			this.rman.rebind(boundName, obj);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void shutdown() throws Exception {
		try {
			String boundName = this.rman
					.getBoundName(NFServiceUnicastRemoteObjectImpl
							.getObjName());
			rman.unbind(boundName);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public boolean status() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		String boundName = this.rman
				.getBoundName(NFServiceUnicastRemoteObjectImpl
						.getObjName());

		NFService service = (NFService) this.rman.lookup(boundName);
		boolean _isRunning = false;
		if (service != null) {
			try {
				_isRunning = service.isRunning();
			} catch (RemoteException re) {
				throw new NFRemoteException("데이터서버의 상태를 확인할 수 없습니다.", re)
						.addContextValue("service", service).addContextValue(
								"isRunning", _isRunning);
			}
		}
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"_isRunning: " + _isRunning);

		return _isRunning;
	}

}
