package com.abreqadhabra.nflight.application.service.net.stream.nonblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.server.net.socket.NetworkChannelHelper;
import com.abreqadhabra.nflight.application.service.net.INetworkService;
import com.abreqadhabra.nflight.application.service.net.NetworkServiceHelper;
import com.abreqadhabra.nflight.application.service.net.stream.blocking.BlockingNetworkServiceWorker;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class NonBlockingNetworkServiceImpl implements INetworkService {
	private static final Class<NonBlockingNetworkServiceImpl> THIS_CLAZZ = NonBlockingNetworkServiceImpl.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private final Configure configure;
	private final InetSocketAddress socketAddress;
	private boolean isRunning;
	private final ThreadPoolExecutor threadPoolExecutor;

	public NonBlockingNetworkServiceImpl(final Configure configure,
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
	public void start() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

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
						.getInt(Configure.BLOCKING_BIND_BACKLOG);
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
				throw new IllegalStateException(" 서버 소켓 채널 또는 셀렉터가 열려있지 않습니다.");
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
			long timeout = 1000;
			@SuppressWarnings("unused")
			int keyNum = selector.select(timeout);

			// Iterate over the set of keys for which events are available
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			for (SelectionKey selectionKey : selectionKeys) {
				
				// prevent the same key from coming up again
				// 취득한 키를 키 집합에서 제거
				selectionKeys.remove(selectionKey);

				if (!selectionKey.isValid()) {
					continue;
				}

//				System.out.println(" [ fd: " + selectionKey.hashCode()
//						+ " interest ops: {"
//						+ getSelectionKeyString(selectionKey.interestOps())
//						+ " }; ready ops: {"
//						+ getSelectionKeyString(selectionKey.readyOps())
//						+ " } ]");

				// 이벤트가 사용할 수 있는지 확인하고 처리
				if (selectionKey.isAcceptable()) {
					final SocketChannel socket = ((ServerSocketChannel) selectionKey
							.channel()).accept();
					System.out.println("Accepted socket connection from "
							+ socket.getRemoteAddress());
					socket.configureBlocking(false);

					String welcomeMessage = "Welcome to "
							+ socket.getLocalAddress().toString();
					socket.write(ByteBuffer.wrap(welcomeMessage
							.getBytes("UTF-8")));

					@SuppressWarnings("unused")
					SelectionKey sk = socket.register(selector,
							SelectionKey.OP_READ);

				} else if (selectionKey.isReadable()
						| selectionKey.isWritable()) {
					final SocketChannel socket = (SocketChannel) selectionKey
							.channel();
					
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, socket + ", isOpen=" +
							Boolean.toString(socket.isOpen()) +", isConnected=" +
							Boolean.toString(socket.isConnected()));

					
//					final long startN = System.nanoTime();
//
//					final ByteBuffer dst = NetworkChannelHelper.getByteBuffer(1024);
//
//					final int numRead = socket.read(dst);
//
//					final byte[] data = new byte[numRead];
//					System.arraycopy(dst.array(), 0, data, 0, numRead);
//					LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
//							new String(data, "UTF-8") + " [" + numRead
//									+ " bytes] from " + socket.getRemoteAddress());
//
//					final long endN = System.nanoTime();
//					final long diffN = endN - startN;
//
//					final double elapsedTime = diffN / 1.0E09;
//
//					LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
//							"elapsed: " + Double.toString(elapsedTime) + " seconds");
//					
					
					
				} else {
					System.out.println("Unexpected ops in select "
							+ selectionKey.readyOps());
				}
			}
			selectionKeys.clear();
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	private String getSelectionKeyString(int ops) {
		StringBuffer sb = new StringBuffer();
		sb.append(((ops & SelectionKey.OP_ACCEPT) != 0 ? " OP_ACCEPT" : ""));
		sb.append(((ops & SelectionKey.OP_CONNECT) != 0 ? " OP_CONNECT" : ""));
		sb.append(((ops & SelectionKey.OP_READ) != 0 ? " OP_READ" : ""));
		sb.append(((ops & SelectionKey.OP_WRITE) != 0 ? " OP_WRITE" : ""));

		return sb.toString();
	}

}
