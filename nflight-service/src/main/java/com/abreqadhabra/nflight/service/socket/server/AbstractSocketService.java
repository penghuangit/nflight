package com.abreqadhabra.nflight.service.socket.server;

import com.abreqadhabra.nflight.service.core.Profile;
import com.abreqadhabra.nflight.service.core.server.IService;

public abstract class AbstractSocketService implements IService {

	@Override
	public String getServiceName() {
		return Profile.RMI_SERVICE.activatable.toString();
	}

	@Override
	public boolean isRunning() {
		return true;
	}

	@Override
	public String sayHello() {
		return "Hello, world!";
	}

}
