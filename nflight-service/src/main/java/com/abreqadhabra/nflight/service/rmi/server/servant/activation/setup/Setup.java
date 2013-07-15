package com.abreqadhabra.nflight.service.rmi.server.servant.activation.setup;

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

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.common.util.PropertyLoader;
import com.abreqadhabra.nflight.service.core.boot.Profile;
import com.abreqadhabra.nflight.service.rmi.server.RMIManager;
import com.abreqadhabra.nflight.service.rmi.server.servant.ActivatableNFlightServiceImpl;

public class Setup {
	private static final Class<Setup> THIS_CLAZZ = Setup.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	private static final String BASE_LOCATION = THIS_CLAZZ
			.getProtectionDomain().getCodeSource().getLocation().getFile();

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		System.err.println("BASE_LOCATION: " + BASE_LOCATION);

		
		Properties _props = PropertyFile
				.readPropertyFile(Profile.FILE_ACTIVATION_PROPERTIES);
		
		PropertyLoader.setSystemProperties(_props);
		
		String policy = System.getProperty(Profile.PROPERTIES_ACTIVATION.NFLIGHT_SERVANT_ACTIVATION_POLICY.toString());
		System.setProperty(Profile.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString(), BASE_LOCATION + policy);
		
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		String filename = BASE_LOCATION + System.getProperty(Profile.PROPERTIES_ACTIVATION.NFLIGHT_SERVANT_ACTIVATION_FILE.toString());
		String implClass = System.getProperty(Profile.PROPERTIES_ACTIVATION.NFLIGHT_SERVANT_ACTIVATION_IMPL_CLASS.toString());
		String implCodebase = Profile.ACTIVATION_FILE_PREFIX + BASE_LOCATION;

		
		Properties props = new Properties();
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
				+ ActivatableNFlightServiceImpl.class.getSimpleName();

		Registry registry = RMIManager.getRegistry(host, port);
		registry.rebind(name, stub);
		System.err.println("Stub bound in registry." + Arrays.toString(registry.list()));
		
		
		
	}
}
