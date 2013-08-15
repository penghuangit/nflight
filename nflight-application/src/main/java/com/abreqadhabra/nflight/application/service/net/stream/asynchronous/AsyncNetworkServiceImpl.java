package com.abreqadhabra.nflight.application.service.net.stream.asynchronous;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.exception.ServiceException;
import com.abreqadhabra.nflight.application.service.net.AbstractNetworkServiceImpl;
import com.abreqadhabra.nflight.application.service.net.stream.asynchronous.transport.AsyncAcceptor;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.UnexpectedException;

public class AsyncNetworkServiceImpl extends AbstractNetworkServiceImpl {

	public AsyncNetworkServiceImpl(Configure configure,
			ThreadPoolExecutor threadPool, InetSocketAddress endpoint) {
		super(configure, threadPool, endpoint);
	}

	@Override
	public void startup() throws NFlightException {
		try {
			// wait for incoming connections
			AsyncAcceptor acceptor = new AsyncAcceptor(this.endpoint,
					this.threadPool, this.configure);
			new Thread(acceptor).start();
		} catch (NFlightException e) {
			throw e;
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}
}
