package com.abreqadhabra.nflight.application.service.temp.server;

import java.net.InetSocketAddress;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationSystem;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.transport.exception.RMIServiceException;
import com.abreqadhabra.nflight.application.transport.network.datagram.multicast.MulticastService;
import com.abreqadhabra.nflight.application.transport.network.datagram.unicast.UnicastBlockingService;
import com.abreqadhabra.nflight.application.transport.network.stream.asynchronous.AsyncService;
import com.abreqadhabra.nflight.application.transport.network.stream.blocking.BlockingService;
import com.abreqadhabra.nflight.application.transport.network.stream.nonblocking.NonBlockingService;
import com.abreqadhabra.nflight.application.transport.rmi.activation.ActivatableRMIServantImpl;
import com.abreqadhabra.nflight.application.transport.rmi.rmid.RMIDProcess;
import com.abreqadhabra.nflight.application.transport.rmi.unicast.UnicastRMIServantImpl;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;

public class ServiceFactory {

	public Runnable createBlockingService(boolean isRunning,
			Configure configure, InetSocketAddress endpoint)
			throws NFlightException {
		return new BlockingService(isRunning, configure, endpoint);
	}

	public Runnable createNonBlockingService(boolean isRunning,
			Configure configure, InetSocketAddress endpoint)
			throws NFlightException {
		return new NonBlockingService(isRunning, endpoint, configure);
	}

	public Runnable createAsyncService(boolean isRunning, Configure configure,
			InetSocketAddress endpoint) throws NFlightException {
		return new AsyncService(isRunning, configure, endpoint);
	}

	public Runnable createUnicastService(boolean isRunning,
			Configure configure, InetSocketAddress endpoint)
			throws NFlightException {
		return new UnicastBlockingService(isRunning, configure, endpoint);
	}

	public Runnable createMulticastService(boolean isRunning,
			Configure configure, InetSocketAddress endpoint)
			throws NFlightException {
		return new MulticastService(isRunning, configure, endpoint);
	}

	public Runnable createRMIUnicastService(Configure configure,
			InetSocketAddress endpoint) throws NFlightRemoteException {
		return new UnicastRMIServantImpl(endpoint.getAddress(),
				endpoint.getPort());
	}

	public Runnable createRMIActivatableService(Configure configure,
			InetSocketAddress endpoint) throws NFlightRemoteException {
		executeRMID(configure);
		return new ActivatableRMIServantImpl(endpoint.getAddress(),
				endpoint.getPort());
	}

	private void executeRMID(Configure configure) throws NFlightRemoteException {
		try {
			// 액티베이션 시스템이 기동되어 있을 경우 RMID를 기동을 스킵
			ActivationSystem activationSystem = ActivationGroup.getSystem();
			System.out.println("액티베이션 시스템이 기동되어 있을 경우 RMID를 기동을 스킵 "
					+ activationSystem);
		} catch (ActivationException ae) {
			try {
				// Port already in use: 1098 Address already in use: JVM_Bind
				// 액티베이션 시스템이 기동되어 있지 않을 경우 RMID 시작
				new Thread(new RMIDProcess(configure)).start();
				// 콜러블로 기동결과에 대한 값이 있을때까지 대기
				Thread.sleep(configure
						.getInt(Configure.ACTIVATABLE_RMI_RMID_DELAY_SECONDS));
			} catch (InterruptedException e) {
				throw new RMIServiceException(e);
			}
			System.out.println("액티베이션 시스템이 기동되어 있지 않을 경우 RMID 시작 ");
		}
	}
}	
