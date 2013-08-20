package com.abreqadhabra.nflight.application.common.launcher.conf;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.abreqadhabra.nflight.application.conf.ApplicationConfig;

public interface LauncherConfiguration extends ApplicationConfig {

	static Path FILE_BOOT_PROPERTIES = Paths
			.get("com/abreqadhabra/nflight/application/launcher/conf/boot.properties");
	static String LAUNCHER_SYSTEM_COMMAND_SLEEPTIME_1 = "launcher.system.command.sleeptime.1";
	static Path FILE_SYSTEM_COMMAND_PROPERTIES = Paths
			.get("com/abreqadhabra/nflight/application/launcher/conf/system_command.properties");
	static Path FILE_THREAD_POOL_PROPERTIES = Paths
			.get("com/abreqadhabra/nflight/application/launcher/concurrent/executor/conf/threadpool.properties");
	static String LAUNCHER_THREAD_POOL_CORE_POOL_SIZE = "nflight.application.launcher.thread.pool.core.pool.size";
	static String LAUNCHER_THREAD_POOL_MAXIMUM_POOL_SIZE = "nflight.application.launcher.thread.pool.maximum.pool.size";
	static String LAUNCHER_THREAD_POOL_KEEP_ALIVE_TIME = "nflight.application.launcher.thread.pool.keep.alive.time";
	static String LAUNCHER_THREAD_POOL_QUEUE_CAPACITY = "nflight.application.launcher.thread.pool.queue.capacity";
}
