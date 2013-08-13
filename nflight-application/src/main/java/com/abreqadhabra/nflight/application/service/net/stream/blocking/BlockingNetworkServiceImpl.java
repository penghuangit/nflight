package com.abreqadhabra.nflight.application.service.net.stream.blocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.AbstractNetworkServiceImpl;
import com.abreqadhabra.nflight.application.service.net.stream.blocking.transport.BlockingAcceptor;
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
	public void startup() {
		try {
			this.isRunning = true;
			// wait for incoming connections
			BlockingAcceptor acceptor = new BlockingAcceptor(this.isRunning, this.endpoint,
					this.threadPool, this.configure);
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
