package com.abreqadhabra.nflight.application.service.network.rmi.conf;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.abreqadhabra.nflight.application.service.conf.ServiceConfig;

public interface RMIServantConfig extends ServiceConfig {
	static String KEY_STR_RMI_ACTIVATABLE_BOUND_NAME = "nflight.network.rmi.activatable.bound.name";
	static String KEY_INT_RMI_ACTIVATABLE_RMID_DELAY_SECONDS = "nflight.network.rmi.activatable.rmid.delay.seconds";
	static String KEY_STR_RMI_ACTIVATABLE_RMID_START = "nflight.network.rmi.activatable.rmid.start";
	static String KEY_STR_RMI_ACTIVATABLE_RMID_STOP = "nflight.network.rmi.activatable.rmid.stop";
	static String KEY_BOO_RMI_ACTIVATABLE_RUNNING = "nflight.network.rmi.activatable.running";

	static String KEY_INT_RMI_DEFAULT_PORT = "nflight.network.rmi.default.port";
	
	static String KEY_STR_RMI_UNICAST_BOUND_NAME = "nflight.network.rmi.unicast.bound.name";
	static String KEY_STR_RMI_UNICAST_RUNNING = "nflight.network.rmi.unicast.running";
	
	static Path PATH_RMI_ACTIVATABLE_MARSHALLED_OBJECT = Paths
			.get("com/abreqadhabra/nflight/application/service/rmi/ActivatableRMIServantImpl.ser");
	static Path PATH_RMI_ACTIVATABLE_POLICY = Paths
			.get("com/abreqadhabra/nflight/application/service/rmi/conf/activatable.policy");
	static Path PATH_RMI_ACTIVATABLE_RMID_POLICY = Paths
			.get("com/abreqadhabra/nflight/application/service/rmi/conf/rmid.policy");

	static String STR_PREFIX_RMI_ACTIVATABLE_CODEBASE = "file://";
}
