package com.abreqadhabra.nflight.application.service.rmi.unicast;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.rmi.AbstractRMIServant;
import com.abreqadhabra.nflight.application.service.rmi.RMIServiceException;
import com.abreqadhabra.nflight.application.service.rmi.RMIServiceHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class UnicastRMIServantImpl extends AbstractRMIServant {
	private static final Class<UnicastRMIServantImpl> THIS_CLAZZ = UnicastRMIServantImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public UnicastRMIServantImpl(final Configure configure,
			final InetAddress addr, final int port) throws Exception {
		super(configure, addr, port);
		this.serviceName = Configure.RMI_SERVICE_TYPE.unicast.toString();
		this.boundName = RMIServiceHelper.getBoundName(
				super.addr.getHostAddress(), super.port, this.serviceName);
	}

	@Override
	public void run() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			if (RMIServiceHelper.isActivatedRegistry(this.registry,
					super.boundName)) {
				throw new RMIServiceException(this.boundName
						+ "가 레지스트리에 이미 등록되어 있습니다.");
			} else {
				final Remote stub = UnicastRemoteObject.exportObject(this, 0);
				RMIServiceHelper.rebind(this.registry, this.boundName, stub);
				LOGGER.logp(
						Level.FINER,
						THIS_CLAZZ.getName(),
						METHOD_NAME,
						stub + " Stub bound in registry."
								+ Arrays.toString(registry.list()));
			}
		} catch (final Exception e1) {
			e1.printStackTrace();
		}
	}
}
