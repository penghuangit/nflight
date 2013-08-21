package com.abreqadhabra.nflight.application.service.network.socket.impl.datagram;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.NetworkChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.Config;
import com.abreqadhabra.nflight.application.service.network.socket.AbstractSocketServiceTask;
import com.abreqadhabra.nflight.application.service.network.socket.SocketService;
import com.abreqadhabra.nflight.application.service.network.socket.SocketServiceDescriptor;
import com.abreqadhabra.nflight.application.service.network.socket.conf.SocketServiceConfig;
import com.abreqadhabra.nflight.application.service.network.socket.exception.SocketServiceException;
import com.abreqadhabra.nflight.application.service.network.socket.helper.SocketServiceHelper;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.UnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class MulticastDatagramServiceImpl extends AbstractSocketServiceTask
		implements
			SocketService {
	private static Class<MulticastDatagramServiceImpl> THIS_CLAZZ = MulticastDatagramServiceImpl.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	DatagramChannel channel;
	private InetSocketAddress endpoint;

	public MulticastDatagramServiceImpl(
			SocketServiceDescriptor serviceDescriptor)
			throws NFlightException {
		super(
				Config.getBoolean(SocketServiceConfig.KEY_BOO_SOCKET_MULTICAST_RUNNING));
		this.channel = serviceDescriptor.getDatagramChannel();
		this.endpoint = serviceDescriptor.getEndpoint();
	}

	@Override
	public void bind() throws NFlightException {
		final Thread CURRENT_THREAD = Thread.currentThread();
		final String METHOD_NAME = CURRENT_THREAD.getStackTrace()[1]
				.getMethodName();
		try {
			// create a new server-socket channel & selector
			this.channel.bind(this.endpoint);
			// display a waiting message while ... waiting clients
			LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"Waiting for connections ..." + this.endpoint);
		} catch (IOException e) {
			throw new SocketServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public void start() throws NFlightException {

		try {
			this.bind();
			// check that both of them were successfully opened
			if (this.channel.isOpen()) {
				// wait for incoming connections
				while (this.isRunning) {
					this.accept(null);
				}
			} else {
				throw new SocketServiceException("서버 소켓 채널 또는 셀렉터가 열려있지 않습니다.");
			}
		} catch (NFlightException e) {
			throw e;
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public void stop() throws NFlightException {
		final Thread CURRENT_THREAD = Thread.currentThread();
		try {
			this.isRunning = false;
			this.channel.close();
			System.out.println(">>>>>>>>>>>>>>"+CURRENT_THREAD.getName());

			this.interrupt(CURRENT_THREAD);
		} catch (IOException e) {
			throw new SocketServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public void accept(NetworkChannel socketChannel) throws NFlightException {
		this.receive(null);

	}
	@Override
	public void send(SocketChannel socket) {
		// TODO Auto-generated method stub

	}
	@Override
	public void send(SocketChannel socket, Object message)
			throws NFlightException {
		// TODO Auto-generated method stub

	}

	@Override
	public void receive(NetworkChannel socketChannel) throws NFlightException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			int capacity = Config
					.getInt(SocketServiceConfig.KEY_INT_SOCKET_MULTICAST_INCOMING_BUFFER_CAPACITY);
			ByteBuffer incomingByteBuffer = SocketServiceHelper
					.getByteBuffer(capacity);
			SocketAddress clientEndpoint = this.channel
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
		} catch (IOException e) {
			throw new SocketServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}
}
