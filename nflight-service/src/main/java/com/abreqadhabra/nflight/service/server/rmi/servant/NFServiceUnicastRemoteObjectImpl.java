package com.abreqadhabra.nflight.service.server.rmi.servant;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.server.NFService;

public class NFServiceUnicastRemoteObjectImpl extends UnicastRemoteObject implements NFService {

	public NFServiceUnicastRemoteObjectImpl() throws RemoteException {
		super();
	}

	private static final Class<NFServiceUnicastRemoteObjectImpl> THIS_CLAZZ = NFServiceUnicastRemoteObjectImpl.class;
	private Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private static final long serialVersionUID = 1L;

/*	public UnicastRemoteObjectNFlightServiceImpl(int port,
			RMIClientSocketFactory csf, RMIServerSocketFactory ssf)
			throws RemoteException {
		super(port, csf, ssf);
	}*/

	public static String getObjName() {
		return THIS_CLAZZ.getSimpleName();
	}
	
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
	public boolean isRunning() throws RemoteException {
		return true;
	}
}
