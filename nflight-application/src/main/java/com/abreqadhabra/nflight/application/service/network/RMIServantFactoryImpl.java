package com.abreqadhabra.nflight.application.service.network;

import java.net.InetSocketAddress;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.Configure;
import com.abreqadhabra.nflight.application.common.launcher.Configure.SERVICE_TYPE;
import com.abreqadhabra.nflight.application.service.ServiceFactory;
import com.abreqadhabra.nflight.application.service.network.rmi.RMIServantException;
import com.abreqadhabra.nflight.application.service.network.rmi.impl.ActivatableRMIServantImpl;
import com.abreqadhabra.nflight.application.service.network.rmi.impl.UnicastRMIServantImpl;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class RMIServantFactoryImpl implements ServiceFactory {
	private static Class<RMIServantFactoryImpl> THIS_CLAZZ = RMIServantFactoryImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private SERVICE_TYPE serviceType;
	private Configure configure;
	private InetSocketAddress endpoint;

	public RMIServantFactoryImpl(SERVICE_TYPE serviceType, Configure configure,
			InetSocketAddress endpoint) {
		this.serviceType = serviceType;
		this.configure = configure;
		this.endpoint = endpoint;
	}

	@Override
	public Runnable createService() throws NFlightRemoteException {
		if (this.serviceType.equals(SERVICE_TYPE.rmi_unicast)) {
			return getUnicastRMIServant();
		} else if (this.serviceType.equals(SERVICE_TYPE.rmi_activation)) {
			return getActivatableRMIServant();
		} else {
			throw new RMIServantException("서비스 객체 생성에 실패하였습니다.");
		}
	}

	public Runnable getUnicastRMIServant() throws NFlightRemoteException {
		return new UnicastRMIServantImpl(this.configure, endpoint);
	}

	public Runnable getActivatableRMIServant() throws NFlightRemoteException {
		return new ActivatableRMIServantImpl(this.configure, endpoint);
	}

}
