package com.abreqadhabra.nflight.service.rmi.server.activatable;

import java.net.InetAddress;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupDesc;
import java.rmi.activation.ActivationGroupID;
import java.rmi.activation.ActivationSystem;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.common.util.PropertyLoader;
import com.abreqadhabra.nflight.service.core.Profile;
import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.core.rmi.RegistryManager;
import com.abreqadhabra.nflight.service.core.server.IService;
import com.abreqadhabra.nflight.service.exception.NFRemoteException;
import com.abreqadhabra.nflight.service.rmi.server.AbstractRMIServer;

public class ActivatableRMIServerImpl extends AbstractRMIServer {

	private static final Class<ActivatableRMIServerImpl> THIS_CLAZZ = ActivatableRMIServerImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public ActivatableRMIServerImpl(ProfileImpl profile, IService service)
			throws Exception {
		super(profile, service);
	}

	@Override
	public void startup() throws Exception {
		if (RegistryManager
				.isActivatedRegistry(super.registry, super.boundName)) {
			throw new NFRemoteException(boundName + "가 레지스트리에 이미 등록되어 있습니다.");
		} else {
			Properties _props = PropertyFile.readPropertyFilePath(
					THIS_CLAZZ.getName(), Profile.FILE_ACTIVATION_PROPERTIES);
			String codeBase = super.profile.getCodeBase();
			_props.put(
					Profile.PROPERTIES_ACTIVATION.NFLIGHT_SERVANT_ACTIVATION_IMPL_CODEBASE
							.toString(), Profile.ACTIVATION_FILE_PREFIX
							+ codeBase);

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

			Registry registry = RegistryManager.getRegistry(host, port);
			registry.rebind(RegistryManager.getBoundName(super.host,
					super.port, "activatable"), stub);
			System.err.println("Stub bound in registry."
					+ Arrays.toString(registry.list()));
		}
	}
}