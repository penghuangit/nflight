package com.abreqadhabra.nflight.service.rmi.server.unicast;

import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.rmi.server.AbstractRMIServer;

public class UnicastRMIServerImpl extends AbstractRMIServer {

	private static final Class<UnicastRMIServerImpl> THIS_CLAZZ = UnicastRMIServerImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	
	public UnicastRMIServerImpl(ProfileImpl profile) throws Exception {
		super(profile);
	}

	@Override
	public void startup() throws Exception {
		try {
			// NFlightService uniServant = new
			// UnicastRemoteObjectNFlightServiceImpl();
			// NFlightService actServant = new ActivatableNFlightServiceImpl();

//			Remote obj = super.rman.getUnicastRemoteObjectNFlightServiceImpl();
			// Remote obj = this.rman.getActivatableNFlightServiceImpl();

			UnicastRMIServiceImpl servant = new UnicastRMIServiceImpl();
			
			Remote stub =  UnicastRemoteObject.exportObject(servant, 0); 
			
		
			super.rman.rebind(super.rman.getBoundName("unicast"), stub);
		} catch (Exception e) {
			throw e;
		}
	}
}