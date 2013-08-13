package com.abreqadhabra.nflight.application.server.forkjoin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.ProfileImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ForkJoinServiceExecutor {
	private static Class<ForkJoinServiceExecutor> THIS_CLAZZ = ForkJoinServiceExecutor.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	/** Whether the fork/join pool is started in async mode. */
	public static boolean FORKJOIN_ASYNC_MODE = false;

	ForkJoinPool pool;

	public ForkJoinServiceExecutor() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				LoggingHelper.describe(THIS_CLAZZ));
	}

	public void execute()  {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// Check the number of available processors
		int processors = Runtime.getRuntime().availableProcessors();
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"No of processors: " + processors);

		// pool = new ForkJoinPool(processors,
		// ForkJoinPool.defaultForkJoinWorkerThreadFactory, null,
		// FORKJOIN_ASYNC_MODE);

		pool = new ForkJoinPool(processors);

		
		AsynchronousServerSocketChannel asyncSocketChannel = pool
				.invoke(new AcceptTask(ProfileImpl
						.getInetSocketAddress(null, 9999)));

		System.out.println(asyncSocketChannel.isOpen());

		System.out.println("ActiveThreadCount" + pool.getActiveThreadCount());
		System.out.println("PoolSize" + pool.getPoolSize());
	}


}