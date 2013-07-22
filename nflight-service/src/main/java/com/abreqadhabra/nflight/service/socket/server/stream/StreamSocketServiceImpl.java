package com.abreqadhabra.nflight.service.socket.server.stream;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.socket.server.AbstractSocketService;

public class StreamSocketServiceImpl extends AbstractSocketService {

	private static final Class<StreamSocketServiceImpl> THIS_CLAZZ = StreamSocketServiceImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	
	public StreamSocketServiceImpl(){
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Instantiating a " + THIS_CLAZZ.getSimpleName() + " Class ");
	}

}
