package com.abreqadhabra.nflight.application.service.net.stream.blocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.INetworkService;
import com.abreqadhabra.nflight.application.service.net.NetworkServiceHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class BlockingNetworkServiceImpl implements INetworkService, Runnable {
	private static final Class<BlockingNetworkServiceImpl> THIS_CLAZZ = BlockingNetworkServiceImpl.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private final Configure configure;
	private final InetSocketAddress socketAddress;
	private boolean isRunning;
	private final ThreadPoolExecutor threadPoolExecutor;

	public BlockingNetworkServiceImpl(final Configure configure,
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
			String currentThreadName = Thread.currentThread().getName();
			Thread.currentThread().setName(currentThreadName);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"current thread is " + Thread.currentThread().getName());
		}

		// create a new server-socket channel
		try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
			this.isRunning = true;
			// continue if it was successfully created
			if (serverSocket.isOpen()) {
				// set the blocking mode
				serverSocket.configureBlocking(true);
				// set some options
				NetworkServiceHelper.setChannelOption(serverSocket);
				// maximum number of pending connections
				final int backlog = this.configure
						.getInt(Configure.BLOCKING_BIND_BACKLOG);
				// bind the server-socket channel to local address
				serverSocket.bind(this.socketAddress, backlog);
				// display a waiting message while ... waiting clients
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, serverSocket.getLocalAddress()
								+ " Waiting for connections ...");
				// wait for incoming connections
				while (this.isRunning) {
					this.pendingConnections(serverSocket);
				}
			} else {
				throw new IllegalStateException(
						"ServerSocketChannel has been closed");
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private void pendingConnections(final ServerSocketChannel serverSocket) {

		try {
			final SocketChannel socket = serverSocket.accept();
			System.out.println("Accepted socket connection from "
					+ socket.getRemoteAddress());

			// write an welcome message
			String welcomeMessage = "Welcome to "
					+ socket.getLocalAddress().toString();
			socket.write(ByteBuffer.wrap(welcomeMessage.getBytes("UTF-8")));

			Future<?> f = this.threadPoolExecutor
					.submit(new BlockingNetworkServiceWorker(configure, socket));

			System.out.println("Future<?>: " + f.getClass().getName());

		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

}
