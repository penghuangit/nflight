package com.abreqadhabra.nflight.application.service.net.stream;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.INetworkService;
import com.abreqadhabra.nflight.application.service.net.ServerChannelFactory;
import com.abreqadhabra.nflight.application.service.net.stream.blocking.BlockingNetworkServiceImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public abstract class AbstractNetworkServiceImpl
		implements
			INetworkService,
			Runnable {
	private static final Class<BlockingNetworkServiceImpl> THIS_CLAZZ = BlockingNetworkServiceImpl.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	protected Configure configure;
	protected ThreadPoolExecutor threadPool;
	protected int backlog;
	protected InetSocketAddress endpoint;
	protected boolean isRunning;

	public AbstractNetworkServiceImpl(final Configure configure,
			final ThreadPoolExecutor threadPool,
			final InetSocketAddress socketAddress) {
		this.configure = configure;
		this.endpoint = socketAddress;
		this.threadPool = threadPool;
	}

	@Override
	public ServerChannelFactory createServerChannelFactory() {
		return new ServerChannelFactory();
	}

}
