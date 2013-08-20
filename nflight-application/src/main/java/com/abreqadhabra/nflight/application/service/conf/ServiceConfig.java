package com.abreqadhabra.nflight.application.service.conf;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.abreqadhabra.nflight.application.conf.ApplicationConfig;

public interface ServiceConfig extends ApplicationConfig {

	static Path PATH_SERVICE_PROPERTIES = Paths
			.get("com/abreqadhabra/nflight/application/service/conf/service.properties");

	static enum ENUM_SERVICE_TYPE {
		network_blocking, network_nonblocking, network_async, network_unicast, network_multicast, rmi_unicast, rmi_activation;
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
