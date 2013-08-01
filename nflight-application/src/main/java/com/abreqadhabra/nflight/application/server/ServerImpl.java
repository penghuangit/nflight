package com.abreqadhabra.nflight.application.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Profile;
import com.abreqadhabra.nflight.application.server.aio.SocketServerImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

//Strategy Context
public class ServerImpl implements IServer {
    private static final Class<ServerImpl> THIS_CLAZZ = ServerImpl.class;
    private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

    public ServerImpl(final Profile profile) {

	final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

	LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, LoggingHelper.describe(THIS_CLAZZ));

	try {
	    new SocketServerImpl(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), 9999)).startup();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    @Override
    public void startup() throws Exception {
	// TODO Auto-generated method stub

    }

    @Override
    public boolean status() throws Exception {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public void shutdown() throws Exception {
	// TODO Auto-generated method stub

    }

}
