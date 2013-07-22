package com.abreqadhabra.nflight.app.server.service;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.app.core.Profile.SERVICE_NAME;
import com.abreqadhabra.nflight.app.server.service.rmi.ActivatableRMIServiceImpl;
import com.abreqadhabra.nflight.app.server.service.rmi.UnicastRMIServiceImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

// Flyweight FlyweightFactory
public class ServiceFactory {

	private static final Class<ServiceFactory> THIS_CLAZZ = ServiceFactory.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

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
	public static IService getService(ServiceDescriptor sd) throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		IService _operation = operationMap.get(sd.getServiceName());
		if (_operation == null) {
			SERVICE_NAME _service = SERVICE_NAME.valueOf(sd.getServiceName());
			switch (_service) {
			case unicast:
				_operation = new UnicastRMIServiceImpl(sd);
				break;
			case activatable:
				//_operation = new ActivatableRMIServiceImpl(sd);
				_operation = new ActivatableRMIServiceImpl(sd);
				break;
			default:
				break;
			}
			operationMap.put(sd.getServiceName(), _operation);

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Flyweight -> FlyweightFactory  : Creating operation of service name : "
							+ sd.getServiceName() + "/"
							+ _operation.getClass().getSimpleName());
		}

		return _operation;
	}

}
