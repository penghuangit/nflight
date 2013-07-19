package com.abreqadhabra.nflight.service.core.server;

import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.ProfileImpl;

//Receiver class
public abstract class AbstractServerReceiver {
	private static final Class<AbstractServerReceiver> THIS_CLAZZ = AbstractServerReceiver.class;
	private Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	protected ProfileImpl profile;
	protected IService service;

	public AbstractServerReceiver(ProfileImpl profile, IService service)
			throws Exception {
		this.profile = profile;
		this.service = service;
		init();
	}

	public abstract void init() throws Exception;

	public abstract void startup() throws Exception;

	public abstract void shutdown() throws Exception;

	public abstract boolean status() throws Exception;
}
