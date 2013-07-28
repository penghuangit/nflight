package com.abreqadhabra.nflight.app.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.app.core.Profile;
import com.abreqadhabra.nflight.app.server.service.IService;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

//Strategy Context
public class ServerImpl implements IServer {

	private static final Class<ServerImpl> THIS_CLAZZ = ServerImpl.class;
	private static Logger LOGGER = LoggingHelper
			.getLogger(THIS_CLAZZ);

	Profile profile;
	IService service;
	String serviceName;

	public ServerImpl(final String _serviceName) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		this.serviceName = _serviceName;
		LOGGER.logp(
				Level.FINER,
				THIS_CLAZZ.getName(),
				METHOD_NAME,
				"Strategy -> Context : "
						+ THIS_CLAZZ.getSimpleName());

	}

	@Override
	public IService getService() {
		// TODO Auto-generated method stub
		return this.service;
	}

	@Override
	public void init() throws Exception {
		Thread.currentThread().getStackTrace()[1].getMethodName();

		this.service.init();

	}

	@Override
	public void setService(final IService _operation) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		this.service = _operation;

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(),
				METHOD_NAME, "Strategy -> ConcreteStrategy  : "
						+ this.service.getClass().getSimpleName());
	}

	@Override
	public void shutdown() throws Exception {
		this.service.shutdown();
	}

	@Override
	public void startup() throws Exception {
		this.service.startup();
	}

	@Override
	public boolean status() throws Exception {
		return this.service.status();
	}

}
