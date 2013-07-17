package com.abreqadhabra.nflight.service.server.rmi;

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
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.common.util.PropertyLoader;
import com.abreqadhabra.nflight.service.boot.BootProfile;
import com.abreqadhabra.nflight.service.boot.Profile;
import com.abreqadhabra.nflight.service.server.NFServer;
import com.abreqadhabra.nflight.service.server.NFService;
import com.abreqadhabra.nflight.service.server.rmi.exception.NFRemoteException;

public class NFServerRMIImpl extends NFServer {

	private static final Class<NFServerRMIImpl> THIS_CLAZZ = NFServerRMIImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private static String[] SERVICE_NAMES = { "unicast", "activatable" };
	private RMIManager rman;
	boolean isActivated = false;
	
	public NFServerRMIImpl(BootProfile profile) throws Exception {
		super(profile);
	}
	
	@Override
	public void init() throws Exception {
		this.rman = new RMIManager();
		this.isActivated = isRegistered();
	}

	private boolean isRegistered() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		List<String> serviceNameList = Arrays.asList(SERVICE_NAMES);
		for (int i = 0; i < serviceNameList.size(); i++) {
			String serviceName = serviceNameList.get(i);
			System.out.println(serviceName);

			if (this.rman.isActivatedRegistry(serviceName)) {
				return true;
			} else {
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getName(), METHOD_NAME,
						serviceName + "가  Registry에 등록되어 있지 않습니다.");
			}
		}

		return false;
	}
	
	@Override
	public void startup() throws Exception {
		if (!isActivated) {
			startupUnicastRemoteObjectService();
			startupActivatableService();
		}
	}



	@Override
	public void shutdown() throws Exception {
		if (isActivated) {
			try {
				List<String> serviceNameList = Arrays.asList(this.SERVICE_NAMES);
				for (int i = 0; i < serviceNameList.size(); i++) {
					String serviceName = serviceNameList.get(i);
					rman.unbind(serviceName);
				}
			} catch (Exception e) {
				throw e;
			}
		}
	}

	@Override
	public boolean status() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		boolean _isRunning = false;
		if (isActivated) {
			List<String> serviceNameList = Arrays.asList(this.SERVICE_NAMES);
			for (int i = 0; i < serviceNameList.size(); i++) {
				String serviceName = serviceNameList.get(i);
				NFService service = (NFService) this.rman.lookup(serviceName);
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
						"serviceName is running: " + _isRunning);
			}

			return _isRunning;
		}

		return _isRunning;
	}

	private void startupUnicastRemoteObjectService() throws Exception {

		try {
			// NFlightService uniServant = new
			// UnicastRemoteObjectNFlightServiceImpl();
			// NFlightService actServant = new ActivatableNFlightServiceImpl();

			Remote obj = this.rman.getUnicastRemoteObjectNFlightServiceImpl();
			// Remote obj = this.rman.getActivatableNFlightServiceImpl();

			this.rman.rebind("unicast", obj);
		} catch (Exception e) {
			throw e;
		}
	}

	private void startupActivatableService() throws Exception {

		Properties _props = PropertyFile
				.readPropertyFilePath(THIS_CLAZZ.getName() ,Profile.FILE_ACTIVATION_PROPERTIES);
		String codeBase = super.getBootPofile().getCodeBase();
		_props.put(
				Profile.PROPERTIES_ACTIVATION.NFLIGHT_SERVANT_ACTIVATION_IMPL_CODEBASE
						.toString(), Profile.ACTIVATION_FILE_PREFIX + codeBase);

		PropertyLoader.setSystemProperties(_props);

		String securityPolicy = codeBase + Profile.FILE_ACTIVATION_POLICY;
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

		Registry registry = RMIManager.getRegistry(host, port);
		registry.rebind("activatable", stub);
		System.err.println("Stub bound in registry."
				+ Arrays.toString(registry.list()));

	}


}
