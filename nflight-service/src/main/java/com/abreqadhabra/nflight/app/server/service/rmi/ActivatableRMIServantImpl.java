package com.abreqadhabra.nflight.app.server.service.rmi;

import java.net.InetAddress;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupDesc;
import java.rmi.activation.ActivationGroupID;
import java.rmi.activation.ActivationID;
import java.rmi.activation.ActivationSystem;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.app.core.Profile;
import com.abreqadhabra.nflight.app.core.exception.NFRemoteException;
import com.abreqadhabra.nflight.app.core.rmi.RMIServiceHelper;
import com.abreqadhabra.nflight.app.server.service.ServiceDescriptor;
import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.common.util.PropertyLoader;

//Strategy ConcreteStrategy
public class ActivatableRMIServantImpl extends AbstractRMIServant {

	private static final Class<ActivatableRMIServantImpl> THIS_CLAZZ = ActivatableRMIServantImpl.class;
	private static Logger LOGGER = LoggingHelper
			.getLogger(ActivatableRMIServantImpl.THIS_CLAZZ);

	public ActivatableRMIServantImpl() {
		// TODO Auto-generated constructor stub
	}

	public ActivatableRMIServantImpl(final ActivationID id,
			final MarshalledObject<?> data) throws RemoteException {

		// Register the object with the activation system
		// then export it on an anonymous port
		//
		Activatable.exportObject(this, id, 0);
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		ActivatableRMIServantImpl.LOGGER.logp(
				Level.FINER,
				ActivatableRMIServantImpl.THIS_CLAZZ.getName(),
				METHOD_NAME,
				"Instantiating a "
						+ ActivatableRMIServantImpl.THIS_CLAZZ.getSimpleName()
						+ " Class ");
	}

	// public ActivatableRMIServiceImpl(){}
	public ActivatableRMIServantImpl(final ServiceDescriptor _desc)
			throws Exception {
		super(_desc);
	}

	@Override
	public void startup() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		ActivatableRMIServantImpl.LOGGER.logp(Level.FINER,
				ActivatableRMIServantImpl.THIS_CLAZZ.getName(), METHOD_NAME,
				"Strategy -> ConcreteStrategy -> BehaviourInterface() : "
						+ METHOD_NAME);

		if (RMIServiceHelper.isActivatedRegistry(super.registry,
				super.boundName)) {
			throw new NFRemoteException(this.boundName
					+ "가 레지스트리에 이미 등록되어 있습니다.");
		} else {
			final Properties _props = PropertyFile.readPropertyFilePath(
					ActivatableRMIServantImpl.THIS_CLAZZ.getName(),
					Profile.FILE_ACTIVATION_PROPERTIES);

			final String codeBase = super.desc.getCodeBase();

			_props.put(
					Profile.PROPERTIES_ACTIVATION.NFLIGHT_SERVANT_ACTIVATION_IMPL_CODEBASE
							.toString(), Profile.ACTIVATION_FILE_PREFIX
							+ codeBase);

			PropertyLoader.setSystemProperties(_props);

			final String securityPolicy = codeBase
					+ Profile.FILE_ACTIVATION_POLICY;

			System.setProperty(
					Env.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString(),
					securityPolicy);

			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
			}

			String implCodebase = System
					.getProperty(Profile.PROPERTIES_ACTIVATION.NFLIGHT_SERVANT_ACTIVATION_IMPL_CODEBASE
							.toString());
			final String filename = implCodebase
					+ System.getProperty(Profile.PROPERTIES_ACTIVATION.NFLIGHT_SERVANT_ACTIVATION_FILE
							.toString());
			final String groupPolicy = implCodebase
					+ System.getProperty(Profile.PROPERTIES_ACTIVATION.NFLIGHT_SERVANT_ACTIVATION_POLICY
							.toString());
			final String implClass = System
					.getProperty(Profile.PROPERTIES_ACTIVATION.NFLIGHT_SERVANT_ACTIVATION_IMPL_CLASS
							.toString());

			System.out.println(System.getProperty("java.rmi.server.codebase"));

			System.out.println(System
					.getProperty("java.rmi.server.useCodebaseOnly"));

			System.setProperty(
					"java.rmi.server.codebase",
					Profile.ACTIVATION_FILE_PREFIX
							+ codeBase
							+ " file:///D:/workspace/nflight/nflight-common/target/nflight-common/0.0.1-SNAPSHOT/classes/");

			// System.out.println(System.getProperty("java.rmi.server.codebase"));

			// implCodebase =Profile.ACTIVATION_FILE_PREFIX + codeBase ;

			implCodebase = System.getProperty("java.rmi.server.codebase");
			// implCodebase
			// ="file:///D:/workspace/nflight/nflight-service/target/nflight-service/0.0.1-SNAPSHOT/classes/ file:///D:/workspace/nflight/nflight-common/target/nflight-common/0.0.1-SNAPSHOT/classes/";

			// System.out.println(System.getProperty("java.class.path"));
			// implClass
			// ="com.abreqadhabra.nflight.app.server.service.rmi.ActivatableRMIService";

			final Properties props = new Properties();
			props.put("java.security.policy", groupPolicy);
			// props.put("java.class.path",System.getProperty("java.class.path"));
			// props.put("nflight.servant.activation.impl.codebase",
			// implCodebase);

			final ActivationGroupDesc groupDesc = new ActivationGroupDesc(
					props, null);

			ActivatableRMIServantImpl.LOGGER.logp(Level.FINER,
					ActivatableRMIServantImpl.THIS_CLAZZ.getName(),
					METHOD_NAME, "groupDesc : " + groupDesc);

			final ActivationSystem system = ActivationGroup.getSystem();

			final ActivationGroupID groupID = system.registerGroup(groupDesc);

			ActivatableRMIServantImpl.LOGGER.logp(Level.FINER,
					ActivatableRMIServantImpl.THIS_CLAZZ.getName(),
					METHOD_NAME, "Activation group descriptor registered.");

			// Pass the file that we want to persist to as the Marshalled
			// object
			MarshalledObject<?> data = null;
			if (filename != null && !filename.equals("")) {
				data = new MarshalledObject(filename);
			}

			final ActivationDesc desc = new ActivationDesc(groupID, implClass,
					implCodebase, data);

			// desc = new ActivationDesc
			// (groupID,
			// "com.abreqadhabra.nflight.app.server.service.rmi.ActivatableRMIServiceImpl",
			// "file:/D:/dskim/workspace/nflight/nflight-service/target/nflight-service/0.0.1-SNAPSHOT/classes/",
			// null);

			ActivatableRMIServantImpl.LOGGER.logp(Level.FINER,
					ActivatableRMIServantImpl.THIS_CLAZZ.getName(),
					METHOD_NAME, "groupID:" + groupID + "\nimplClass : "
							+ implClass + "\nimplCodebase : " + implCodebase
							+ "\ndata : " + data);

			final Remote stub = Activatable.register(desc);
			ActivatableRMIServantImpl.LOGGER.logp(Level.FINER,
					ActivatableRMIServantImpl.THIS_CLAZZ.getName(),
					METHOD_NAME, "Activation descriptor registered : " + stub);

			final String host = InetAddress.getLocalHost().getHostAddress();
			final int port = 9999;

			final Registry registry = RMIServiceHelper.getRegistry(host, port);
			registry.rebind(RMIServiceHelper.getBoundName(super.host,
					super.port, "activatable"), stub);
			ActivatableRMIServantImpl.LOGGER.logp(
					Level.FINER,
					ActivatableRMIServantImpl.THIS_CLAZZ.getName(),
					METHOD_NAME,
					"Stub bound in registry."
							+ Arrays.toString(registry.list()));
		}

	}
}
