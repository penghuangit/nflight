package com.abreqadhabra.nflight.application.service.network;

import java.net.InetSocketAddress;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.Config;
import com.abreqadhabra.nflight.application.service.conf.ServiceConfig.ENUM_SERVICE_TYPE;
import com.abreqadhabra.nflight.application.service.network.rmi.conf.RMIServantConfig;
import com.abreqadhabra.nflight.application.service.network.rmi.exception.RMIServantException;
import com.abreqadhabra.nflight.application.service.network.rmi.impl.ActivatableRMIServantImpl;
import com.abreqadhabra.nflight.application.service.network.rmi.impl.UnicastRMIServantImpl;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class RMINetworkServiceFactoryImpl extends NetworkServiceFactory {
	private static Class<RMINetworkServiceFactoryImpl> THIS_CLAZZ = RMINetworkServiceFactoryImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private ENUM_SERVICE_TYPE serviceType;

	public RMINetworkServiceFactoryImpl(ENUM_SERVICE_TYPE serviceType) {
		this.serviceType = serviceType;
	}

	@Override
	public Runnable createService() throws NFlightRemoteException {
		InetSocketAddress endpoint = getEndpoint(Config
				.getInt(RMIServantConfig.KEY_INT_RMI_DEFAULT_PORT));
		if (this.serviceType.equals(ENUM_SERVICE_TYPE.rmi_unicast)) {
			return new UnicastRMIServantImpl(endpoint);
		} else if (this.serviceType.equals(ENUM_SERVICE_TYPE.rmi_activation)) {
			return new ActivatableRMIServantImpl(endpoint);
		} else {
			throw new RMIServantException("서비스 객체 생성에 실패하였습니다.");
		}
	}
}
