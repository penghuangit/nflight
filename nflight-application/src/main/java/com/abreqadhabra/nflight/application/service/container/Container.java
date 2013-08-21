package com.abreqadhabra.nflight.application.service.container;

import com.abreqadhabra.nflight.application.service.conf.ServiceConfig.ENUM_SERVICE_TYPE;

public interface Container {

	void startupService(ENUM_SERVICE_TYPE serviceType);

	void startupAllService();

	void shutdown(ENUM_SERVICE_TYPE serviceType);

	void shutdownAllService();

}
