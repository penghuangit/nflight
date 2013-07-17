package com.abreqadhabra.nflight.service.server.rmi.servant;


import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationID;

import com.abreqadhabra.nflight.service.server.NFService;

public class NFServiceRMIActivatableImpl extends Activatable implements
		NFService {

	public NFServiceRMIActivatableImpl(ActivationID id, MarshalledObject data)
			throws RemoteException {
		// Register the object with the activation
		// system then export it on an
		// anonymous port
		super(id, 0);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public String sayHello() {
		return "Hello, world!";
	}

	/**
	 * <p>
	 * [機　能] データサーバの生死を確認する。
	 * </p>
	 * <p>
	 * [説　明] データサーバの生死を確認する。
	 * </p>
	 * <p>
	 * [備　考] なし
	 * </p>
	 * 
	 * @return boolean データサーバが生きているならば、true
	 * @throws RemoteException
	 */
	@Override
	public boolean isRunning() {
		return true;
	}

}
