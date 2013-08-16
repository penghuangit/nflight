package com.abreqadhabra.nflight.application.service.net.stream.blocking;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.AbstractNetworkServiceImpl;
import com.abreqadhabra.nflight.application.transport.network.stream.blocking.BlockingAcceptor;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.UnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class BlockingNetworkServiceImpl extends AbstractNetworkServiceImpl {
	private static Class<BlockingNetworkServiceImpl> THIS_CLAZZ = BlockingNetworkServiceImpl.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public BlockingNetworkServiceImpl(Configure configure,
			ThreadPoolExecutor threadPool, InetSocketAddress endpoint) {
		super(configure, threadPool, endpoint);
	}

	@Override
	public void startup() throws NFlightException {
		try {
			this.isRunning = true;
			// wait for incoming connections
			BlockingAcceptor acceptor = new BlockingAcceptor(this.isRunning, this.endpoint,
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
