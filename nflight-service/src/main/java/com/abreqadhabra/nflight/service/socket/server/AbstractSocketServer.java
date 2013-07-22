package com.abreqadhabra.nflight.service.socket.server;

import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.core.server.AbstractServerReceiver;
import com.abreqadhabra.nflight.service.core.server.IService;

public abstract class AbstractSocketServer extends AbstractServerReceiver {

	private static final Class<AbstractSocketServer> THIS_CLAZZ = AbstractSocketServer.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public AbstractSocketServer(ProfileImpl profile, IService service)
			throws Exception {
		super(profile, service);
	}

}
