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
public class ActivatableRMIServiceImpl extends AbstractRMIService {

	private static final Class<ActivatableRMIServiceImpl> THIS_CLAZZ = ActivatableRMIServiceImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

//	public ActivatableRMIServiceImpl(){}
	public ActivatableRMIServiceImpl(ServiceDescriptor _desc) throws Exception {
		super(_desc);
	}

	public ActivatableRMIServiceImpl(ActivationID id, MarshalledObject<?> data)
			throws RemoteException {

		// Register the object with the activation system
		// then export it on an anonymous port
		//
		Activatable.exportObject(this, id, 0);
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Instantiating a " + THIS_CLAZZ.getSimpleName() + " Class ");
	}

		public ActivatableRMIServiceImpl() {
		// TODO Auto-generated constructor stub
	}

		@Override
	public void startup() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Strategy -> ConcreteStrategy -> BehaviourInterface() : "
						+ METHOD_NAME);

		if (RMIServiceHelper.isActivatedRegistry(super.registry,
				super.boundName)) {
			throw new NFRemoteException(boundName + "가 레지스트리에 이미 등록되어 있습니다.");
		} else {
			Properties _props = PropertyFile.readPropertyFilePath(
					THIS_CLAZZ.getName(), Profile.FILE_ACTIVATION_PROPERTIES);

			String codeBase = super.desc.getCodeBase();

			_props.put(
					Profile.PROPERTIES_ACTIVATION.NFLIGHT_SERVANT_ACTIVATION_IMPL_CODEBASE
							.toString(), Profile.ACTIVATION_FILE_PREFIX
							+ codeBase);

			PropertyLoader.setSystemProperties(_props);

			String securityPolicy = codeBase + Profile.FILE_ACTIVATION_POLICY;

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
			
			
			System.setProperty(
					"java.rmi.server.codebase", Profile.ACTIVATION_FILE_PREFIX + codeBase +
					" file:/D:/dskim/workspace/nflight/nflight-common/target/nflight-common/0.0.1-SNAPSHOT/classes");
	//		implCodebase =Profile.ACTIVATION_FILE_PREFIX + codeBase + "  file:/D:/dskim/workspace/nflight/nflight-common/target/nflight-common/0.0.1-SNAPSHOT/classes";

			//implCodebase =System.getProperty("java.class.path");
			
			 //System.out.println(System.getProperty("java.class.path"));
			//	implClass ="com.abreqadhabra.nflight.app.server.service.rmi.ActivatableRMIService";

			Properties props = new Properties();
			props.put("java.security.policy", groupPolicy);
	//	props.put("java.class.path",System.getProperty("java.class.path"));
		//	props.put("nflight.servant.activation.impl.codebase", implCodebase);

			ActivationGroupDesc groupDesc = new ActivationGroupDesc(props, null);

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,"groupDesc : " + groupDesc);

			ActivationSystem system = ActivationGroup.getSystem();

			ActivationGroupID groupID = system.registerGroup(groupDesc);

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,"Activation group descriptor registered.");

			// Pass the file that we want to persist to as the Marshalled
			// object
			MarshalledObject<?> data = null;
			if (filename != null && !filename.equals("")) {
				data = new MarshalledObject(filename);
			}

			ActivationDesc desc = new ActivationDesc(groupID, implClass,
					implCodebase, data);

//			desc = new ActivationDesc 
//				    (groupID, "com.abreqadhabra.nflight.app.server.service.rmi.ActivatableRMIServiceImpl", 
//				      "file:/D:/dskim/workspace/nflight/nflight-service/target/nflight-service/0.0.1-SNAPSHOT/classes/", null);
			
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"groupID:" + groupID + "\nimplClass : " + implClass
							+ "\nimplCodebase : " + implCodebase +"\ndata : " + data);

			
			Remote stub = Activatable.register(desc);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,"Activation descriptor registered : " + stub);

			String host = InetAddress.getLocalHost().getHostAddress();
			int port = 9999;

			Registry registry = RMIServiceHelper.getRegistry(host, port);
			registry.rebind(RMIServiceHelper.getBoundName(super.host,
					super.port, "activatable"), stub);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,"Stub bound in registry."
					+ Arrays.toString(registry.list()));
		}

	}
}
