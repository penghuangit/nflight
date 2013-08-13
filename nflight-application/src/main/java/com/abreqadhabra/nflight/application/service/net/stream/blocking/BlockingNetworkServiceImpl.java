package com.abreqadhabra.nflight.application.service.net.stream.blocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.AbstractNetworkServiceImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class BlockingNetworkServiceImpl extends AbstractNetworkServiceImpl {
	private static Class<BlockingNetworkServiceImpl> THIS_CLAZZ = BlockingNetworkServiceImpl.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public BlockingNetworkServiceImpl(Configure configure,
			ThreadPoolExecutor threadPool,
			InetSocketAddress endpoint) {
		super(configure, threadPool, endpoint);
		this.backlog = this.configure.getInt(Configure.BLOCKING_BIND_BACKLOG);
	}

	@Override
	public void run() {
		try {
			this.isRunning = true;
			// create a new server-socket channel
			ServerSocketChannel serverSocket;

			serverSocket = this.createServerChannelFactory()
					.createBlockingServerSocketChannel(this.endpoint,
							this.backlog);

			// wait for incoming connections
			while (this.isRunning) {
				this.pendingConnections(serverSocket);
			}
		} catch (IOException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	private void pendingConnections(ServerSocketChannel serverSocket) {

		try {
			SocketChannel socket = serverSocket.accept();
			System.out.println("Accepted socket connection from "
					+ socket.getRemoteAddress());

			// write an welcome message
			String welcomeMessage = "Welcome to "
					+ socket.getLocalAddress().toString();
			socket.write(ByteBuffer.wrap(welcomeMessage.getBytes("UTF-8")));

			Future<?> f = this.threadPool
					.submit(new BlockingNetworkServiceWorker(this.configure,
							socket));

			System.out.println("Future<?>: " + f.getClass().getName());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
