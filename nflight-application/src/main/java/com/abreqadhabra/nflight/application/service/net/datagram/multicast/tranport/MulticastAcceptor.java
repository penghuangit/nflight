package com.abreqadhabra.nflight.application.service.net.datagram.multicast.tranport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.Acceptor;
import com.abreqadhabra.nflight.application.service.net.NetworkServiceHelper;
import com.abreqadhabra.nflight.application.service.net.ServerChannelFactory;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class MulticastAcceptor implements Runnable, Acceptor {
	private static Class<MulticastAcceptor> THIS_CLAZZ = MulticastAcceptor.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private boolean isRunning;
	private Configure configure;
	DatagramChannel channel;

	public MulticastAcceptor(boolean isRunning, InetSocketAddress endpoint,
			Configure configure) throws IOException, InterruptedException,
			ExecutionException {
		this.isRunning = isRunning;
		this.configure = configure;
		init(endpoint);
	}
	@Override
	public void init(InetSocketAddress endpoint) throws IOException,
			InterruptedException, ExecutionException {
		// create a new server-socket channel & selector
		channel = this.createServerChannelFactory()
				.createMulticastDatagramChannel(StandardProtocolFamily.INET,
						endpoint);

	}

	@Override
	public ServerChannelFactory createServerChannelFactory() {
		return new ServerChannelFactory();
	}

	@Override
	public void run() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		try {
			// check that both of them were successfully opened
			if (channel.isOpen()) {
				// wait for incoming connections
				while (this.isRunning) {
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
				}
			} else {
				throw new IllegalStateException("서버 소켓 채널 또는 셀렉터가 열려있지 않습니다.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
