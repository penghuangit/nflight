package com.abreqadhabra.nflight.application.common.launcher;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import com.abreqadhabra.nflight.common.util.IOStream;

public interface Configure {

	static Class<Configure> THIS_CLAZZ = Configure.class;
	public static Path CODE_BASE_PATH = IOStream.getCodebasePath(THIS_CLAZZ.getName());

	// launcher
	public static Path FILE_BOOT_PROPERTIES = Paths
			.get("com/abreqadhabra/nflight/application/launcher/conf/boot.properties");

	public static Path FILE_THREAD_POOL_PROPERTIES = Paths
			.get("com/abreqadhabra/nflight/application/launcher/concurrent/executor/conf/threadpool.properties");

	// NET

	public static enum SERVICE_TYPE {

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

	static String LAUNCHER_THREAD_POOL_CORE_POOL_SIZE = "nflight.application.launcher.thread.pool.core.pool.size";
	static String LAUNCHER_THREAD_POOL_MAXIMUM_POOL_SIZE = "nflight.application.launcher.thread.pool.maximum.pool.size";
	static String LAUNCHER_THREAD_POOL_KEEP_ALIVE_TIME = "nflight.application.launcher.thread.pool.keep.alive.time";
	static String LAUNCHER_THREAD_POOL_QUEUE_CAPACITY = "nflight.application.launcher.thread.pool.queue.capacity";

	public static Path FILE_SERVICE_PROPERTIES = Paths
			.get("com/abreqadhabra/nflight/application/service/conf/service.properties");
	static Path FILE_CHANNEL_OPTION_PROPERTIES = Paths
			.get("com/abreqadhabra/nflight/application/service/conf/channel_option.properties");

	static String PREFIX_KEY_PROPERTIES_CHANNEL_OPTION = "nflight.socket.option.";

	// STREAM SERVICE - BLOCKING
	static String BLOCKING_RUNNING = "nflight.service.stream.blocking.running";
	static String BLOCKING_DEFAULT_PORT = "nflight.service.stream.blocking.bind.port.default";
	static String BLOCKING_BIND_BACKLOG = "nflight.service.stream.blocking.bind.backlog";
	static String BLOCKING_INCOMING_BUFFER_CAPACITY = "nflight.service.stream.blocking.buffer.incoming.capacity";
	static String BLOCKING_SERVICE_THREAD_POOL_NAME = "nflight.service.stream.blocking.thread.pool.name";
	static String BLOCKING_SERVICE_THREAD_POOL_MONITORING_DELAY_SECONDS = "nflight.service.stream.blocking.thread.pool.monitoring.delay.seconds";
	static String BLOCKING_SERVICE_THREAD_POOL_MONITORING = "nflight.service.stream.blocking.thread.pool.monitoring";

	// STREAM SERVICE - NON-BLOCKING
	static String NONBLOCKING_RUNNING = "nflight.service.stream.nonblocking.running";
	static String NONBLOCKING_DEFAULT_PORT = "nflight.service.stream.nonblocking.bind.port.default";
	static String NONBLOCKING_BIND_BACKLOG = "nflight.service.stream.nonblocking.bind.backlog";
	static String NONBLOCKING_INCOMING_BUFFER_CAPACITY = "nflight.service.stream.nonblocking.buffer.incoming.capacity";

	// STREAM SERVICE - ASYNC
	static String ASYNC_RUNNING = "nflight.service.stream.async.running";
	static String ASYNC_DEFAULT_PORT = "nflight.service.stream.async.bind.port.default";
	static String ASYNC_BIND_BACKLOG = "nflight.service.stream.async.bind.backlog";
	static String ASYNC_INCOMING_BUFFER_CAPACITY = "nflight.service.stream.async.buffer.incoming.capacity";
	static String ASYNC_SERVICE_THREAD_POOL_NAME = "nflight.service.stream.async.thread.pool.name";
	static String ASYNC_SERVICE_THREAD_POOL_MONITORING_DELAY_SECONDS = "nflight.service.stream.async.thread.pool.monitoring.delay.seconds";
	static String ASYNC_SERVICE_THREAD_POOL_MONITORING = "nflight.service.stream.async.thread.pool.monitoring";
	static String ASYNC_THREADPOOL_INITIALSIZE = "nflight.service.stream.async.init.threadpool.initialsize";
	
	// DATAGRAM SERVICE - UNICAST
	static String UNICAST_RUNNING = "nflight.service.datagram.unicast.running";
	static String UNICAST_DEFAULT_PORT = "nflight.service.datagram.unicast.bind.port.default";
	static String UNICAST_INCOMING_BUFFER_CAPACITY = "nflight.service.datagram.unicast.buffer.incoming.capacity";

	static String CHANNEL_OPTION_IP_MULTICAST_IF = PREFIX_KEY_PROPERTIES_CHANNEL_OPTION + "network.multicast.ip_multicast_if";

	// DATAGRAM SERVICE - MULTICAST
	static String MULTICAST_RUNNING = "nflight.service.datagram.multicast.running";
	static String MULTICAST_DEFAULT_PORT = "nflight.service.datagram.multicast.bind.port.default";
	static String MULTICAST_INCOMING_BUFFER_CAPACITY = "nflight.service.datagram.multicast.buffer.incoming.capacity";

	static String MULTICAST_GROUP_ADDRESS = "225.4.5.6";

	
	static String RMI_DEFAULT_PORT = "nflight.service.rmi.bind.port.default";
	// RMI SERVICE - UNICAST
	static String UNICAST_RMI_RUNNING = "nflight.service.rmi.unicast.running";
	static String UNICAST_RMI_BOUND_NAME = "nflight.service.rmi.unicast.bound.name";

	// RMI SERVICE - ACTIVATABLE
	static String ACTIVATABLE_RMI_BOUND_NAME = "nflight.service.rmi.activatable.bound.name";
	static String ACTIVATABLE_RMI_SYSTEM_COMMAND_RMID_START = "nflight.service.rmi.activatable.rmi.system.command.rmid.start";
	static String ACTIVATABLE_RMI_SYSTEM_COMMAND_RMID_STOP = "nflight.service.rmi.activatable.rmi.system.command.rmid.stop";
	static String ACTIVATABLE_RMI_RMID_DELAY_SECONDS="nflight.service.rmi.activatable.rmid.delay.seconds";
	
	
	
	
	public static String PREFIX_FILE_ACTIVATABLE = "file://";

	public static Path FILE_ACTIVATABLE_POLICY = Paths
			.get("com/abreqadhabra/nflight/application/service/rmi/conf/activatable.policy");

	public static Path FILE_RMID_POLICY = Paths
			.get("com/abreqadhabra/nflight/application/service/rmi/conf/rmid.policy");

	public static String ACTIVATABLE_RMI_DEFAULT_PORT = "nflight.service.rmi.activatable.bind.port.default";

	public static Path FILE_ACTIVATION = Paths
			.get("com/abreqadhabra/nflight/application/service/rmi/ActivatableRMIServantImpl.ser");

	public static Path FILE_SYSTEM_COMMAND_PROPERTIES = Paths
			.get("com/abreqadhabra/nflight/application/launcher/conf/system_command.properties");


	public static enum PROPERTIES_ACTIVATION {

		NFLIGHT_SERVICE_RMI_ACTIVATABLE_SETUP_CODEBASE, NFLIGHT_SERVICE_RMI_ACTIVATABLE_IMPL_CODEBASE, NFLIGHT_SERVICE_RMI_ACTIVATABLE_IMPL_CLASS, NFLIGHT_SERVICE_RMI_ACTIVATABLE_NAME, NFLIGHT_SERVICE_RMI_ACTIVATABLE_FILE, NFLIGHT_SERVICE_RMI_ACTIVATABLE_POLICY;

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

	// SYSTEM COMMAND
	public static String LAUNCHER_SYSTEM_COMMAND_SLEEPTIME_1 = "launcher.system.command.sleeptime.1";



	// Methods

	public String get(String key);

	public void set(String key, String value);

	Properties getProperties();

	public int getInt(String key);

	public boolean getBoolean(String key);
}
