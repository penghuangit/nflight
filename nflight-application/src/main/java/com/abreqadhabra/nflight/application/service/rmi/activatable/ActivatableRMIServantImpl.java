package com.abreqadhabra.nflight.application.service.rmi.activatable;

import java.net.InetAddress;
import java.net.UnknownHostException;
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

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.rmi.AbstractRMIServant;
import com.abreqadhabra.nflight.application.service.rmi.RMIServiceException;
import com.abreqadhabra.nflight.application.service.rmi.RMIServiceHelper;
import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyLoader;

public class ActivatableRMIServantImpl extends AbstractRMIServant {
	private static final Class<ActivatableRMIServantImpl> THIS_CLAZZ = ActivatableRMIServantImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public ActivatableRMIServantImpl() {

	}

	public ActivatableRMIServantImpl(final ActivationID id,
			final MarshalledObject<?> data) throws RemoteException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// Register the object with the activation system
		// then export it on an anonymous port
		Remote stub = Activatable.exportObject(this, id, 0);

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"stub for the activatable remote object: " + stub.toString());
	}

	public ActivatableRMIServantImpl(final Configure configure,
			final InetAddress addr, final int port) throws Exception {
		super(configure, addr, port);
		this.boundName = RMIServiceHelper.getBoundName(
				this.addr.getHostAddress(), this.port,
				Configure.RMI_SERVICE_TYPE.activatable.toString());
	}

	@Override
	public void run() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
		String classname = THIS_CLAZZ.getName();
		String codebase = Configure.CODE_BASE_PATH.toString();
		String policyfile = Configure.FILE_ACTIVATABLE_POLICY.toString();
		MarshalledObject<Object> data = new MarshalledObject<Object>(Configure.FILE_ACTIVATION.toString());

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Activation descriptor registered : " + policyfile);

		
		//삭제예정 start 실행 초기에 보안관리자 설정 필요
		 System.setProperty(
		 Env.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString(),
		 policyfile);
		
		 if (System.getSecurityManager() == null) {
		 System.setSecurityManager(new SecurityManager());
		 }
		//삭제예정 end
		 
		final Remote stub = install(classname, codebase, policyfile, data);
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Activation descriptor registered : " + stub);

	
			final Registry registry = RMIServiceHelper.getRegistry(
					addr.getHostAddress(), port);
			registry.rebind(super.boundName, stub);

			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getName(),
					METHOD_NAME,
					stub + " Stub bound in registry."
							+ Arrays.toString(registry.list()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Remote install(String implClass, String implCodebase, String policyfile,
			MarshalledObject<Object> data) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		try {
			Properties props = new Properties();
			props.put("java.security.policy", policyfile);

			// Create a new activation group descriptor
			ActivationGroupDesc.CommandEnvironment ace = null;
			ActivationGroupDesc groupDesc = new ActivationGroupDesc(props, ace);

			
			LOGGER.logp(Level.FINER,
					THIS_CLAZZ.getName(), METHOD_NAME,
					"groupDesc : " + groupDesc);
			
			// Once the ActivationGroupDesc has been created,
			// register it with the activation system to
			// obtain its ID
			ActivationGroupID groupID = ActivationGroup.getSystem().registerGroup(
					groupDesc);

			LOGGER.logp(Level.FINER,
					THIS_CLAZZ.getName(), METHOD_NAME,
					"Activation group descriptor registered.");
			
			// Now we need to explicitly create the group
			ActivationGroup.createGroup(groupID, groupDesc, 0);

			// Register the rmiobject with rmid
			// Auto restart
			ActivationDesc desc = new ActivationDesc(groupID, implClass, implCodebase,
					data, true);

			LOGGER.logp(Level.FINER,
					THIS_CLAZZ.getName(), METHOD_NAME,
					"groupID:" + groupID + "\nimplClass : " + implClass
							+ "\nimplCodebase : " + implCodebase + "\ndata : "
							+ data);
			
			
//			FINER: groupID:java.rmi.activation.ActivationGroupID@71ec9704
//			implClass : com.abreqadhabra.nflight.app.server.service.rmi.ActivatableServantImpl
//			implCodebase : file:///D:/workspace/nflight/nflight-service/target/nflight-service/0.0.1-SNAPSHOT/classes/ file:///D:/workspace/nflight/nflight-common/target/nflight-common/0.0.1-SNAPSHOT/classes/
//			data : java.rmi.MarshalledObject@928a7e4d
			
			// return a stub.
			return Activatable.register(desc);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// @Override
	// public void run() {
	// final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
	// .getMethodName();
	// try {
	// if (RMIServiceHelper.isActivatedRegistry(super.registry,
	// super.boundName)) {
	// throw new RMIServiceException(this.boundName
	// + "가 레지스트리에 이미 등록되어 있습니다.");
	// } else {
	//
	// String implCodebaseKey =
	// Configure.PROPERTIES_ACTIVATION.NFLIGHT_SERVICE_RMI_ACTIVATABLE_IMPL_CODEBASE
	// .toString();
	//
	// configure.set(implCodebaseKey,
	// Configure.ACTIVATABLE_FILE_PREFIX
	// + Configure.CODE_BASE_PATH);
	//
	// PropertyLoader.setSystemProperties(configure.getProperties());
	//
	// System.setProperty(
	// Env.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString(),
	// Configure.FILE_ACTIVATABLE_POLICY.toString());
	//
	// if (System.getSecurityManager() == null) {
	// System.setSecurityManager(new SecurityManager());
	// }
	//
	// String implCodebase = System
	// .getProperty(Configure.PROPERTIES_ACTIVATION.NFLIGHT_SERVICE_RMI_ACTIVATABLE_IMPL_CODEBASE
	// .toString());
	// final String filename = implCodebase
	// +
	// System.getProperty(Configure.PROPERTIES_ACTIVATION.NFLIGHT_SERVICE_RMI_ACTIVATABLE_FILE
	// .toString());
	// final String groupPolicy = implCodebase
	// +
	// System.getProperty(Configure.PROPERTIES_ACTIVATION.NFLIGHT_SERVICE_RMI_ACTIVATABLE_POLICY
	// .toString());
	// final String implClass = System
	// .getProperty(Configure.PROPERTIES_ACTIVATION.NFLIGHT_SERVICE_RMI_ACTIVATABLE_IMPL_CLASS
	// .toString());
	//
	// System.out.println(System
	// .getProperty("java.rmi.server.codebase"));
	//
	// System.out.println(System
	// .getProperty("java.rmi.server.useCodebaseOnly"));
	//
	// System.setProperty(
	// "java.rmi.server.codebase",
	// Configure.ACTIVATABLE_FILE_PREFIX
	// + Configure.CODE_BASE_PATH
	// +
	// " file:///D:/workspace/nflight/nflight-common/target/nflight-common/0.0.1-SNAPSHOT/classes/");
	//
	// // System.out.println(System.getProperty("java.rmi.server.codebase"));
	//
	// // implCodebase =Profile.ACTIVATION_FILE_PREFIX + codeBase ;
	//
	// implCodebase = System.getProperty("java.rmi.server.codebase");
	// // implCodebase
	// //
	// ="file:///D:/workspace/nflight/nflight-service/target/nflight-service/0.0.1-SNAPSHOT/classes/ file:///D:/workspace/nflight/nflight-common/target/nflight-common/0.0.1-SNAPSHOT/classes/";
	//
	// // System.out.println(System.getProperty("java.class.path"));
	// // implClass
	// //
	// ="com.abreqadhabra.nflight.app.server.service.rmi.ActivatableRMIService";
	//
	// final Properties props = new Properties();
	// props.put("java.security.policy", groupPolicy);
	// // props.put("java.class.path",System.getProperty("java.class.path"));
	// // props.put("nflight.servant.activation.impl.codebase",
	// // implCodebase);
	//
	// final ActivationGroupDesc groupDesc = new ActivationGroupDesc(
	// props, null);
	//
	// LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
	// "groupDesc : " + groupDesc);
	//
	// final ActivationSystem system = ActivationGroup.getSystem();
	//
	// final ActivationGroupID groupID = system
	// .registerGroup(groupDesc);
	//
	// LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
	// "Activation group descriptor registered.");
	//
	// // Pass the file that we want to persist to as the Marshalled
	// // object
	// MarshalledObject<?> data = null;
	// if ((filename != null) && !filename.equals("")) {
	// data = new MarshalledObject(filename);
	// }
	//
	// final ActivationDesc desc = new ActivationDesc(groupID,
	// implClass, implCodebase, data);
	//
	// // desc = new ActivationDesc
	// // (groupID,
	// //
	// "com.abreqadhabra.nflight.app.server.service.rmi.ActivatableRMIServiceImpl",
	// //
	// "file:/D:/dskim/workspace/nflight/nflight-service/target/nflight-service/0.0.1-SNAPSHOT/classes/",
	// // null);
	//
	// LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
	// "groupID:" + groupID + "\nimplClass : " + implClass
	// + "\nimplCodebase : " + implCodebase
	// + "\ndata : " + data);
	//
	// final Remote stub = Activatable.register(desc);
	// LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
	// "Activation descriptor registered : " + stub);
	//
	// final String host = InetAddress.getLocalHost().getHostAddress();
	// final int port = 9999;
	//
	// final Registry registry = RMIServiceHelper.getRegistry(host,
	// port);
	// registry.rebind(super.boundName, stub);
	//
	// LOGGER.logp(
	// Level.FINER,
	// THIS_CLAZZ.getName(),
	// METHOD_NAME,
	// stub + " Stub bound in registry."
	// + Arrays.toString(registry.list()));
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

	@Override
	public String sayHello() throws RemoteException {
		return "Hello, world!";
	}

}
