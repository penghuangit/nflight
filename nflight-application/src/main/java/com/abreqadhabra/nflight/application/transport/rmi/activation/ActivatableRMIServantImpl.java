package com.abreqadhabra.nflight.application.transport.rmi.activation;

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
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.transport.rmi.AbstractRMIServant;
import com.abreqadhabra.nflight.application.transport.rmi.RMIServiceHelper;
import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ActivatableRMIServantImpl extends AbstractRMIServant {
	private static Class<ActivatableRMIServantImpl> THIS_CLAZZ = ActivatableRMIServantImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public ActivatableRMIServantImpl() {

	}

	public ActivatableRMIServantImpl(ActivationID id,
			MarshalledObject<?> data) throws RemoteException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		// Register the object with the activation system
		// then export it on an anonymous port
		Remote stub = Activatable.exportObject(this, id, 0);
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"stub for the activatable remote object: " + stub.toString());
	}

	public ActivatableRMIServantImpl(Configure configure,
			InetAddress addr, int port) throws Exception {
		super(configure, addr, port);
		this.serviceName = Configure.RMI_SERVICE_TYPE.activatable.toString();
		this.boundName = RMIServiceHelper.getBoundName(
				super.addr.getHostAddress(), super.port, this.serviceName);
	}

	@Override
	public void run() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			String className = THIS_CLAZZ.getName();
			String codebase = Configure.PREFIX_FILE_ACTIVATABLE
					+ Configure.CODE_BASE_PATH.toString();
			String policyfile = Configure.FILE_ACTIVATABLE_POLICY
					.toString();
			MarshalledObject<Object> data = new MarshalledObject<Object>(
					Configure.FILE_ACTIVATION.toString());

			// 삭제예정 start 실행 초기에 보안관리자 설정 필요
			System.setProperty(
					Env.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString(),
					policyfile);

			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
			}
			// 삭제예정 end

			Remote stub = this.install(className, codebase, policyfile,
					data);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Activation descriptor registered : " + stub);

			this.registry.rebind(super.boundName, stub);

			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getName(),
					METHOD_NAME,
					stub + " Stub bound in registry."
							+ Arrays.toString(this.registry.list()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Remote install(String className, String codebase,
			String policyFile, MarshalledObject<Object> data) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			Properties props = new Properties();
			props.put("java.security.policy", policyFile);

			// Create a new activation group descriptor
			ActivationGroupDesc groupDesc = new ActivationGroupDesc(
					props, null);

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"groupDesc : " + groupDesc);

			// Once the ActivationGroupDesc has been created,
			// register it with the activation system to
			// obtain its ID
			ActivationGroupID groupID = ActivationGroup.getSystem()
					.registerGroup(groupDesc);

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Activation group descriptor registered.");

			// Now we need to explicitly create the group
			ActivationGroup.createGroup(groupID, groupDesc, 0);

			// Register the rmi object with rmid Auto restart
			ActivationDesc desc = new ActivationDesc(groupID, className,
					codebase, data, true);

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"groupID:" + groupID + "\nclassName : " + className
							+ "\ncodebase : " + codebase + "\ndata : " + data);

			// return a stub.
			return Activatable.register(desc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// The ActivatableShutdown interface implementation
//	public boolean uninstall() throws RemoteException {
//		return deactivate(true);
//	}
//	
//	protected boolean deactivate(boolean forever) {
//		try {
//			if (forever) {
//				Activatable.unregister(this.getID());
//				return true;
//			} else {
//				return Activatable.inactive(this.getID());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}

}
