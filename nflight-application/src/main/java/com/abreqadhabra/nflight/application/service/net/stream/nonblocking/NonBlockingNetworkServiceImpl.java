package com.abreqadhabra.nflight.application.service.net.stream.nonblocking;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.AbstractNetworkServiceImpl;
import com.abreqadhabra.nflight.application.transport.network.stream.nonblocking.NonBlockingAcceptor;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.UnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class NonBlockingNetworkServiceImpl extends AbstractNetworkServiceImpl {
	private static Class<NonBlockingNetworkServiceImpl> THIS_CLAZZ = NonBlockingNetworkServiceImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public NonBlockingNetworkServiceImpl(Configure configure,
			ThreadPoolExecutor threadPool, InetSocketAddress endpoint) {
		super(configure, threadPool, endpoint);
	}

	@Override
	public void startup() throws NFlightException {
		try {
			this.isRunning = true;
			// wait for incoming connections
			NonBlockingAcceptor acceptor = new NonBlockingAcceptor(
					this.isRunning, this.endpoint, /* this.threadPool, */
					this.configure);
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
