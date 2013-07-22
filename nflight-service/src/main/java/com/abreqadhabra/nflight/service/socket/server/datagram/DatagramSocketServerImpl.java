package com.abreqadhabra.nflight.service.socket.server.datagram;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.core.server.IService;
import com.abreqadhabra.nflight.service.socket.server.AbstractSocketServer;

public class DatagramSocketServerImpl extends AbstractSocketServer {

	private static final Class<DatagramSocketServerImpl> THIS_CLAZZ = DatagramSocketServerImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public DatagramSocketServerImpl(ProfileImpl profile, IService service)
			throws Exception {
		super(profile, service);
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Instantiating a " + THIS_CLAZZ.getSimpleName() + " Class ");
	}

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startup() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean status() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}


}