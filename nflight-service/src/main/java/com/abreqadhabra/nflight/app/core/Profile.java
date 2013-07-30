package com.abreqadhabra.nflight.app.core;

/**
 * This class allows retrieving configuration-dependent classes.
 * 
 */
public abstract class Profile {

	public static enum BOOT_OPTION {

		conf, version, service, name, type, mode, command, mainclass, gui, host, port, UNKNOWN;
	}

	public static enum BOOT_OPTION_SERVICE_COMMAND {

		startup, shutdown, status, UNKNOWN;

		public static BOOT_OPTION_SERVICE_COMMAND getValue(final String value) {
			try {
				return BOOT_OPTION_SERVICE_COMMAND.valueOf(value.replace(".",
						"_"));
			} catch (final Exception e) {
				return UNKNOWN;
			}
		}
	}

	public static enum BOOT_OPTION_SERVICE_MAINCLASS {

		com_abreqadhabra_nflight_app_server_ServerTest,
		// com_abreqadhabra_nflight_service_rmi_server_NFServerRMIImpl,
		// com_abreqadhabra_nflight_service_socket_server_NFServerSocketImpl,
		//
		// com_abreqadhabra_nflight_service_rmi_server_unicast_UnicastRMIServerImpl,
		// com_abreqadhabra_nflight_service_rmi_server_activatable_ActivatableRMIServerImpl,
		UNKNOWN;

		public static boolean contains(final String name) {
			for (final BOOT_OPTION_SERVICE_MAINCLASS className : BOOT_OPTION_SERVICE_MAINCLASS
					.values()) {
				if (className.name().equals(name.replace(".", "_"))) {
					return true;
				}
			}
			return false;
		}

		public static BOOT_OPTION_SERVICE_MAINCLASS getValue(final String value) {
			try {
				return BOOT_OPTION_SERVICE_MAINCLASS.valueOf(value.replace(".",
						"_"));
			} catch (final Exception e) {
				return UNKNOWN;
			}
		}

		private String name = null; // property name

		/**
		 * Returns this property's name
		 */
		@Override
		public String toString() {
			if (this.name == null) {
				this.name = this.name().replace("_", ".");
			}
			return this.name;
		}
	}

	public static enum BOOTCOMMAND_OS {
		windows, linux;
	}

	public static enum PROPERTIES_ACTIVATION {

		NFLIGHT_SERVANT_ACTIVATION_SETUP_CODEBASE, NFLIGHT_SERVANT_ACTIVATION_IMPL_CODEBASE, NFLIGHT_SERVANT_ACTIVATION_IMPL_CLASS, NFLIGHT_SERVANT_ACTIVATION_NAME, NFLIGHT_SERVANT_ACTIVATION_FILE, NFLIGHT_SERVANT_ACTIVATION_POLICY;

		private String lowercase = null; // property name in lowercase

		/**
		 * Returns this property's name in lowercase.
		 */
		@Override
		public String toString() {
			if (this.lowercase == null) {
				this.lowercase = this.name().toLowerCase(java.util.Locale.US)
						.replace("_", ".");
			}
			return this.lowercase;
		}
	}

	public static enum PROPERTIES_BOOT {

		NFLIGHT_BOOT_OPTION_CONF, NFLIGHT_BOOT_OPTION_VERSION, NFLIGHT_BOOT_OPTION_SERVICE, NFLIGHT_BOOT_OPTION_SERVICE_COMMAND, NFLIGHT_BOOT_OPTION_SERVICE_MAINCLASS, NFLIGHT_BOOT_OPTION_SERVICE_GUI, NFLIGHT_BOOT_OPTION_SERVICE_HOST, NFLIGHT_BOOT_OPTION_SERVICE_PORT, UNKNOWN;

		private String lowercase = null; // property name in lowercase

		/**
		 * Returns this property's name in lowercase.
		 */
		@Override
		public String toString() {
			if (this.lowercase == null) {
				this.lowercase = this.name().toLowerCase(java.util.Locale.US)
						.replace("_", ".");
			}
			return this.lowercase;
		}
	}

	public static enum PROPERTIES_BOOTCOMMAND {
		NFLIGHT_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_START_WINDOWS, NFLIGHT_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_STOP_WINDOWS, UNKNOWN;

		private String lowercase = null; // property name

		/**
		 * Returns this property's name in lowercase.
		 */
		@Override
		public String toString() {
			if (this.lowercase == null) {
				this.lowercase = this.name().toLowerCase(java.util.Locale.US)
						.replace("_", ".");
			}
			return this.lowercase;
		}
	}

	public static enum RMI_SERVICE {
		unicast, activatable, rmid;
	}

	public static enum SERVICE_NAME {
		unicast, activatable, rmid, stream, datagram, multicast;
	}

	public static enum SOCKET_SERVICE {
		stream, datagram, multicast;
	}

	public static final String FILE_BOOT_PROPERTIES = "com/abreqadhabra/nflight/app/core/conf/boot.properties";

	public static final String FILE_BOOT_POLICY = "com/abreqadhabra/nflight/app/core/conf/boot.policy";

	public static final String BOOT_OPTION_PREFIX = "--";

	public static final String FILE_BOOTCOMMAND_PROPERTIES = "com/abreqadhabra/nflight/app/core/conf/bootcommand.properties";

	public static final BOOTCOMMAND_OS BOOTCOMMAND_OS_DEFAULT = BOOTCOMMAND_OS.windows;

	public static final int BOOTCOMMAND_SLEEPTIME_1 = 2000;
	public static final int BOOTCOMMAND_SLEEPTIME_2 = 10000;
	public static final String FILE_ACTIVATION_PROPERTIES = "com/abreqadhabra/nflight/app/acceptor/service/rmi/conf/activatable.properties";

	public static final String FILE_ACTIVATION_POLICY = "com/abreqadhabra/nflight/app/acceptor/service/rmi/conf/activatable.policy";

	public static final String FILE_RMID_POLICY = "com/abreqadhabra/nflight/app/acceptor/service/rmi/conf/rmid.policy";

	public static final String ACTIVATION_FILE_PREFIX = "file://";

	public static final Object BOOT_OPTION_CONF = "--conf";

}
