package com.abreqadhabra.nflight.server.rmi.activation;

import java.io.IOException;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupDesc;
import java.rmi.activation.ActivationGroupID;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.Constants;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.common.util.PropertyLoader;

public class Setup {
	private static final Class<Setup> THIS_CLAZZ = Setup.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public void Setup(){
		init();
	}
	
	public static void main(String[] args) {

	init();	
	}
		
		private static void init() {

		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		/*
		 * Construct an activation group descriptor.
		 * 
		 * First, create a properties map containing overrides for system
		 * properties in the activation group VM. Add a value for the
		 * "java.security.policy" system property to the properties map. The
		 * value for the security policy file is specified by the
		 * "examples.activation.policy" system property which defaults to the
		 * file named "group.policy" in the working directory.
		 * 
		 * Also add the value for the "examples.activation.impl.codebase" system
		 * property to the map, which is specified by the
		 * "examples.activation.impl.codebase" system property. This property is
		 * used in the group's policy file.
		 */

		Properties _props = getActivationProperties();

		/*
		 * java.security.policy=setup.policy java.rmi.server.codebase=codebase
		 * nflight.system.rmi.activation.setup.codebase=setupCodebase
		 * nflight.system.rmi.activation.impl.codebase=implCodebase
		 * nflight.system.rmi.activation.name=name
		 * nflight.system.rmi.activation.file=filename
		 * nflight.system.rmi.activation.policy=group.policy
		 */
		
		
		String classpath = System.getProperty("java.class.path");
		_props.put("java.class.path", "no_classpath");

		PropertyLoader.setSystemProperties(_props);

		String implClass = "com.abreqadhabra.nflight.server.rmi.remote.ActivatableNFlightServiceImpl";
		String policy = System.getProperty(
				Constants.RMI.KEY_RMI_ACTIVATION_POLICY, "group.policy");
		String implCodebase = System
				.getProperty(Constants.RMI.KEY_RMI_ACTIVATION_IMPL_CODEBASE);
		String filename = System.getProperty(
				Constants.RMI.KEY_RMI_ACTIVATION_FILE, "");
		String name = System.getProperty("examples.activation.name");

		ActivationGroupID groupID = registerActivationGroup(_props);

		Remote obj = getActivatableObject(groupID, implClass, implCodebase,
				filename);

		bindActivatableObject(name, obj);
	}

	private static void bindActivatableObject(String name, Remote obj) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		/*
		 * Bind the activatable object's stub to a name in the registry running
		 * on port 1099. The name is specified by the system property
		 * "examples.activation.name".
		 */

		try {
			LocateRegistry.getRegistry().rebind(name, obj);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Stub bound in registry.");
	}

	private static Remote getActivatableObject(ActivationGroupID groupID,
			String implClass, String implCodebase, String filename) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		/*
		 * Construct an activation descriptor for the activatable object.
		 * 
		 * The location specifies a URL from where the implementation class can
		 * be loaded when this object is activated. The value of the URL is
		 * specified by the "examples.activation.impl.codebase" system property.
		 * 
		 * The data (optional) specifies initialization data passed to the
		 * remote object when being constructed as part of the activation
		 * process. Here, the value of "data" is specified by the
		 * "examples.activation.filename" system property and represents the
		 * name of a file containing the object's persistent state (to be read
		 * in when the object is activated).
		 */
		MarshalledObject data = null;
		if (filename != null && !filename.equals("")) {
			try {
				data = new MarshalledObject(filename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		ActivationDesc desc = new ActivationDesc(groupID, implClass,
				implCodebase, data);

		/*
		 * Register the activation descriptor with the activation system. Use
		 * the convenience method 'Activatable.register' which returns a
		 * constructed stub for the activatable object after registering the
		 * activation descriptor.
		 */
		Remote stub = null;
		try {
			stub = Activatable.register(desc);
		} catch (RemoteException | ActivationException e) {
			e.printStackTrace();
		}
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Activation descriptor registered.");

		return stub;
	}

	private static Properties getActivationProperties() {
		Properties _props = null;

		/*
		 * Properties props = new Properties();
		 * props.put("java.security.policy", policy);
		 * props.put("java.class.path", "no_classpath");
		 * props.put("examples.activation.impl.codebase", implCodebase); if
		 * (filename != null && !filename.equals("")) {
		 * props.put("examples.activation.file", filename); }
		 */

		try {
			_props = PropertyFile
					.readPropertyFile(Constants.RMI.DEFAULT_CONFIG_FILE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return _props;
	}

	private static ActivationGroupID registerActivationGroup(Properties props) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		ActivationGroupDesc groupDesc = new ActivationGroupDesc(props, null);
		
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"groupDesc.toString() : " + groupDesc.toString());
		/*
		 * Register the activation group descriptor with the activation system
		 * to obtain a group ID.
		 */
		ActivationGroupID groupID = null;
		try {
			groupID = ActivationGroup.getSystem().registerGroup(groupDesc);
		} catch (RemoteException | ActivationException e) {
			e.printStackTrace();
		}
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Activation group descriptor registered.");

		return groupID;
	}

}
