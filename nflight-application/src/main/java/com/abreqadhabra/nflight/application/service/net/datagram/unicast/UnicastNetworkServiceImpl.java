package com.abreqadhabra.nflight.application.service.net.datagram.unicast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.AbstractNetworkServiceImpl;
import com.abreqadhabra.nflight.application.service.net.datagram.unicast.transport.UnicastBlockingAcceptor;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class UnicastNetworkServiceImpl extends AbstractNetworkServiceImpl {
	private static Class<UnicastNetworkServiceImpl> THIS_CLAZZ = UnicastNetworkServiceImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public UnicastNetworkServiceImpl(Configure configure,
			ThreadPoolExecutor threadPool, InetSocketAddress endpoint) {
		super(configure, threadPool, endpoint);
	}

	@Override
	public void startup() {
		try {
			this.isRunning = true;
			// wait for incoming connections
			UnicastBlockingAcceptor acceptor = new UnicastBlockingAcceptor(
					this.isRunning, this.endpoint, /* this.threadPool, */
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
