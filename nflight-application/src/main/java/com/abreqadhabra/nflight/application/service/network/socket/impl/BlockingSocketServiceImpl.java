package com.abreqadhabra.nflight.application.service.network.socket.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.NetworkChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.Config;
import com.abreqadhabra.nflight.application.service.network.socket.AbstractSocketService;
import com.abreqadhabra.nflight.application.service.network.socket.ServerSocketChannelFactory;
import com.abreqadhabra.nflight.application.service.network.socket.conf.SocketServiceConfiguration;
import com.abreqadhabra.nflight.application.service.network.socket.exception.SocketServiceException;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.UnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class BlockingSocketServiceImpl extends AbstractSocketService {
	private static Class<BlockingSocketServiceImpl> THIS_CLAZZ = BlockingSocketServiceImpl.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private ThreadPoolExecutor threadPool;
	private ServerSocketChannel channel;

	public BlockingSocketServiceImpl(InetSocketAddress endpoint) throws NFlightException {
		super(Config.getBoolean(SocketServiceConfiguration.KEY_BOO_SOCKET_BLOCKING_RUNNING));
		this.threadPool = this
				.getThreadPoolExecutor(
						SocketServiceConfiguration.KEY_INT_SOCKET_BLOCKING_SERVICE_THREAD_POOL_NAME,
						SocketServiceConfiguration.KEY_INT_SOCKET_BLOCKING_SERVICE_THREAD_POOL_MONITORING_DELAY_SECONDS,
						SocketServiceConfiguration.KEY_INT_SOCKET_BLOCKING_SERVICE_THREAD_POOL_MONITORING);
		this.init(endpoint);
	}

	@Override
	public void init(InetSocketAddress endpoint) throws NFlightException {
		int backlog = Config.getInt(SocketServiceConfiguration.KEY_INT_SOCKET_BLOCKING_BIND_BACKLOG);
		try {
			// create a new server-socket channel
			this.channel = this.createServerChannelFactory()
					.createBlockingServerSocketChannel(endpoint, backlog);
		} catch (IOException | InterruptedException | ExecutionException e) {
			throw new SocketServiceException(e).addContextValue("endpoint",
					endpoint).addContextValue("backlog", backlog);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public void start() throws NFlightException {
		try {
			while (this.isRunning) {
				SocketChannel socket = this.channel.accept();
				this.accept(socket);
			}
		} catch (IOException e) {
			throw new SocketServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public void stop() throws NFlightException {
		try {
			this.channel.close();
			this.threadPool.shutdown();
			this.interrupt();
		} catch (IOException e) {
			throw new SocketServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public void accept(NetworkChannel socketChannel) throws NFlightException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		SocketChannel socket = (SocketChannel) socketChannel;
		try {
			LOGGER.logp(
					Level.INFO,
					CLAZZ_NAME,
					METHOD_NAME,
					"Accepted socket connection from "
							+ socket.getRemoteAddress());
			// write an welcome message
			String welcomeMessage = "Welcome to "
					+ socket.getLocalAddress().toString();
			this.send(socket, welcomeMessage);
			this.receive(socket);
		} catch (IOException e) {
			throw new SocketServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public void send(SocketChannel socket) {
		// 큐에 있는 스트림을 전송
	}

	@Override
	public void send(SocketChannel socket, Object message)
			throws NFlightException {
		try {
			if (message instanceof String) {
				socket.write(ByteBuffer.wrap(((String) message)
						.getBytes("UTF-8")));
			}
		} catch (IOException e) {
			throw new SocketServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public void receive(NetworkChannel socketChannel) {
		SocketChannel socket = (SocketChannel) socketChannel;
		@SuppressWarnings("unused")
		Future<?> future = this.threadPool.submit(new BlockingSocketReceiver(socket));
	}

	@Override
	public ServerSocketChannelFactory createServerChannelFactory() {
		return new ServerSocketChannelFactory();
	}
}
