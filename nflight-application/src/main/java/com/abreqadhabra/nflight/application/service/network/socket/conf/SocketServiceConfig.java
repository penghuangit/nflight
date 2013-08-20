package com.abreqadhabra.nflight.application.service.network.socket.conf;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.abreqadhabra.nflight.application.service.conf.ServiceConfig;

public interface SocketServiceConfig extends ServiceConfig {

	
//	static enum KEY_ENUM_SOCKET_BLOCKING {
//		NFLIGHT_BOOT_OPTION_CONF, NFLIGHT_BOOT_OPTION_SERVICE, NFLIGHT_BOOT_OPTION_SERVICE_COMMAND, NFLIGHT_BOOT_OPTION_SERVICE_GUI, NFLIGHT_BOOT_OPTION_SERVICE_HOST, NFLIGHT_BOOT_OPTION_SERVICE_MAINCLASS, NFLIGHT_BOOT_OPTION_SERVICE_PORT, NFLIGHT_BOOT_OPTION_VERSION, UNKNOWN;
//		
//		private String lowercase = null; // property name in lowercase
//		
//		/**
//		 * Returns this property's name in lowercase.
//		 **/
//		@Override
//		public String toString() {
//			if (this.lowercase == null) {
//				this.lowercase = this.name().toLowerCase(java.util.Locale.US)
//						.replace("_", ".");
//			}
//			return this.lowercase;
//		}
//	}
//	
	static String KEY_INT_SOCKET_BLOCKING_DEFAULT_PORT = "nflight.service.stream.blocking.bind.port.default";
	static String KEY_INT_SOCKET_BLOCKING_BIND_BACKLOG = "nflight.service.stream.blocking.bind.backlog";
	static String KEY_INT_SOCKET_BLOCKING_INCOMING_BUFFER_CAPACITY = "nflight.service.stream.blocking.buffer.incoming.capacity";
	static String KEY_BOO_SOCKET_BLOCKING_RUNNING = "nflight.service.stream.blocking.running";
	static String KEY_INT_SOCKET_BLOCKING_SERVICE_THREAD_POOL_MONITORING = "nflight.service.stream.blocking.thread.pool.monitoring";
	static String KEY_INT_SOCKET_BLOCKING_SERVICE_THREAD_POOL_MONITORING_DELAY_SECONDS = "nflight.service.stream.blocking.thread.pool.monitoring.delay.seconds";
	static String KEY_INT_SOCKET_BLOCKING_SERVICE_THREAD_POOL_NAME = "nflight.service.stream.blocking.thread.pool.name";
	
	static String KEY_INT_SOCKET_NONBLOCKING_DEFAULT_PORT = "nflight.service.stream.nonblocking.bind.port.default";
	static String KEY_INT_SOCKET_NONBLOCKING_BIND_BACKLOG = "nflight.service.stream.nonblocking.bind.backlog";
	static String KEY_INT_SOCKET_NONBLOCKING_INCOMING_BUFFER_CAPACITY = "nflight.service.stream.nonblocking.buffer.incoming.capacity";
	static String KEY_BOO_SOCKET_NONBLOCKING_RUNNING = "nflight.service.stream.nonblocking.running";

	static String KEY_INT_SOCKET_ASYNC_BIND_BACKLOG = "nflight.service.stream.async.bind.backlog";
	static String KEY_INT_SOCKET_ASYNC_INCOMING_BUFFER_CAPACITY = "nflight.service.stream.async.buffer.incoming.capacity";
	static String KEY_BOO_SOCKET_ASYNC_RUNNING = "nflight.service.stream.async.running";
	static String KEY_BOO_SOCKET_ASYNC_SERVICE_THREAD_POOL_MONITORING = "nflight.service.stream.async.thread.pool.monitoring";
	static String KEY_INT_SOCKET_ASYNC_SERVICE_THREAD_POOL_MONITORING_DELAY_SECONDS = "nflight.service.stream.async.thread.pool.monitoring.delay.seconds";
	static String KEY_STR_SOCKET_ASYNC_SERVICE_THREAD_POOL_NAME = "nflight.service.stream.async.thread.pool.name";
	static String KEY_INT_SOCKET_ASYNC_THREADPOOL_INITIALSIZE = "nflight.service.stream.async.init.threadpool.initialsize";
	static String KEY_INT_SOCKET_ASYNC_DEFAULT_PORT = "nflight.service.stream.async.bind.port.default";
	
	static String KEY_INT_SOCKET_UNICAST_INCOMING_BUFFER_CAPACITY = "nflight.service.datagram.unicast.buffer.incoming.capacity";
	static String KEY_BOO_SOCKET_UNICAST_RUNNING = "nflight.service.datagram.unicast.running";
	static String KEY_INT_SOCKET_UNICAST_DEFAULT_PORT = "nflight.service.datagram.unicast.bind.port.default";
	
	static String KEY_INT_SOCKET_MULTICAST_INCOMING_BUFFER_CAPACITY = "nflight.service.datagram.multicast.buffer.incoming.capacity";
	static String KEY_BOO_SOCKET_MULTICAST_RUNNING = "nflight.service.datagram.multicast.running";
	static String KEY_STR_SOCKET_MULTICAST_GROUP_ADDRESS = "225.4.5.6";
	static String KEY_INT_SOCKET_MULTICAST_DEFAULT_PORT = "nflight.service.datagram.multicast.bind.port.default";
	
	static String KEY_STR_PREFIX_CHANNEL_OPTION = "nflight.socket.option.";
	static String KEY_INT_SOCKET_CHANNEL_OPTION_IP_MULTICAST_IF = KEY_STR_PREFIX_CHANNEL_OPTION
			+ "network.multicast.ip_multicast_if";
	
	static Path PATH_CHANNEL_OPTION_PROPERTIES = Paths
			.get("com/abreqadhabra/nflight/application/service/conf/channel_option.properties");
	
}
