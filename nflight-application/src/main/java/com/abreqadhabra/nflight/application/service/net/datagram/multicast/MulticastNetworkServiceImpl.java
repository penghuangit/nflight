package com.abreqadhabra.nflight.application.service.net.datagram.multicast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.AbstractNetworkServiceImpl;
import com.abreqadhabra.nflight.application.service.net.NetworkServiceHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class MulticastNetworkServiceImpl extends AbstractNetworkServiceImpl {
	private static final Class<MulticastNetworkServiceImpl> THIS_CLAZZ = MulticastNetworkServiceImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public MulticastNetworkServiceImpl(final Configure configure,
			final ThreadPoolExecutor threadPool,
			final InetSocketAddress endpoint) {
		super(configure, threadPool, endpoint);
	}

	@Override
	public void run() {
		this.isRunning = true;
		// create a new server-socket channel & selector
		final DatagramChannel serverSocket = this.createServerChannelFactory()
				.createMulticastDatagramChannel(StandardProtocolFamily.INET,
						this.endpoint);
		// check that both of them were successfully opened
		if (serverSocket.isOpen()) {
			// wait for incoming connections
			while (this.isRunning) {
				this.pendingConnections(serverSocket);
			}
		} else {
			throw new IllegalStateException("서버 소켓 채널 또는 셀렉터가 열려있지 않습니다.");
		}
	}

	private void pendingConnections(final DatagramChannel channel) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {

			final int capacity = this.configure
					.getInt(Configure.MULTICAST_INCOMING_BUFFER_CAPACITY);
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
