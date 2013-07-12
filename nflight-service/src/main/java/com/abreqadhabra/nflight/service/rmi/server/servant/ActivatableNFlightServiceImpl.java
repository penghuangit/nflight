package com.abreqadhabra.nflight.service.rmi.server.servant;

import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationID;

import com.abreqadhabra.nflight.service.core.NFlightService;

public class ActivatableNFlightServiceImpl extends Activatable implements Remote, NFlightService {
	
    /**
     * Constructs an <code>ExtendsActivatable</code> instance with
     * the specified activation ID and data.  This constructor is
     * called during activation to construct the object.
     **/
    public ActivatableNFlightServiceImpl(ActivationID id, MarshalledObject<?> data) 
	throws RemoteException
    {
	/*
	 * Export the activatable object on an anonymous port.
	 */
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
