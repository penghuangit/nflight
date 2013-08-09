package com.abreqadhabra.nflight.application;

import java.nio.file.Path;

import com.abreqadhabra.nflight.application.launcher.Configure;

public final class Configuration {
	public static final String LAUNCHER_CLASS = "com.abreqadhabra.nflight.application.launcher.ServiceLauncher";

	public static final Path FILE_BOOT_POLICY = Configure.CODE_BASE_PATH
			.resolve("com/abreqadhabra/nflight/application/launcher/conf/boot.policy");
	public static final String BOOT_OPTION_PREFIX = "--";
	public static final Object BOOT_OPTION_CONF = BOOT_OPTION_PREFIX + "conf";
	public static String Charset = "UTF-8";

	public static enum SERVICE_NAME {
		unicast, activatable, rmid, stream, datagram, multicast;
	}
	public static enum PROPERTIES_SYSTEM {

		JAVA_SECURITY_POLICY, JAVA_RMI_SERVER_CODEBASE, SUN_JNU_ENCODING, UNKNOWN;

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

	public static enum BOOT_OPTION {

		conf, version, service, name, type, mode, command, mainclass, gui, host, port, UNKNOWN;
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

}