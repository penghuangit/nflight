package com.abreqadhabra.nflight.common;

public class Env {

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

	public static final long THREADHELPER_MILLIS = 3000;

	public static String Charset = "UTF-8";
}
