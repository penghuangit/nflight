package com.abreqadhabra.nflight.server.rmi.remote;

import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;

import com.abreqadhabra.nflight.server.NFlightService;

public class ActivatableNFlightServiceImpl extends Activatable implements Remote, NFlightService {
	
	protected ActivatableNFlightServiceImpl(String location, MarshalledObject<?> data,
			boolean restart, int port, RMIClientSocketFactory csf,
			RMIServerSocketFactory ssf) throws ActivationException,
			RemoteException {
		super(location, data, restart, port, csf, ssf);

	}

	private static final long serialVersionUID = 1L;

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
