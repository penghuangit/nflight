package com.abreqadhabra.nflight.application.service.net.datagram.unicast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.NetworkServiceHelper;
import com.abreqadhabra.nflight.application.service.net.stream.AbstractNetworkServiceImpl;
import com.abreqadhabra.nflight.application.service.net.stream.ServerSession;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class UnicastNetworkServiceImpl extends AbstractNetworkServiceImpl {
	private static final Class<UnicastNetworkServiceImpl> THIS_CLAZZ = UnicastNetworkServiceImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public UnicastNetworkServiceImpl(final Configure configure,
			final ThreadPoolExecutor threadPool,
			final InetSocketAddress endpoint) {
		super(configure, threadPool, endpoint);
	}

	@Override
	public void run() {
		try {
			this.isRunning = true;
			// create a new server-socket channel & selector
			final Selector selector = Selector.open();
			final DatagramChannel serverSocket = this
					.createServerChannelFactory().createUnicastDatagramChannel(
							StandardProtocolFamily.INET, this.endpoint);
			// check that both of them were successfully opened
			if (selector.isOpen() && serverSocket.isOpen()) {
				// Register the server socket channel, indicating an interest in
				// accepting new connections
				serverSocket.register(selector, SelectionKey.OP_READ);
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

				LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME,
						NetworkServiceHelper.getReadySetString(selectionKey));

				// 이벤트가 사용할 수 있는지 확인하고 처리
				if (selectionKey.isReadable()) {
					this.receive(selector, selectionKey);

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

	private void receive(final Selector selector,
			final SelectionKey selectionKey) {

		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			final DatagramChannel channel = (DatagramChannel) selectionKey
					.channel();

			final int capacity = this.configure
					.getInt(Configure.UNICAST_INCOMING_BUFFER_CAPACITY);
			final ByteBuffer incomingByteBuffer = NetworkServiceHelper
					.getByteBuffer(capacity);

			final SocketAddress clientEndpoint = channel
					.receive(incomingByteBuffer);

			incomingByteBuffer.flip();
			if (incomingByteBuffer.hasRemaining()) {
				incomingByteBuffer.compact();
			} else {
				incomingByteBuffer.clear();
			}

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					new String(incomingByteBuffer.array(), "UTF-8") + " ["
							+ incomingByteBuffer.limit() + " bytes] from "
							+ clientEndpoint);

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
