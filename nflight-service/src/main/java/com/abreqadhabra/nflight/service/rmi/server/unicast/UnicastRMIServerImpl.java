package com.abreqadhabra.nflight.service.rmi.server.unicast;

import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.core.server.IService;
import com.abreqadhabra.nflight.service.rmi.server.AbstractRMIServer;

public class UnicastRMIServerImpl extends AbstractRMIServer {

	private static final Class<UnicastRMIServerImpl> THIS_CLAZZ = UnicastRMIServerImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	

	public UnicastRMIServerImpl(ProfileImpl profile, IService service) throws Exception {
		super(profile, service);
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();


	}

	@Override
	public void startup() throws Exception {
		try {
			// NFlightService uniServant = new
			// UnicastRemoteObjectNFlightServiceImpl();
			// NFlightService actServant = new ActivatableNFlightServiceImpl();

//			Remote obj = super.rman.getUnicastRemoteObjectNFlightServiceImpl();
			// Remote obj = this.rman.getActivatableNFlightServiceImpl();

			Remote stub =  UnicastRemoteObject.exportObject(super.getService(), 0); 
			
		
			super.rebind(super.getBoundName("unicast"), stub);
		} catch (Exception e) {
			throw e;
		}
	}
}