package com.abreqadhabra.nflight.application.service.net.datagram.multicast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.AbstractNetworkServiceImpl;
import com.abreqadhabra.nflight.application.service.net.NetworkServiceHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class MulticastNetworkServiceImpl extends AbstractNetworkServiceImpl {
	private static Class<MulticastNetworkServiceImpl> THIS_CLAZZ = MulticastNetworkServiceImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public MulticastNetworkServiceImpl(Configure configure,
			ThreadPoolExecutor threadPool, InetSocketAddress endpoint) {
		super(configure, threadPool, endpoint);
	}

	@Override
	public void run() {
		try {
			this.isRunning = true;
			// create a new server-socket channel & selector
			DatagramChannel serverSocket = this.createServerChannelFactory()
					.createMulticastDatagramChannel(
							StandardProtocolFamily.INET, this.endpoint);
			// check that both of them were successfully opened
			if (serverSocket.isOpen()) {
				// wait for incoming connections
				while (this.isRunning) {
					this.pendingConnections(serverSocket);
				}
			} else {
				throw new IllegalStateException("서버 소켓 채널 또는 셀렉터가 열려있지 않습니다.");
			}
		} catch (IOException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	private void pendingConnections(DatagramChannel channel) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {

			int capacity = this.configure
					.getInt(Configure.MULTICAST_INCOMING_BUFFER_CAPACITY);
			ByteBuffer incomingByteBuffer = NetworkServiceHelper
					.getByteBuffer(capacity);

			SocketAddress clientEndpoint = channel.receive(incomingByteBuffer);

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

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
