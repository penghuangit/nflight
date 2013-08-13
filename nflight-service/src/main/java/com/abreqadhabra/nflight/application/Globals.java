package com.abreqadhabra.nflight.application;

import java.nio.file.Path;

import com.abreqadhabra.nflight.common.util.IOStream;

public class Globals {
	private static Class<Globals> THIS_CLAZZ = Globals.class;
	public static String LAUNCHER_CLASS = "com.abreqadhabra.nflight.application.launcher.ServiceLauncher";
	public static Path CODE_BASE_PATH = IOStream
			.getCodebasePath(THIS_CLAZZ.getName());
	public static Path FILE_BOOT_PROPERTIES = CODE_BASE_PATH
			.resolve("com/abreqadhabra/nflight/application/launcher/conf/boot.properties");
	public static Path FILE_BOOT_POLICY = CODE_BASE_PATH
			.resolve("com/abreqadhabra/nflight/application/launcher/conf/boot.policy");
	public static String BOOT_OPTION_PREFIX = "--";
	public static Object BOOT_OPTION_CONF = BOOT_OPTION_PREFIX + "conf";
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
}