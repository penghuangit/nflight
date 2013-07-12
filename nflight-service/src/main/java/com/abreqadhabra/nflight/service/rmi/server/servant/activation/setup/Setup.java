package com.abreqadhabra.nflight.service.rmi.server.servant.activation.setup;

import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupDesc;
import java.rmi.activation.ActivationGroupID;
import java.util.Properties;

import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.service.core.Env;

public class Setup {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		
		
		

	}
	
	
	private static Properties getActivationProperties() {
		Properties _props = null;

		try {
			_props = PropertyFile
					.readPropertyFile(Env.RMI.FILE_ACTIVATION_CONFIG_DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return _props;
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
