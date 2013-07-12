package com.abreqadhabra.nflight.service.core;

public class Env {

	public static class Properties {

		public static class System {
			
			public static class Constants {
			
			}
				
			public static enum PropertyKey {
				
				JAVA_SECURITY_POLICY, 
				JAVA_RMI_SERVER_CODEBASE;
		
				private String lowercase = null; // property name in lowercase
				/**
				 * Returns this property's name in lowercase.
				 */
				public String toString() {
					if (lowercase == null) {
						lowercase = name().toLowerCase(java.util.Locale.US)
								.replace("_", ".");
					}
					return lowercase;
				}
			}
		}

		public static class Boot {

			public static class Constants {
				
				public static final String FILE_NAME_BOOT_PROPERTIES = "/com/abreqadhabra/nflight/service/core/boot/conf/boot.properties";
				public static final String FILE_NAME_BOOT_POLICY = "com/abreqadhabra/nflight/service/core/boot/conf/boot.policy";
			}

			public static enum PropertyKey {
				
				NFLIGHT_SERVICE_CORE_BOOT_OPTION_CONF, 
				NFLIGHT_SERVICE_CORE_BOOT_OPTION_VERSION, 
				NFLIGHT_SERVICE_CORE_BOOT_OPTION_SERVICE, 
				NFLIGHT_SERVICE_CORE_BOOT_OPTION_SERVICE_TYPE, 
				NFLIGHT_SERVICE_CORE_BOOT_OPTION_SERVICE_MODE, 
				NFLIGHT_SERVICE_CORE_BOOT_OPTION_SERVICE_NAME, 
				NFLIGHT_SERVICE_CORE_BOOT_OPTION_SERVICE_COMMAND, 
				NFLIGHT_SERVICE_CORE_BOOT_OPTION_SERVICE_GUI, 
				NFLIGHT_SERVICE_CORE_BOOT_OPTION_SERVICE_HOST, 
				NFLIGHT_SERVICE_CORE_BOOT_OPTION_SERVICE_PORT;
		
				private String lowercase = null; // property name in lowercase
				/**
				 * Returns this property's name in lowercase.
				 */
				public String toString() {
					if (lowercase == null) {
						lowercase = name().toLowerCase(java.util.Locale.US)
								.replace("_", ".");
					}
					return lowercase;
				}
			}

			public static enum Option {
				
				conf, 
				version, 
				service, 
				name, 
				type, 
				mode, 
				command, 
				gui, 
				host, 
				port;
			}
			
			public static enum ServiceType {
				
				rmi, 
				socket,
				oracle,
				derby;
			}		
			
			public static enum ServiceMode {
				
				unicast, 
				activatable,
				tcp,
				udp;
			}	
			
			public static enum ServiceName {
				
				client, 
				server;
			}		
			public static enum ServiceCommand {
				
				startup, 
				shutdown, 
				status;
			}		
		}
	
		public static class BootCommand {

			public static class Constants {

				public static final String FILE_NAME_BOOTCOMMAND_PROPERTIES = "/com/abreqadhabra/nflight/service/boot/conf/bootcommand.properties";
				public static final String BOOTCOMMAND_OS_DEFAULT = "windows";
			}

			public static enum PropertyKey {

				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_SLEEPTIME_1,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_SLEEPTIME_2,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_OS_DEFAULT,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_ACTIVATABLE_CLIENT_SHUTDOWN_LINUX,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_ACTIVATABLE_CLIENT_SHUTDOWN_WINDOWS,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_ACTIVATABLE_CLIENT_STARTUP_LINUX,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_ACTIVATABLE_CLIENT_STARTUP_WINDOWS,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_SHUTDOWN_LINUX,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_SHUTDOWN_WINDOWS,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_START_LINUX,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_ACTIVATABLE_RMID_START_WINDOWS,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_ACTIVATABLE_SERVER_SHUTDOWN_LINUX,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_ACTIVATABLE_SERVER_SHUTDOWN_WINDOWS,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_ACTIVATABLE_SERVER_STARTUP_LINUX,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_ACTIVATABLE_SERVER_STARTUP_WINDOWS,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_ACTIVATABLE_SERVER_STATUS_LINUX,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_ACTIVATABLE_SERVER_STATUS_WINDOWS,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_UNICAST_CLIENT_SHUTDOWN_LINUX,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_UNICAST_CLIENT_SHUTDOWN_WINDOWS,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_UNICAST_CLIENT_STARTUP_LINUX,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_UNICAST_CLIENT_STARTUP_WINDOWS,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_UNICAST_SERVER_SHUTDOWN_LINUX,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_UNICAST_SERVER_SHUTDOWN_WINDOWS,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_UNICAST_SERVER_STARTUP_LINUX,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_UNICAST_SERVER_STARTUP_WINDOWS,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_UNICAST_SERVER_STATUS_LINUX,
				NFLIGHT_SERVICE_CORE_BOOTCOMMAND_RMI_UNICAST_SERVER_STATUS_WINDOWS;
			
				private String lowercase = null; // property name in lowercase

				/**
				 * Returns this property's name in lowercase.
				 */
				public String toString() {
					if (lowercase == null) {
						lowercase = name().toLowerCase(java.util.Locale.US)
								.replace("_", ".");
					}
					return lowercase;
				}
			}
		}

		public static class Setup {

			public static class Constants {

				public static final String FILE_NAME_SETUP_PROPERTIES = "/com/abreqadhabra/nflight/service/rmi/server/servant/activation/conf/setup.properties";
				public static final String REGISTRY_BOUND_NAME_SUFFIX = "NFlight/";
			}

			public static enum PropertyKey {

				NFLIGHT_SERVICE_RMI_SERVER_SERVANT_ACTIVATION_SETUP_CODEBASE,
				NFLIGHT_SERVICE_RMI_SERVER_SERVANT_ACTIVATION_IMPL_CODEBASE,
				NFLIGHT_SERVICE_RMI_SERVER_SERVANT_ACTIVATION_IMPL_CLASS,
				NFLIGHT_SERVICE_RMI_SERVER_SERVANT_ACTIVATION_NAME,
				NFLIGHT_SERVICE_RMI_SERVER_SERVANT_ACTIVATION_FILE,
				NFLIGHT_SERVICE_RMI_SERVER_SERVANT_ACTIVATION_POLICY;
			
				private String lowercase = null; // property name in lowercase

				/**
				 * Returns this property's name in lowercase.
				 */
				public String toString() {
					if (lowercase == null) {
						lowercase = name().toLowerCase(java.util.Locale.US)
								.replace("_", ".");
					}
					return lowercase;
				}
			}
		}
	}
}