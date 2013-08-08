package com.abreqadhabra.nflight.application.service.net.stream.asynchronous;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.server.net.async.handler.AsyncServerAcceptHandler;
import com.abreqadhabra.nflight.application.service.net.INetworkService;
import com.abreqadhabra.nflight.application.service.net.NetworkServiceHelper;
import com.abreqadhabra.nflight.application.service.net.stream.nonblocking.NonBlockingServerSessionImpl;
import com.abreqadhabra.nflight.application.service.net.stream.nonblocking.ServerSession;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class AsyncNetworkServiceImpl implements INetworkService, Runnable {
	private static final Class<AsyncNetworkServiceImpl> THIS_CLAZZ = AsyncNetworkServiceImpl.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private final Configure configure;
	private final InetSocketAddress socketAddress;
	private boolean isRunning;
	private ExecutorService threadPoolExecutor;

	public AsyncNetworkServiceImpl(final Configure configure,
			final InetSocketAddress socketAddress,
			final ThreadPoolExecutor threadPoolExecutor) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		this.configure = configure;
		this.socketAddress = socketAddress;
		this.threadPoolExecutor = threadPoolExecutor;

		LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"instantiated an idle instance of " + CLAZZ_NAME);
	}

	@Override
	public void run() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		if (LOGGER.isLoggable(Level.FINER)) {
			final String currentThreadName = Thread.currentThread().getName();
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"current thread is " + currentThreadName);
		}

		AsynchronousChannelGroup threadGroup = null;
		// create asynchronous server-socket channel bound to the thread Group
		try {
			int initialSize = configure
					.getInt(Configure.ASYNC_THREADPOOL_INITIALSIZE);
			threadGroup = AsynchronousChannelGroup.withCachedThreadPool(
					threadPoolExecutor, initialSize);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// create a new server-socket channel & selector
		try (AsynchronousServerSocketChannel asyncServerSocketChannel = AsynchronousServerSocketChannel
				.open(threadGroup);) {

			this.isRunning = true;

			if (asyncServerSocketChannel.isOpen()) {
				// set some options
				NetworkServiceHelper.setChannelOption(asyncServerSocketChannel);

				// maximum number of pending connections
				int backlog = configure.getInt(Configure.ASYNC_BIND_BACKLOG);
				// bind the server-socket channel to local address
				asyncServerSocketChannel.bind(socketAddress, backlog);
				// display a waiting message while ... waiting clients
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, asyncServerSocketChannel.getLocalAddress()
								+ ": Waiting for connections ...");

				// wait for incoming connections
				this.pendingConnections(asyncServerSocketChannel);

			} else {
				throw new IllegalStateException("채널이 열려있지 않습니다.");
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	private void pendingConnections(
			AsynchronousServerSocketChannel asyncServerSocketChannel) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		asyncServerSocketChannel.accept(null,
				new AsyncReceiveCompletionHandler(configure,
						asyncServerSocketChannel));

		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
