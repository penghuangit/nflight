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

	static final Path FILE_SOCKET_OPTION_PROPERTIES = CODE_BASE_PATH
			.resolve("com/abreqadhabra/nflight/application/server/aio/conf/socketoption.properties");

	static final String PREFIX_KEY_PROPERTIES_SOCKET_OPTION = "nflight.socketoption.";;
	
	public String get(String key);

	public void set(String key, String value);

	Hashtable<Object, Object> all();
	
	//ASYNC SERVER
	static final String ASYNC_THREADPOOL_INITIALSIZE ="nflight.server.async.init.threadpool.initialsize";
	static final String ASYNC_BIND_BACKLOG="nflight.server.async.bind.backlog";
	static final String ACCEPTOR_READ_BUFFER_CAPACITY = "nflight.server.async.acceptor.bytebuffer.capacity";

	public int getInt(String asyncBindBacklog);

}
