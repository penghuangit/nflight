package com.abreqadhabra.nflight.service.socket.server.stream;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.core.server.IService;
import com.abreqadhabra.nflight.service.socket.server.AbstractSocketServer;
import com.abreqadhabra.nflight.service.socket.server.IServerStrategy;

public class StreamSocketServerImpl extends AbstractSocketServer {

	private static final Class<StreamSocketServerImpl> THIS_CLAZZ = StreamSocketServerImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public StreamSocketServerImpl(ProfileImpl profile, IService service)
			throws Exception {
		super(profile, service);
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Instantiating a " + THIS_CLAZZ.getSimpleName() + " Class ");
	}
	
	IServerStrategy serverStrategy;

	public IServerStrategy getServerStrategy() {
		return serverStrategy;
	}

	public void setServerStrategy(IServerStrategy serverStrategy) {
		this.serverStrategy = serverStrategy;
	}

	@Override
	public void init() throws Exception {
		serverStrategy.initBehaviorInterface();
		
	}

	@Override
	public void startup() throws Exception {
		serverStrategy.startupBehaviorInterface();
		
	}

	@Override
	public void shutdown() throws Exception {
		serverStrategy.shutdownBehaviorInterface();
	}

	@Override
	public boolean status() throws Exception {		
		return serverStrategy.statusBehaviorInterface();
	}


}