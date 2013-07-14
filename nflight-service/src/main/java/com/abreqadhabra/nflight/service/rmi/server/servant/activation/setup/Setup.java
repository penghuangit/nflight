package com.abreqadhabra.nflight.service.rmi.server.servant.activation.setup;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupDesc;
import java.rmi.activation.ActivationGroupID;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.common.util.PropertyLoader;
import com.abreqadhabra.nflight.service.core.Env;
import com.abreqadhabra.nflight.service.core.boot.BootProfile;
import com.abreqadhabra.nflight.service.rmi.server.RMIManager;

public class Setup {
	private static final Class<Setup> THIS_CLAZZ = Setup.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	private static final String BASE_LOCATION = THIS_CLAZZ
			.getProtectionDomain().getCodeSource().getLocation().getFile();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		BootProfile.setSecurityManager();
	
		setSystemProperties();
		
		//rmid -J-Djava.security.policy=policy
		
		String host = null;
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int port = 9999;
		try {
			Registry registry =RMIManager.getRegistry(host, port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String policy = BASE_LOCATION + System.getProperty(Env.Properties.Setup.PropertyKey.NFLIGHT_SERVICE_RMI_SERVER_SERVANT_ACTIVATION_POLICY.toString());
	 	Properties props = new Properties();
        props.put("java.security.policy", policy);

     // Create a new activation group descriptor
		ActivationGroupDesc.CommandEnvironment ace = null;
		ActivationGroupDesc groupDesc = new ActivationGroupDesc(props, ace);
		
		ActivationGroupID groupID = registerGroup(groupDesc);
		

		
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"groupID : " + groupID);
		
		String filename =System.getProperty(Env.Properties.Setup.PropertyKey.NFLIGHT_SERVICE_RMI_SERVER_SERVANT_ACTIVATION_FILE.toString());

		MarshalledObject data = null;
		if (filename != null && !filename.equals("")) {
		    try {
				data = new MarshalledObject(filename);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"data : " + data.toString());

		String implCodebase = System
				.getProperty(Env.Properties.Setup.PropertyKey.NFLIGHT_SERVICE_RMI_SERVER_SERVANT_ACTIVATION_IMPL_CODEBASE.toString());
		
		String implClass =
				System
				.getProperty(Env.Properties.Setup.PropertyKey.NFLIGHT_SERVICE_RMI_SERVER_SERVANT_ACTIVATION_IMPL_CLASS.toString());
		


		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				implClass+","+implCodebase+","+policy+","+data);
		
		
		Remote obj = install(implClass, implCodebase, policy, data);
		
	}
	
	private static ActivationGroupID registerGroup(ActivationGroupDesc groupDesc) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"groupDesc.toString() : " + groupDesc.toString());
		/*
		 * Register the activation group descriptor with the activation system
		 * to obtain a group ID.
		 */
		ActivationGroupID groupID = null;
		try {
			ActivationGroupID gid = ActivationGroup.getSystem().registerGroup(
					groupDesc);
		} catch (RemoteException | ActivationException e) {
			e.printStackTrace();
		}
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Activation group descriptor registered.");

		return groupID;
	}

	private static void setSystemProperties() {
		Properties _props = null;

		try {
			_props = PropertyFile
					.readPropertyFile(Env.Properties.Setup.Constants.FILE_NAME_SETUP_PROPERTIES);
		} catch (Exception e) {
			e.printStackTrace();
		}

		PropertyLoader.setSystemProperties(_props);
	}

	// @param classname
	// The name of the class that implements
	// the remote interface(s) and extends the
	// java.rmi.activation.Activatable class
	// @param codebase
	// A URL from where the class definition
	// will come when this object is requested
	// (activated). Important: don't forget
	// the trailing slash at the end of the
	// URL or your classes won't be found.
	// @param policyfile
	// Because of the Java 2 security model, a
	// security policy should be specified for
	// the ActivationGroup Virtual Machine that will be
	// started by rmid.
	// @return
	// The stub or null
	static public Remote install(String classname, String codebase,
			String policyfile, MarshalledObject data) {
		try {
			Properties props = new Properties();
			props.put("java.security.policy", policyfile);

			// Create a new activation group descriptor
			ActivationGroupDesc.CommandEnvironment ace = null;
			ActivationGroupDesc group = new ActivationGroupDesc(props, ace);

			// Once the ActivationGroupDesc has been created,
			// register it with the activation system to
			// obtain its ID
			ActivationGroupID gid = ActivationGroup.getSystem().registerGroup(
					group);

			// Now we need to explicitly create the group
			ActivationGroup.createGroup(gid, group, 0);

			// Register the rmiobject with rmid
			// Auto restart
			ActivationDesc desc = new ActivationDesc(gid, classname, codebase,
					data, true);

			// return a stub.
			return Activatable.register(desc);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
