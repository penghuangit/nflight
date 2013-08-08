package com.abreqadhabra.nflight.application.service.net.stream.nonblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.INetworkService;
import com.abreqadhabra.nflight.application.service.net.NetworkServiceHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class NonBlockingNetworkServiceImpl implements INetworkService, Runnable {
	private static final Class<NonBlockingNetworkServiceImpl> THIS_CLAZZ = NonBlockingNetworkServiceImpl.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private final Configure configure;
	private final InetSocketAddress socketAddress;
	private boolean isRunning;
	public NonBlockingNetworkServiceImpl(final Configure configure,
			final InetSocketAddress socketAddress,
			final ThreadPoolExecutor threadPoolExecutor) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		this.configure = configure;
		this.socketAddress = socketAddress;
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

		// create a new server-socket channel & selector
		try (ServerSocketChannel serverSocket = ServerSocketChannel.open();
				Selector selector = Selector.open();) {
			this.isRunning = true;
			// check that both of them were successfully opened
			if (serverSocket.isOpen() && selector.isOpen()) {
				// configure non-blocking mode
				serverSocket.configureBlocking(false);
				// set some options
				NetworkServiceHelper.setChannelOption(serverSocket);
				// maximum number of pending connections
				final int backlog = this.configure
						.getInt(Configure.NONBLOCKING_BIND_BACKLOG);
				// bind the server-socket channel to local address
				serverSocket.bind(this.socketAddress, backlog);
				// display a waiting message while ... waiting clients
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, serverSocket.getLocalAddress()
								+ " Waiting for connections ...");
				// Register the server socket channel, indicating an interest in
				// accepting new connections
				serverSocket.register(selector, SelectionKey.OP_ACCEPT);
				// wait for incoming connections
				while (this.isRunning) {
					this.pendingConnections(selector);
				}

			} else {
				throw new IllegalStateException("서버 소켓 채널 또는 셀렉터가 열려있지 않습니다.");
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	private void pendingConnections(final Selector selector) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			// wait for incoming an event one of the registered channels
			// 등록된 서버 소켓 채널에 대한 이벤트 발생을 대기
			final long timeout = 1000;
			selector.select(timeout);

			// there is something to process on selected keys
			final Iterator<SelectionKey> iterator = selector.selectedKeys()
					.iterator();

			while (iterator.hasNext()) {
				final SelectionKey selectionKey = iterator.next();
				// 취득한 키를 키 집합에서 제거
				// prevent the same key from coming up again
				iterator.remove();
				if (!selectionKey.isValid()) {
					continue;
				}
				
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
						NetworkServiceHelper.getReadySetString(selectionKey));
				
				// 이벤트가 사용할 수 있는지 확인하고 처리
				if (selectionKey.isAcceptable()) {
					this.accept(selector, selectionKey);
				} else if (selectionKey.isReadable()) {
					final ServerSession session = (ServerSession) selectionKey
							.attachment();
					session.receive(session);
				} else if (selectionKey.isWritable()) {
					final ServerSession session = (ServerSession) selectionKey
							.attachment();
					session.send(session);
				} else {
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
							METHOD_NAME, "Unexpected ops in select "
									+ selectionKey.readyOps());
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private void accept(final Selector selector, final SelectionKey selectionKey) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				NetworkServiceHelper.getReadySetString(selectionKey));

		try {
			final SocketChannel socket = ((ServerSocketChannel) selectionKey
					.channel()).accept();
			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getSimpleName(),
					METHOD_NAME,
					"Accepted socket connection from "
							+ socket.getRemoteAddress());
			socket.configureBlocking(false);
			final SelectionKey readyKey = socket.register(selector,
					SelectionKey.OP_READ);
			final ServerSession session = this.createSession(socket, readyKey);
			readyKey.attach(session);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private ServerSession createSession(final SocketChannel socket,
			final SelectionKey selectionKey) {
		return new NonBlockingServerSessionImpl(configure, socket, selectionKey);
	}

}