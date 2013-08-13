package com.abreqadhabra.nflight.application.server.net.socket;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public abstract class AbstractSocketServerImpl implements ISocketServer {
	private static Class<AbstractSocketServerImpl> THIS_CLAZZ = AbstractSocketServerImpl.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	protected Configure configure;
	protected ThreadPoolExecutor threadPoolExecutor;

	public AbstractSocketServerImpl(Configure configure,
			ThreadPoolExecutor threadPoolExecutor) {
		this.configure = configure;
		this.threadPoolExecutor = threadPoolExecutor;
		init();
	}

	@Override
	public void startup() throws Exception {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		if (open()) {
			bind();
			accept();
		} else {
			LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"The server-socket channel cannot be opened!");
		}
	}

	@Override
	public void shutdown() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean status() {
		// TODO Auto-generated method stub
		return false;
	}

}
