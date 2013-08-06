package com.abreqadhabra.nflight.application.service.net.stream.blocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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

	private Configure configure;
	private InetSocketAddress socketAddress;
	private boolean isRunning;

	public BlockingNetworkServiceImpl(final Configure configure,
			InetSocketAddress socketAddress) {
		this.configure = configure;
		this.socketAddress = socketAddress;
	}

	@Override
	public void start() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// create a new server-socket channel
		try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
			isRunning = true;
			// continue if it was successfully created
			if (serverSocket.isOpen()) {
				// set the blocking mode
				serverSocket.configureBlocking(true);
				// set some options
				NetworkServiceHelper.setChannelOption(serverSocket);
				// maximum number of pending connections
				int backlog = this.configure
						.getInt(Configure.BLOCKING_BIND_BACKLOG);
				// bind the server-socket channel to local address
				serverSocket.bind(this.socketAddress, backlog);
				// display a waiting message while ... waiting clients
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, serverSocket.getLocalAddress()
								+ " Waiting for connections ...");

				// wait for incoming connections
				while (isRunning) {
					pendingConnections(serverSocket);
				}
			} else {
				throw new IllegalStateException("ServerSocketChannel has been closed");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void pendingConnections(ServerSocketChannel serverSocket) {

		try (SocketChannel socket = serverSocket.accept()) {
			System.out.println("Incoming connection from: "
					+ socket.getRemoteAddress());
			
			// AIOSession session =new
			// AIOSession(sessionId.incrementAndGet(),channel,protocal,logic);
			// sessionMap.put(session.getId(),session);
			// session.init(configure);
			// session.start();
			//http://blog.daum.net/oraclejava/15867252 -> nonblocking/thread/selectable
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
