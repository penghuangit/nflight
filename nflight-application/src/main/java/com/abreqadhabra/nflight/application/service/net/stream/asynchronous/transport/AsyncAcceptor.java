package com.abreqadhabra.nflight.application.service.net.stream.asynchronous.transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.Acceptor;
import com.abreqadhabra.nflight.application.service.net.ServerChannelFactory;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class AsyncAcceptor implements Runnable, Acceptor {
	private static Class<AsyncAcceptor> THIS_CLAZZ = AsyncAcceptor.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private ThreadPoolExecutor threadPool;
	private Configure configure;
	private AsynchronousServerSocketChannel channel;

	public AsyncAcceptor(InetSocketAddress endpoint,
			ThreadPoolExecutor threadPool, Configure configure)
			throws IOException, InterruptedException, ExecutionException {
		this.threadPool = threadPool;
		this.configure = configure;

		this.init(endpoint);
	}

	@Override
	public void init(InetSocketAddress endpoint) throws IOException,
			InterruptedException, ExecutionException {
		int initialSize = this.configure
				.getInt(Configure.ASYNC_THREADPOOL_INITIALSIZE);
		// maximum number of pending connections
		int backlog = this.configure.getInt(Configure.ASYNC_BIND_BACKLOG);
		this.channel = this.createServerChannelFactory()
				.createAsyncServerSocketChannel(this.threadPool, initialSize,
						endpoint, backlog);
	}

	@Override
	public ServerChannelFactory createServerChannelFactory() {
		return new ServerChannelFactory();
	}

	@Override
	public void run() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getSimpleName(),
					METHOD_NAME,
					"current thread is "
							+ LoggingHelper.getThreadName(Thread
									.currentThread()));
			this.channel.accept(null, new AsyncReceiveCompletionHandler(
					this.configure, this.channel));
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
