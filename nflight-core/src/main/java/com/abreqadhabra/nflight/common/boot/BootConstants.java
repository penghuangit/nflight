package com.abreqadhabra.nflight.common.boot;


public class BootConstants {

	public static final String DEFAULT_PROPERTIES_FILE_NAME = "/com/abreqadhabra/nflight/core/config/boot.properties";

	
	public class Options{
		
		// BOOT OPTIONS CONFIGURATION
		public static final String STR_BOOT_OPTION_SERVICE = "service";
		public static final String STR_BOOT_OPTION_SERVICE_NAME  = "service-name";
		public static final String STR_BOOT_OPTION_SERVICE_CLASS = "service-class";
		public static final String STR_BOOT_OPTION_SERVICE_COMMAND = "service-command";
		public static final String STR_BOOT_OPTION_HOST = "host";
		public static final String STR_BOOT_OPTION_PORT = "port";
		public static final String STR_BOOT_OPTION_GUI = "gui";
		public static final String STR_BOOT_OPTION_CONF ="conf";

		
		// SERVICE-NAME OPTIONS CONFIGURATION
		public static final String STR_SERVICE_NAME_RMI_CLIENT = "rmiclient";
		public static final String STR_SERVICE_NAME_RMI_SERVER = "rmiserver";
		public static final String STR_SERVICE_NAME_SOCKET_CLIENT = "socketclient";
		public static final String STR_SERVICE_NAME_SOCKET_SERVER = "socketserver";
		public static final String STR_SERVICE_NAME_DATA_SERVER = "dataserver";

		
	}


}
