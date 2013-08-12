package com.abreqadhabra.nflight.application.service.rmi.activatable;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.rmi.MarshalledObject;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationID;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.service.rmi.activatable.sample.ActivatableUninstall;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ActivatableWrapper extends Activatable
		implements
			ActivatableUninstall,
			Runnable {

	private static final Class<ActivatableWrapper> THIS_CLAZZ = ActivatableWrapper.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	private static final long serialVersionUID = 1L;

	Thread t = null;
	long IDLE_TIME_IN_MINUTES = 1;

	public ActivatableWrapper(final ActivationID id,
			final MarshalledObject<?> data) throws RemoteException {
		// Register the object with the activation
		// system then export it on an
		// anonymous port
		super(id, 0);
		this.restore(data);
		t = new Thread(this);
		t.start();
	}

	private void restore(final MarshalledObject<?> data) {
		if (data == null) {
			return;
		}
		try {
			final Object o = data.get();
			if (o instanceof java.lang.String) {
				final String filename = (String) o;
				System.out.println("Recreating RMI Registry binding from file:"
						+ filename);
				final File f = new File(filename);
				if (f.exists()) {
					final ObjectInputStream ois = new ObjectInputStream(
							new FileInputStream(f));
					final String objectname = (String) ois.readObject();
					final Remote stub = (Remote) ois.readObject();
					Naming.rebind(objectname, stub);
				}
			} else {
				System.out.println("Error: Corrupt data...");
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
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
	
	// The ActivatableShutdown interface implementation
	public boolean uninstall() throws RemoteException {
		return deactivate(true);
	}

	protected void grantNewLife() {
		t.interrupt();
	}

	public static void main(final String[] args) throws Exception {

		
	}
}
