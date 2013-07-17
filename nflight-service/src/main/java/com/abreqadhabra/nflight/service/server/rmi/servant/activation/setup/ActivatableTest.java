package com.abreqadhabra.nflight.service.server.rmi.servant.activation.setup;


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
import com.abreqadhabra.nflight.common.util.IOStream;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.common.util.PropertyLoader;
import com.abreqadhabra.nflight.service.boot.Profile;
import com.abreqadhabra.nflight.service.server.NFService;
import com.abreqadhabra.nflight.service.server.rmi.RMIManager;
import com.abreqadhabra.nflight.service.server.rmi.servant.NFServiceRMIActivatableImpl;

public class ActivatableTest {
	private static final Class<ActivatableTest> THIS_CLAZZ = ActivatableTest.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	/**
	 * java -Djava.security.policy=<i><b>setup.policy</b></i> \
	 * -Djava.server.rmi..codebase=<i><b>codebase</b></i> \
	 * -Dexamples.activation.setup.codebase=<i><b>setupCodebase</b></i> \
	 * -Dexamples.activation.impl.codebase=<i><b>implCodebase</b></i> \
	 * -Dexamples.activation.name=<i><b>name</b></i> \
	 * [-Dexamples.activation.file=<i><b>filename</b></i>] \
	 * [-Dexamples.activation.policy=<i><b>group.policy</b></i>] \
	 * examples.activation.Setup <i><b>implClass</b></i>
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		String codeBase = IOStream.getCodebase(ActivatableTest.class.getName());

		System.err.println("BASE_LOCATION: " + codeBase);

		Properties _props = PropertyFile
				.readPropertyFile(Profile.FILE_ACTIVATION_PROPERTIES);
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

		String name = "rmi://" + host + ":" + port + "/"
				+ NFServiceRMIActivatableImpl.class.getSimpleName();

		Registry registry = RMIManager.getRegistry(host, port);
		registry.rebind(name, stub);
		System.err.println("Stub bound in registry."
				+ Arrays.toString(registry.list()));

		NFService service = (NFService) registry.lookup("rmi://"
				+ host + ":" + port + "/ActivatableNFlightServiceImpl");
		String response = service.sayHello();
		System.out.println("ActivatableNFlightServiceImpl response: "
				+ response);

	}
}
