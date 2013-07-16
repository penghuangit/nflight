package com.abreqadhabra.nflight.service.socket.server;

import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.boot.BootProfile;
import com.abreqadhabra.nflight.service.core.server.NFlightServer;

public class NFlightSocketServerImpl implements NFlightServer {

	private static final Class<NFlightSocketServerImpl> THIS_CLAZZ = NFlightSocketServerImpl.class;
	private Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);


	private BootProfile profile;

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	public NFlightSocketServerImpl(BootProfile profile) throws Exception {
		this.setProfile(profile);
		init();
	}

	private void init() throws Exception {
		LOGGER.info("init: " + THIS_CLAZZ.getCanonicalName());
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

	public BootProfile getProfile() {
		return profile;
	}

	public void setProfile(BootProfile profile) {
		this.profile = profile;
	}


}
