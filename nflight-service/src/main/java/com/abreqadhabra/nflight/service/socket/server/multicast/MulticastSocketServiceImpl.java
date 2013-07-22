package com.abreqadhabra.nflight.service.socket.server.multicast;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.socket.server.AbstractSocketService;

public class MulticastSocketServiceImpl extends AbstractSocketService {

	private static final Class<MulticastSocketServiceImpl> THIS_CLAZZ = MulticastSocketServiceImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	
	public MulticastSocketServiceImpl() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Instantiating a " + THIS_CLAZZ.getSimpleName() + " Class ");
	}


}
