package com.abreqadhabra.nflight.samples.rmi;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.rmi.MarshalledObject;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupDesc;
import java.rmi.activation.ActivationGroupID;
import java.rmi.activation.ActivationID;
import java.util.Properties;

public class ActivatableWrapper extends Activatable implements
		ActivatableUninstall, Runnable {
	private Thread t = null;
	protected long IDLE_TIME_IN_MINUTES = 1;

	public ActivatableWrapper(ActivationID id, MarshalledObject data)
			throws RemoteException {
		// Register the object with the activation
		// system then export it on an
		// anonymous port
		super(id, 0);
		restore(data);
		t = new Thread(this);
		t.start();
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

	// The ActivatableShutdown interface implementation
	public boolean uninstall() throws RemoteException {
		return deactivate(true);
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(IDLE_TIME_IN_MINUTES * 60000);
				boolean b = deactivate();
				if (b) {
					System.out.println("Deactivated object...");
					return;
				}
			} catch (Exception e) {
				System.out.println("Granted new life...");
			}
		}
	}

	protected void grantNewLife() {
		t.interrupt();
	}

	protected boolean deactivate() {
		return deactivate(false);
	}

	protected boolean deactivate(boolean forever) {
		try {
			if (forever) {
				Activatable.unregister(this.getID());
				return true;
			} else {
				return Activatable.inactive(this.getID());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private void restore(MarshalledObject data) {
		if (data == null)
			return;
		try {
			Object o = data.get();
			if (o instanceof java.lang.String) {
				String filename = (String) o;
				System.out
						.println("Recreating RMI Registry binding from	     file:"
								+ filename);
				File f = new File(filename);
				if (f.exists()) {
					ObjectInputStream ois = new ObjectInputStream(
							new FileInputStream(f));
					String objectname = (String) ois.readObject();
					Remote stub = (Remote) ois.readObject();
					Naming.rebind(objectname, stub);
				}
			} else {
				System.out.println("Error: Corrupt data...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}