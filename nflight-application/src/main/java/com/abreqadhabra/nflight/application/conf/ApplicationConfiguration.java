package com.abreqadhabra.nflight.application.conf;

import java.nio.file.Path;

import com.abreqadhabra.nflight.common.util.IOStream;

public interface ApplicationConfiguration {
	static final Class<ApplicationConfiguration> THIS_CLAZZ = ApplicationConfiguration.class;
	static final Path PATH_APPLICATION_CODEBASE = IOStream
			.getCodebasePath(THIS_CLAZZ.getName());

	static Path PATH_APPLICATION_BOOT_POLICY = PATH_APPLICATION_CODEBASE
			.resolve("com/abreqadhabra/nflight/application/conf/boot.policy");

	static String STR_APPLICATION_BOOT_LAUNCHER_CLASS_MAIN = "com.abreqadhabra.nflight.application.common.launcher.ServiceLauncher";
	static final String STR_PREFIX_APPLICATION_BOOT_OPTION = "--";
	static final String STR_APPLICATION_BOOT_OPTION_CONF = STR_PREFIX_APPLICATION_BOOT_OPTION
			+ "conf";

	static enum ENUM_APPLICATION_BOOT_OPTION {
		command, conf, gui, host, mainclass, mode, name, port, service, type, UNKNOWN, version;
	}

	static enum ENUM_APPLICATION_BOOT_PROPERTIES {
		NFLIGHT_BOOT_OPTION_CONF, NFLIGHT_BOOT_OPTION_SERVICE, NFLIGHT_BOOT_OPTION_SERVICE_COMMAND, NFLIGHT_BOOT_OPTION_SERVICE_GUI, NFLIGHT_BOOT_OPTION_SERVICE_HOST, NFLIGHT_BOOT_OPTION_SERVICE_MAINCLASS, NFLIGHT_BOOT_OPTION_SERVICE_PORT, NFLIGHT_BOOT_OPTION_VERSION, UNKNOWN;
		private String lowercase = null; // property name in lowercase
		/**
		 * Returns this property's name in lowercase.
		 **/
		@Override
		public String toString() {
			if (this.lowercase == null) {
				this.lowercase = this.name().toLowerCase(java.util.Locale.US)
						.replace("_", ".");
			}
			return this.lowercase;
		}
	}

	static enum ENUM_APPLICATION_SYSTEM_PROPERTIES {
		JAVA_RMI_SERVER_CODEBASE, JAVA_SECURITY_POLICY, SUN_JNU_ENCODING, UNKNOWN;
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
