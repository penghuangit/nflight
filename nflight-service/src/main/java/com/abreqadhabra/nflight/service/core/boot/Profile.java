package com.abreqadhabra.nflight.service.core.boot;


/**
 * This class allows retrieving configuration-dependent classes.
 * 
 */
public abstract class Profile {

	public static final String FILE_BOOT_PROPERTIES = "/com/abreqadhabra/nflight/service/core/boot/conf/boot.properties";
	public static final String FILE_BOOT_POLICY = "com/abreqadhabra/nflight/service/core/boot/conf/boot.policy";

	public static enum PROPERTIES_BOOT {

		NFLIGHT_BOOT_OPTION_CONF, NFLIGHT_BOOT_OPTION_VERSION, NFLIGHT_BOOT_OPTION_SERVICE, NFLIGHT_BOOT_OPTION_SERVICE_COMMAND, NFLIGHT_BOOT_OPTION_SERVICE_MAINCLASS, NFLIGHT_BOOT_OPTION_SERVICE_GUI, NFLIGHT_BOOT_OPTION_SERVICE_HOST, NFLIGHT_BOOT_OPTION_SERVICE_PORT, UNKNOWN;

		private String lowercase = null; // property name in lowercase

		/**
		 * Returns this property's name in lowercase.
		 */
		@Override
		public String toString() {
			if (lowercase == null) {
				lowercase = name().toLowerCase(java.util.Locale.US).replace(
						"_", ".");
			}
			return lowercase;
		}
	}

	public static final String BOOT_OPTION_PREFIX = "--";

	public static enum BOOT_OPTION {

		conf, version, service, name, type, mode, command, mainclass, gui, host, port, UNKNOWN;
	}

	public static enum BOOT_OPTION_SERVICE_MAINCLASS {

		com_abreqadhabra_nflight_service_rmi_server_NFlightRMIServerImpl,
		com_abreqadhabra_nflight_service_socket_server_NFlightSocketServerImpl,
		 UNKNOWN;

		private String name = null; // property name

		/**
		 * Returns this property's name
		 */
		@Override
		public String toString() {
			if (name == null) {
				name = name().replace("_", ".");
			}
			return name;
		}

		public static boolean contains(String name) {
			for (BOOT_OPTION_SERVICE_MAINCLASS className : values()) {
				if (className.name().equals(name.replace(".", "_"))) {
					return true;
				}
			}
			return false;
		}

		public static BOOT_OPTION_SERVICE_MAINCLASS getValue(String value) {
			try {
				return valueOf(value.replace(".", "_"));
			} catch (Exception e) {
				return UNKNOWN;
			}
		}
	}

	public static enum BOOT_OPTION_SERVICE_COMMAND {

		startup, shutdown, status, UNKNOWN;

		public static BOOT_OPTION_SERVICE_COMMAND getValue(String value) {
			try {
				return valueOf(value.replace(".", "_"));
			} catch (Exception e) {
				return UNKNOWN;
			}
		}
	}

	public static final String FILE_BOOTCOMMAND_PROPERTIES = "/com/abreqadhabra/nflight/service/core/boot/conf/bootcommand.properties";
	public static final BOOTCOMMAND_OS BOOTCOMMAND_OS_DEFAULT = BOOTCOMMAND_OS.windows;
	public static final int BOOTCOMMAND_SLEEPTIME_1 = 2000;
	public static final int BOOTCOMMAND_SLEEPTIME_2 = 10000;

	public static enum BOOTCOMMAND_OS {
		windows, linux;
	}

	public static enum PROPERTIES_BOOTCOMMAND {
		NFLIGHT_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_START_WINDOWS, NFLIGHT_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_STOP_WINDOWS, UNKNOWN;

		private String lowercase = null; // property name

		/**
		 * Returns this property's name in lowercase.
		 */
		@Override
		public String toString() {
			if (lowercase == null) {
				lowercase = name().toLowerCase(java.util.Locale.US).replace(
						"_", ".");
			}
			return lowercase;
		}
	}

	public static final String FILE_ACTIVATION_PROPERTIES = "/com/abreqadhabra/nflight/service/rmi/server/servant/activation/conf/activation.properties";

	public static final String FILE_ACTIVATION_POLICY = "com/abreqadhabra/nflight/service/rmi/server/servant/activation/conf/activation.policy";
	public static final String FILE_RMID_POLICY = "/com/abreqadhabra/nflight/service/rmi/server/servant/activation/conf/rmid.policy";

	public static final String ACTIVATION_FILE_PREFIX = "file:";

	public static enum PROPERTIES_ACTIVATION {

		NFLIGHT_SERVANT_ACTIVATION_SETUP_CODEBASE, NFLIGHT_SERVANT_ACTIVATION_IMPL_CODEBASE, NFLIGHT_SERVANT_ACTIVATION_IMPL_CLASS, NFLIGHT_SERVANT_ACTIVATION_NAME, NFLIGHT_SERVANT_ACTIVATION_FILE, NFLIGHT_SERVANT_ACTIVATION_POLICY;

		private String lowercase = null; // property name in lowercase

		/**
		 * Returns this property's name in lowercase.
		 */
		public String toString() {
			if (lowercase == null) {
				lowercase = name().toLowerCase(java.util.Locale.US).replace(
						"_", ".");
			}
			return lowercase;
		}
	}
}
