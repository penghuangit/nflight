package com.abreqadhabra.nflight.service.core;

public class Env {

	public class Boot {
		public static final String FILE_BOOT_CONFIG_DEFAULT = "/com/abreqadhabra/nflight/service/core/boot/conf/boot.properties";
		public static final String FILE_BOOT_POLICY_DEFAULT = "com/abreqadhabra/nflight/service/core/boot/conf/default.policy";

		public static final String KEY_JAVA_SECURITY_POLICY = "java.security.policy";

		// BOOT OPTIONS CONFIGURATION
		public static final String KEY_BOOT_OPTION_SERVICE = "nflight.service.core.boot.option.service";
		public static final String KEY_BOOT_OPTION_SERVICE_NAME = "nflight.service.core.boot.option.service.name";
		public static final String KEY_BOOT_OPTION_SERVICE_CLASS = "nflight.service.core.boot.option.service.class";
		public static final String KEY_BOOT_OPTION_SERVICE_COMMAND = "nflight.service.core.boot.option.service.command";
		public static final String KEY_BOOT_OPTION_HOST = "nflight.service.core.boot.option.host";
		public static final String KEY_BOOT_OPTION_PORT = "nflight.service.core.boot.option.port";
		public static final String KEY_BOOT_OPTION_GUI = "nflight.service.core.boot.option.gui";
		public static final String KEY_BOOT_OPTION_CONF = "nflight.service.core.boot.option.conf";

		public static final String VAL_BOOT_OPTION_SERVICE = "service";
		public static final String VAL_BOOT_OPTION_SERVICE_NAME = "service-name";
		public static final String VAL_BOOT_OPTION_SERVICE_CLASS = "service-class";
		public static final String VAL_BOOT_OPTION_SERVICE_COMMAND = "service-command";
		public static final String VAL_BOOT_OPTION_HOST = "host";
		public static final String VAL_BOOT_OPTION_PORT = "port";
		public static final String VAL_BOOT_OPTION_GUI = "gui";
		public static final String VAL_BOOT_OPTION_CONF = "conf";

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

	public class BootCommand {
		public static final String FILE_COMMAND_CONFIG_DEFAULT = "/com/abreqadhabra/nflight/service/boot/conf/command.properties";

		public static final String KEY_COMMAND_SLEEP_TIME_1 = "nflight.service.core.boot.command.sleeptime.1";
		public static final String KEY_COMMAND_SLEEP_TIME_2 = "nflight.service.core.boot.command.sleeptime.2";

		public static final String KEY_COMMAND_OS = "nflight.service.core.command.os";
		public static final String KEY_COMMAND_OS_DEFAULT = "nflight.service.core.boot.command.os.default";
		public static final String KEY_COMMAND_OS_WINDOWS = "nflight.service.core.boot.command.os.windows";
		public static final String KEY_COMMAND_OS_LINUX = "nflight.service.core.boot.command.os.linux";

		public static final String VAL_COMMAND_OS_DEFAULT = KEY_COMMAND_OS_WINDOWS;
	}

	public class RMI {

		public static final String FILE_ACTIVATION_SETUP_DEFAULT = "/com/abreqadhabra/nflight/service/rmi/server/servant/activation/conf/setup.properties";

		public static final String KEY_JAVA_RMI_SERVER_CODEBASE = "java.rmi.server.codebase";
		public static final String KEY_RMI_ACTIVATION_SETUP_CODEBASE = "nflight.service.rmi.server.servant.activation.setup.codebase";
		public static final String KEY_RMI_ACTIVATION_IMPL_CODEBASE = "nflight.service.rmi.server.servant.activation.impl.codebase";
		public static final String KEY_RMI_ACTIVATION_NAME = "nflight.service.rmi.server.servant.activation.name";
		public static final String KEY_RMI_ACTIVATION_POLICY = "nflight.service.rmi.server.servant.activation.policy";
		public static final String KEY_RMI_ACTIVATION_FILE = "nflight.service.rmi.server.servant.activation.file";
		public static final String KEY_RMI_ACTIVATION_IMPL_CLASSES = "nflight.service.rmi.server.servant.activation.impl.class";

		public static final String STR_BOUND_NAME_SUFFIX = "NFlight/";

	}

}
