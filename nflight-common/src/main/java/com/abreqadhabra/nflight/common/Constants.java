package com.abreqadhabra.nflight.common;

public class Constants {

	public class Boot {
		public static final String DEFAULT_PROPERTIES_FILE_NAME = "/com/abreqadhabra/nflight/common/boot/config/boot.properties";

		// BOOT OPTIONS CONFIGURATION
		public static final String KEY_BOOT_OPTION_SERVICE = "nflight.system.boot.option.service";
		public static final String KEY_BOOT_OPTION_SERVICE_NAME = "nflight.system.boot.option.service.name";
		public static final String KEY_BOOT_OPTION_SERVICE_CLASS = "nflight.system.boot.option.service.class";
		public static final String KEY_BOOT_OPTION_SERVICE_COMMAND = "nflight.system.boot.option.service.command";
		public static final String KEY_BOOT_OPTION_HOST = "nflight.system.boot.option.host";
		public static final String KEY_BOOT_OPTION_PORT = "nflight.system.boot.option.port";
		public static final String KEY_BOOT_OPTION_GUI = "nflight.system.boot.option.gui";
		public static final String KEY_BOOT_OPTION_CONF = "nflight.system.boot.option.conf";

		public static final String STR_BOOT_OPTION_SERVICE = "service";
		public static final String STR_BOOT_OPTION_SERVICE_NAME = "service-name";
		public static final String STR_BOOT_OPTION_SERVICE_CLASS = "service-class";
		public static final String STR_BOOT_OPTION_SERVICE_COMMAND = "service-command";
		public static final String STR_BOOT_OPTION_HOST = "host";
		public static final String STR_BOOT_OPTION_PORT = "port";
		public static final String STR_BOOT_OPTION_GUI = "gui";
		public static final String STR_BOOT_OPTION_CONF = "conf";
		
		// SERVICE-NAME OPTIONS CONFIGURATION
		public static final String STR_SERVICE_NAME_RMI_CLIENT = "rmiclient";
		public static final String STR_SERVICE_NAME_RMI_SERVER = "rmiserver";
		public static final String STR_SERVICE_NAME_SOCKET_CLIENT = "socketclient";
		public static final String STR_SERVICE_NAME_SOCKET_SERVER = "socketserver";
		public static final String STR_SERVICE_NAME_DATA_SERVER = "dataserver";

		// SERVICE-COMMAND OPTIONS CONFIGURATION
		public static final String STR_SERVICE_COMMAND_STARTUP = "startup";
		public static final String STR_SERVICE_COMMAND_SHUTDOWN = "shutdown";
		public static final String STR_SERVICE_COMMAND_STATUS = "status";

	}
	
	public class BootCommand{
		public static final String DEFAULT_CONFIG_FILE_NAME = "/com/abreqadhabra/nflight/common/boot/config/command.properties";

		
		public static final String KEY_COMMAND_SLEEP_TIME_1 ="nflight.system.command.sleeptime.1"; 
		public static final String KEY_COMMAND_SLEEP_TIME_2 ="nflight.system.command.sleeptime.2";
		
		public static final String KEY_COMMAND_OS ="nflight.system.command.os";
		public static final String KEY_COMMAND_OS_WINDOWS ="nflight.system.command.windows";
		public static final String KEY_COMMAND_OS_LINUX ="nflight.system.command.linux";

		public static final String STR_COMMAND_OS_WINDOWS = "windows";
		public static final String STR_COMMAND_OS_LINUX = "linux";
		public static final String STR_COMMAND_OS_DEFAULT = STR_COMMAND_OS_WINDOWS;
	}

	public static class RMIServer{
	    
	    public static final String STR_SERVICE_REGISTRY ="NFlight/RMIServer";
	    
	    public static final String KEY_JAVA_SECURITY_POLICY ="java.security.policy";
	    public static final String DEFAULT_POLICY_FILE_NAME = "com/abreqadhabra/nflight/common/boot/config/default.policy";
	   

	}


}
