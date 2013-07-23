package com.abreqadhabra.nflight.app.server.service;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.app.core.Profile.SERVICE_NAME;
import com.abreqadhabra.nflight.app.server.service.rmi.ActivatableRMIServantImpl;
import com.abreqadhabra.nflight.app.server.service.rmi.UnicastRMIServantImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

// Flyweight FlyweightFactory
public class ServiceFactory {

	private static final Class<ServiceFactory> THIS_CLAZZ = ServiceFactory.class;
	private static Logger LOGGER = LoggingHelper
			.getLogger(ServiceFactory.THIS_CLAZZ);

	/**
	 * Pool for one soldier only if there are more soldier types this can be an
	 * array or list or better a HashMap
	 * 
	 */
	private static final HashMap<String, IService> operationMap = new HashMap<String, IService>();

	/**
	 * getFlyweight
	 * 
	 * @return
	 * @throws Exception
	 */
	public static IService getService(final ServiceDescriptor sd)
			throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		IService _operation = ServiceFactory.operationMap.get(sd
				.getServiceName());
		if (_operation == null) {
			final SERVICE_NAME _service = SERVICE_NAME.valueOf(sd
					.getServiceName());
			switch (_service) {
			case unicast:
				_operation = new UnicastRMIServantImpl(sd);
				break;
			case activatable:
				// _operation = new ActivatableRMIServiceImpl(sd);
				_operation = new ActivatableRMIServantImpl(sd);
				break;
			default:
				break;
			}
			ServiceFactory.operationMap.put(sd.getServiceName(), _operation);

			ServiceFactory.LOGGER.logp(Level.FINER,
					ServiceFactory.THIS_CLAZZ.getName(), METHOD_NAME,
					"Flyweight -> FlyweightFactory  : Creating operation of service name : "
							+ sd.getServiceName() + "/"
							+ _operation.getClass().getSimpleName());
		}

		return _operation;
	}

}
