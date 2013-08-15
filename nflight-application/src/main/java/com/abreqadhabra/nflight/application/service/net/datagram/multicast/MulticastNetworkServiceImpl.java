package com.abreqadhabra.nflight.application.service.net.datagram.multicast;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.exception.ServiceException;
import com.abreqadhabra.nflight.application.service.net.AbstractNetworkServiceImpl;
import com.abreqadhabra.nflight.application.service.net.datagram.multicast.tranport.MulticastAcceptor;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.UnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class MulticastNetworkServiceImpl extends AbstractNetworkServiceImpl {
	private static Class<MulticastNetworkServiceImpl> THIS_CLAZZ = MulticastNetworkServiceImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public MulticastNetworkServiceImpl(Configure configure,
			ThreadPoolExecutor threadPool, InetSocketAddress endpoint) {
		super(configure, threadPool, endpoint);
	}

	@Override
	public void startup() throws NFlightException {
		try {
			this.isRunning = true;
			// wait for incoming connections
			MulticastAcceptor acceptor = new MulticastAcceptor(isRunning,
					this.endpoint,
					/* this.threadPool, */this.configure);
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
