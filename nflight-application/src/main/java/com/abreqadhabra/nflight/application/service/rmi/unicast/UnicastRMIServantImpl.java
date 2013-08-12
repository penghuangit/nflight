package com.abreqadhabra.nflight.application.service.rmi.unicast;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
		this.boundName = RMIServiceHelper.getBoundName(
				this.addr.getHostAddress(), this.port,
				Configure.RMI_SERVICE_TYPE.unicast.toString());
	}

	@Override
	public void run() {
		Thread.currentThread().getStackTrace()[1].getMethodName();

		try {
			if (RMIServiceHelper.isActivatedRegistry(super.registry,
					super.boundName)) {
				throw new RMIServiceException(this.boundName
						+ "가 레지스트리에 이미 등록되어 있습니다.");
			} else {
				try {
					final Remote servant = UnicastRemoteObject.exportObject(
							this, 0);
					RMIServiceHelper.rebind(super.registry, RMIServiceHelper
							.getBoundName(super.addr.getHostAddress(),
									super.port, "unicast"), servant);
				} catch (final Exception e) {
					throw e;
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String sayHello() throws RemoteException {
		return "Hello, world!";
	}

}
