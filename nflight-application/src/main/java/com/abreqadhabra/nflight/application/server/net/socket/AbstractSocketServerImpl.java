package com.abreqadhabra.nflight.application.server.net.socket;

import java.util.concurrent.ThreadPoolExecutor;

import com.abreqadhabra.nflight.application.launcher.Configure;

public abstract class AbstractSocketServerImpl implements ISocketServer {

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
		open();
		bind();
		accept();
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
