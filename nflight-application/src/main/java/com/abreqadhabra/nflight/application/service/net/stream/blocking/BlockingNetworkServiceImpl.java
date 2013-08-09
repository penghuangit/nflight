package com.abreqadhabra.nflight.application.service.net.stream.blocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.stream.AbstractNetworkServiceImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class BlockingNetworkServiceImpl extends AbstractNetworkServiceImpl {
	private static final Class<BlockingNetworkServiceImpl> THIS_CLAZZ = BlockingNetworkServiceImpl.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public BlockingNetworkServiceImpl(final Configure configure,
			final ThreadPoolExecutor threadPool,
			final InetSocketAddress socketAddress) {
		super(configure, threadPool, socketAddress);
		this.backlog = this.configure.getInt(Configure.BLOCKING_BIND_BACKLOG);
	}

	@Override
	public void run() {
		this.isRunning = true;
		boolean isBlock = true;
		// create a new server-socket channel
		final ServerSocketChannel serverSocket = this
				.createServerChannelFactory()
				.createBlockingServerSocketChannel(isBlock, this.endpoint,
						this.backlog);
		// wait for incoming connections
		while (this.isRunning) {
			this.pendingConnections(serverSocket);
		}
	}

	private void pendingConnections(final ServerSocketChannel serverSocket) {

		try {
			final SocketChannel socket = serverSocket.accept();
			System.out.println("Accepted socket connection from "
					+ socket.getRemoteAddress());

			// write an welcome message
			final String welcomeMessage = "Welcome to "
					+ socket.getLocalAddress().toString();
			socket.write(ByteBuffer.wrap(welcomeMessage.getBytes("UTF-8")));

			final Future<?> f = this.threadPool
					.submit(new BlockingNetworkServiceWorker(this.configure,
							socket));

			System.out.println("Future<?>: " + f.getClass().getName());

		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

}
