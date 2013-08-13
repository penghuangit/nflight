package com.abreqadhabra.nflight.application.service.net.stream.nonblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.AbstractNetworkServiceImpl;
import com.abreqadhabra.nflight.application.service.net.stream.nonblocking.transport.NonBlockingAcceptor;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class NonBlockingNetworkServiceImpl extends AbstractNetworkServiceImpl {
	private static Class<NonBlockingNetworkServiceImpl> THIS_CLAZZ = NonBlockingNetworkServiceImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public NonBlockingNetworkServiceImpl(Configure configure,
			ThreadPoolExecutor threadPool, InetSocketAddress endpoint) {
		super(configure, threadPool, endpoint);
	}

	@Override
	public void startup() {
		try {
			this.isRunning = true;
			// wait for incoming connections
			NonBlockingAcceptor acceptor = new NonBlockingAcceptor(
					this.isRunning, this.endpoint, /*this.threadPool,*/
					this.configure);
			new Thread(acceptor).start();
		} catch (IOException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

}
