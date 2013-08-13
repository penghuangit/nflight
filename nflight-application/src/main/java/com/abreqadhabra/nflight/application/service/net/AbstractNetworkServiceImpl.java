package com.abreqadhabra.nflight.application.service.net;

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
	private static Class<BlockingNetworkServiceImpl> THIS_CLAZZ = BlockingNetworkServiceImpl.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	protected Configure configure;
	protected ThreadPoolExecutor threadPool;
	protected int backlog;
	protected InetSocketAddress endpoint;
	protected boolean isRunning;

	public AbstractNetworkServiceImpl(Configure configure,
			ThreadPoolExecutor threadPool,
			InetSocketAddress endpoint) {
		this.configure = configure;
		this.endpoint = endpoint;
		this.threadPool = threadPool;
	}

	@Override
	public ServerChannelFactory createServerChannelFactory() {
		return new ServerChannelFactory();
	}

}
