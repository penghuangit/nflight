package com.abreqadhabra.nflight.application.service.network.socket.conf;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.abreqadhabra.nflight.application.service.conf.ServiceConfiguration;

public interface SocketServiceConfiguration extends ServiceConfiguration {

	static String ASYNC_BIND_BACKLOG = "nflight.service.stream.async.bind.backlog";
	static String ASYNC_INCOMING_BUFFER_CAPACITY = "nflight.service.stream.async.buffer.incoming.capacity";
	static String ASYNC_RUNNING = "nflight.service.stream.async.running";
	static String ASYNC_SERVICE_THREAD_POOL_MONITORING = "nflight.service.stream.async.thread.pool.monitoring";
	static String ASYNC_SERVICE_THREAD_POOL_MONITORING_DELAY_SECONDS = "nflight.service.stream.async.thread.pool.monitoring.delay.seconds";
	static String ASYNC_SERVICE_THREAD_POOL_NAME = "nflight.service.stream.async.thread.pool.name";
	static String ASYNC_THREADPOOL_INITIALSIZE = "nflight.service.stream.async.init.threadpool.initialsize";
	static String BLOCKING_BIND_BACKLOG = "nflight.service.stream.blocking.bind.backlog";

	static String BLOCKING_INCOMING_BUFFER_CAPACITY = "nflight.service.stream.blocking.buffer.incoming.capacity";
	static String BLOCKING_RUNNING = "nflight.service.stream.blocking.running";
	static String BLOCKING_SERVICE_THREAD_POOL_MONITORING = "nflight.service.stream.blocking.thread.pool.monitoring";

	static String BLOCKING_SERVICE_THREAD_POOL_MONITORING_DELAY_SECONDS = "nflight.service.stream.blocking.thread.pool.monitoring.delay.seconds";
	static String BLOCKING_SERVICE_THREAD_POOL_NAME = "nflight.service.stream.blocking.thread.pool.name";
	static String PREFIX_KEY_PROPERTIES_CHANNEL_OPTION = "nflight.socket.option.";
	static String CHANNEL_OPTION_IP_MULTICAST_IF = PREFIX_KEY_PROPERTIES_CHANNEL_OPTION
			+ "network.multicast.ip_multicast_if";
	static Path FILE_CHANNEL_OPTION_PROPERTIES = Paths
			.get("com/abreqadhabra/nflight/application/service/conf/channel_option.properties");
	static String MULTICAST_INCOMING_BUFFER_CAPACITY = "nflight.service.datagram.multicast.buffer.incoming.capacity";
	static String MULTICAST_RUNNING = "nflight.service.datagram.multicast.running";
	static String NONBLOCKING_BIND_BACKLOG = "nflight.service.stream.nonblocking.bind.backlog";

	static String NONBLOCKING_INCOMING_BUFFER_CAPACITY = "nflight.service.stream.nonblocking.buffer.incoming.capacity";
	static String NONBLOCKING_RUNNING = "nflight.service.stream.nonblocking.running";

	

	static String UNICAST_INCOMING_BUFFER_CAPACITY = "nflight.service.datagram.unicast.buffer.incoming.capacity";

	static String UNICAST_RUNNING = "nflight.service.datagram.unicast.running";


	static String BLOCKING_DEFAULT_PORT = "nflight.service.stream.blocking.bind.port.default";
	static String NONBLOCKING_DEFAULT_PORT = "nflight.service.stream.nonblocking.bind.port.default";
	static String ASYNC_DEFAULT_PORT = "nflight.service.stream.async.bind.port.default";
	static String UNICAST_DEFAULT_PORT = "nflight.service.datagram.unicast.bind.port.default";
	
	
	static String MULTICAST_GROUP_ADDRESS = "225.4.5.6";
	static String MULTICAST_DEFAULT_PORT = "nflight.service.datagram.multicast.bind.port.default";
}
