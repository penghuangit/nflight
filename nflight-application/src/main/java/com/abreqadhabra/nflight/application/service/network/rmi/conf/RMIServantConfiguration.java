package com.abreqadhabra.nflight.application.service.network.rmi.conf;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.abreqadhabra.nflight.application.service.conf.ServiceConfiguration;

public interface RMIServantConfiguration extends ServiceConfiguration {

	static Path FILE_ACTIVATABLE_MARSHALLED_OBJECT = Paths
			.get("com/abreqadhabra/nflight/application/service/rmi/ActivatableRMIServantImpl.ser");
	static Path FILE_ACTIVATABLE_POLICY = Paths
			.get("com/abreqadhabra/nflight/application/service/rmi/conf/activatable.policy");
	static Path FILE_ACTIVATABLE_RMID_POLICY = Paths
			.get("com/abreqadhabra/nflight/application/service/rmi/conf/rmid.policy");
	
	static String PREFIX_ACTIVATABLE_CODEBASE = "file://";
	
	static String STR_RMI_DEFAULT_PORT = "nflight.service.rmi.bind.port.default";
	
	static String STR_ACTIVATABLE_RMI_BOUND_NAME = "nflight.service.rmi.activatable.bound.name";
	static String STR_ACTIVATABLE_RMI_RMID_DELAY_SECONDS = "nflight.service.rmi.activatable.rmid.delay.seconds";
	static String STR_ACTIVATABLE_RMI_SYSTEM_COMMAND_RMID_START = "nflight.service.rmi.activatable.rmi.system.command.rmid.start";
	
	static String STR_ACTIVATABLE_RMI_SYSTEM_COMMAND_RMID_STOP = "nflight.service.rmi.activatable.rmi.system.command.rmid.stop";
	static String STR_UNICAST_RMI_BOUND_NAME = "nflight.service.rmi.unicast.bound.name";
	static String STR_UNICAST_RMI_RUNNING = "nflight.service.rmi.unicast.running";

	
}
