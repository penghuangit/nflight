package com.abreqadhabra.nflight.application.service.net.stream.blocking;

import java.io.IOException;
import java.net.InetSocketAddress;
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

public class BlockingNetworkServiceImpl implements INetworkService {
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
		this.configure = configure;
		this.socketAddress = socketAddress;
		this.threadPoolExecutor = threadPoolExecutor;
	}

	@Override
	public void start() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

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

				System.out.println(serverSocket + ": " + serverSocket.isOpen());

				// wait for incoming connections
				while (this.isRunning) {
					this.pendingConnections(serverSocket);
				}

				// threadPoolExecutor.submit(new BlockingNetworkServiceWorker(
				// serverSocket));

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
			System.out.println("Incoming connection from: "
					+ socket.getRemoteAddress());

			// System.out.println(socket.isBlocking());
			// ByteBuffer buffer = ByteBuffer.allocate(10);
			// System.out.println(socket.read(buffer));

			// BlockingNetworkServiceWorker st = new
			// BlockingNetworkServiceWorker(socket);
			// new Thread(st).start();

			Future<?> f = this.threadPoolExecutor
					.submit(new BlockingNetworkServiceWorker(socket));

			System.out.println("Future<?>: " + f.getClass().getName());
			
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

}
