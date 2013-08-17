package com.abreqadhabra.nflight.application.trash_dispatcher;

import java.util.concurrent.ConcurrentHashMap;

public class Dispatcher {

	ConcurrentHashMap<String, ServiceDescriptor> registry = new ConcurrentHashMap<String, ServiceDescriptor>();

	public void register(String serviceName, ServiceDescriptor serviceDescriptor) throws Exception {
		ServiceDescriptor registeredServiceDescriptor = locate(serviceName);
		if (registeredServiceDescriptor == null) {
			registry.put(serviceName, serviceDescriptor);
		}
	}

	public ServiceDescriptor locate(String serviceName) throws Exception {
		ServiceDescriptor serviceDescriptor = (ServiceDescriptor) registry
				.get(serviceName);

		if (serviceDescriptor == null) {
		//	throw new Exception("not found");
		}
		return serviceDescriptor;
	}

}
