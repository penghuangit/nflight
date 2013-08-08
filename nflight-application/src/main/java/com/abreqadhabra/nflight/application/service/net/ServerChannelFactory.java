package com.abreqadhabra.nflight.application.service.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.service.net.NetworkServiceHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ServerChannelFactory {
	private static final Class<ServerChannelFactory> THIS_CLAZZ = ServerChannelFactory.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public AsynchronousServerSocketChannel createAsynchronousServerSocketChannel(
			final ThreadPoolExecutor threadPool, final int initialSize,
			final SocketAddress endpoint, final int backlog) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			// create asynchronous server-socket channel bound to the thread
			// Group
			final AsynchronousChannelGroup threadGroup = AsynchronousChannelGroup
					.withCachedThreadPool(threadPool, initialSize);

			final AsynchronousServerSocketChannel asyncServerSocketChannel = AsynchronousServerSocketChannel
					.open(threadGroup);
			if (asyncServerSocketChannel.isOpen()) {
				// set some options
				NetworkServiceHelper.setChannelOption(asyncServerSocketChannel);
				// bind the server-socket channel to local address
				asyncServerSocketChannel.bind(endpoint, backlog);
				// display a waiting message while ... waiting clients
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, asyncServerSocketChannel.getLocalAddress()
								+ ": Waiting for connections ...");

				return asyncServerSocketChannel;
			} else {
				throw new IllegalStateException("채널이 열려있지 않습니다.");
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public ServerSocketChannel createBlockingServerSocketChannel(
			final boolean isBlock, final InetSocketAddress endpoint,
			final int backlog) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			final ServerSocketChannel serverSocket = ServerSocketChannel.open();
			if (serverSocket.isOpen()) {
				// configure non-blocking mode
				serverSocket.configureBlocking(isBlock);
				// set some options
				NetworkServiceHelper.setChannelOption(serverSocket);
				// bind the server-socket channel to local address
				serverSocket.bind(endpoint, backlog);
				// display a waiting message while ... waiting clients
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, serverSocket.getLocalAddress()
								+ " Waiting for connections ...");

				return serverSocket;
			} else {
				throw new IllegalStateException("채널이 열려있지 않습니다.");
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
