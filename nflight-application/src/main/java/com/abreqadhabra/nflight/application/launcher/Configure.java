package com.abreqadhabra.nflight.application.launcher;

import java.nio.file.Path;
import java.util.Hashtable;

import com.abreqadhabra.nflight.common.util.IOStream;

public interface Configure {
	static final Class<Configure> THIS_CLAZZ = Configure.class;

	public static final Path CODE_BASE_PATH = IOStream
			.getCodebasePath(THIS_CLAZZ.getName());

	public static final Path FILE_BOOT_PROPERTIES = CODE_BASE_PATH
			.resolve("com/abreqadhabra/nflight/application/launcher/conf/boot.properties");

	public static final Path FILE_THREAD_POOL_PROPERTIES = CODE_BASE_PATH
			.resolve("com/abreqadhabra/nflight/application/launcher/concurrent/executor/conf/threadpool.properties");

	public static final Path FILE_SOCKET_SERVER_PROPERTIES = CODE_BASE_PATH
			.resolve("com/abreqadhabra/nflight/application/server/aio/conf/socketserver.properties");

	static final Path FILE_CHANNEL_OPTION_PROPERTIES = CODE_BASE_PATH
			.resolve("com/abreqadhabra/nflight/application/server/aio/conf/socketoption.properties");

	static final String PREFIX_KEY_PROPERTIES_CHANNEL_OPTION = "nflight.socketoption.";;
	static final String CHANNEL_OPTION_IP_MULTICAST_IF = "nflight.socketoption.multicast.ip_multicast_if";
	
	public String get(String key);

	public void set(String key, String value);

	Hashtable<Object, Object> getAll();

	public int getInt(String asyncBindBacklog);
	
	// STREAM SERVICE - BLOCKING
	static final String BLOCKING_DEFAULT_PORT = "nflight.service.stream.blocking.bind.port.default";
	static final String BLOCKING_BIND_BACKLOG = "nflight.service.stream.blocking.bind.backlog";
	static final String BLOCKING_INCOMING_BUFFER_CAPACITY = "nflight.service.stream.blocking.buffer.incoming.capacity";

	// STREAM SERVICE - NON-BLOCKING
	static final String NONBLOCKING_DEFAULT_PORT = "nflight.service.stream.nonblocking.bind.port.default";
	static final String NONBLOCKING_BIND_BACKLOG = "nflight.service.stream.nonblocking.bind.backlog";
	static final String NONBLOCKING_INCOMING_BUFFER_CAPACITY = "nflight.service.stream.nonblocking.buffer.incoming.capacity";

	// STREAM SERVICE - ASYNC
	static final String ASYNC_DEFAULT_PORT = "nflight.service.stream.async.bind.port.default";
	static final String ASYNC_THREADPOOL_INITIALSIZE = "nflight.service.stream.async.init.threadpool.initialsize";
	static final String ASYNC_BIND_BACKLOG = "nflight.service.stream.async.bind.backlog";
	static final String ASYNC_INCOMING_BUFFER_CAPACITY = "nflight.service.stream.async.buffer.incoming.capacity";

	// DATAGRAM SERVICE - UNICAST
	static final String UNICAST_DEFAULT_PORT = "nflight.service.datagram.unicast.bind.port.default";
	static final String UNICAST_INCOMING_BUFFER_CAPACITY = "nflight.service.datagram.unicast.buffer.incoming.capacity";

	// DATAGRAM SERVICE - MULTICAST
	static final String MULTICAST_DEFAULT_PORT = "nflight.service.datagram.multicast.bind.port.default";
	static final String MULTICAST_INCOMING_BUFFER_CAPACITY = "nflight.service.datagram.multicast.buffer.incoming.capacity";

	static final String MULTICAST_GROUP_ADDRESS = "225.4.5.6";


	public static enum STREAM_SERVICE_TYPE {

		blocking, nonblocking, async, unicast, multicast, UNKNOWN;
	}


}
