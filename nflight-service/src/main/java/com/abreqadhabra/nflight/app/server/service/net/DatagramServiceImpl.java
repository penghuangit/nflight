package com.abreqadhabra.nflight.app.server.service.net;

import java.util.logging.Logger;

import com.abreqadhabra.nflight.app.server.service.ServiceDescriptor;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

//Strategy ConcreteStrategy
public class DatagramServiceImpl extends AbstractService {

	private static final Class<DatagramServiceImpl> THIS_CLAZZ = DatagramServiceImpl.class;
	private static Logger LOGGER = LoggingHelper
			.getLogger(DatagramServiceImpl.THIS_CLAZZ);

	DatagramAcceptor serverThread;

	public DatagramServiceImpl(final ServiceDescriptor sd) throws Exception {
		super(sd);
	}

	@Override
	public void init() throws Exception {
		Thread.currentThread().getStackTrace()[1].getMethodName();

	}

	@Override
	public boolean isRunning() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String sayHello() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startup() throws Exception {
		serverThread = new DatagramAcceptor(desc.getAddress(), desc.getPort());
		serverThread.start();
	}

	public void shutdown() throws Exception {
		serverThread.disconnect();
	}

	
	@Override
	public boolean status() throws Exception {
		return true;
	}

}
