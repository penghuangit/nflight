package com.abreqadhabra.nflight.application.container.network.server;

import java.net.InetSocketAddress;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationSystem;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.Configure;
import com.abreqadhabra.nflight.application.service.rmi.RMIServantException;
import com.abreqadhabra.nflight.application.service.rmi.impl.ActivatableRMIServantImpl;
import com.abreqadhabra.nflight.application.service.rmi.impl.RMIDRunnableImpl;
import com.abreqadhabra.nflight.application.service.rmi.impl.UnicastRMIServantImpl;
import com.abreqadhabra.nflight.application.service.socket.impl.AsynchronousSocketServiceImpl;
import com.abreqadhabra.nflight.application.service.socket.impl.BlockingSocketServiceImpl;
import com.abreqadhabra.nflight.application.service.socket.impl.MulticastDatagramServiceImpl;
import com.abreqadhabra.nflight.application.service.socket.impl.NonblockingSocketServiceImpl;
import com.abreqadhabra.nflight.application.service.socket.impl.UnicastSocketServiceImpl;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ServiceRunnableFactory {
	private static Class<ServiceRunnableFactory> THIS_CLAZZ = ServiceRunnableFactory.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public Runnable createBlockingService(boolean isRunning,
			Configure configure, InetSocketAddress endpoint)
			throws NFlightException {
		return new BlockingSocketServiceImpl(isRunning, configure, endpoint);
	}

	public Runnable createNonBlockingService(boolean isRunning,
			Configure configure, InetSocketAddress endpoint)
			throws NFlightException {
		return new NonblockingSocketServiceImpl(isRunning, endpoint, configure);
	}

	public Runnable createAsyncService(boolean isRunning, Configure configure,
			InetSocketAddress endpoint) throws NFlightException {
		return new AsynchronousSocketServiceImpl(isRunning, configure, endpoint);
	}

	public Runnable createUnicastService(boolean isRunning,
			Configure configure, InetSocketAddress endpoint)
			throws NFlightException {
		return new UnicastSocketServiceImpl(isRunning, configure, endpoint);
	}

	public Runnable createMulticastService(boolean isRunning,
			Configure configure, InetSocketAddress endpoint)
			throws NFlightException {
		return new MulticastDatagramServiceImpl(isRunning, configure, endpoint);
	}

	public Runnable createRMIUnicastService(Configure configure,
			InetSocketAddress endpoint) throws NFlightRemoteException {
		return new UnicastRMIServantImpl(endpoint.getAddress(),
				endpoint.getPort(), Configure.SERVICE_TYPE.rmi_unicast.toString());
	}

	public Runnable createRMIActivatableService(Configure configure,
			InetSocketAddress endpoint) throws NFlightRemoteException {
		this.executeRMID(configure);
		return new ActivatableRMIServantImpl(endpoint.getAddress(),
				endpoint.getPort(),
				Configure.SERVICE_TYPE.rmi_activation.toString());
	}

	private void executeRMID(Configure configure) throws NFlightRemoteException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			// 액티베이션 시스템이 기동되어 있을 경우 RMID를 기동을 스킵
			ActivationSystem activationSystem = ActivationGroup.getSystem();
			LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getName(), METHOD_NAME,
					"액티베이션 시스템이 기동되어 있을 경우 RMID를 기동을 스킵 " + activationSystem);
		} catch (ActivationException ae) {
			try {
				// Port already in use: 1098 Address already in use: JVM_Bind
				// 액티베이션 시스템이 기동되어 있지 않을 경우 RMID 시작
				new Thread(new RMIDRunnableImpl(configure)).start();
				// 콜러블로 기동결과에 대한 값이 있을때까지 대기
				Thread.sleep(configure
						.getInt(Configure.ACTIVATABLE_RMI_RMID_DELAY_SECONDS));
			} catch (InterruptedException e) {
				throw new RMIServantException(e);
			}
			LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getName(), METHOD_NAME,
					"액티베이션 시스템이 기동되어 있지 않을 경우 RMID 시작 ");
		}
	}
}
