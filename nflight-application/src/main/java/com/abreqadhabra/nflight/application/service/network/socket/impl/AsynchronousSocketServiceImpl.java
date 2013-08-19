package com.abreqadhabra.nflight.application.service.network.socket.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.NetworkChannel;
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
import com.abreqadhabra.nflight.application.trash_server.net.socket.NetworkChannelHelper;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.UnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class AsynchronousSocketServiceImpl extends AbstractSocketService {
	private static Class<AsynchronousSocketServiceImpl> THIS_CLAZZ = AsynchronousSocketServiceImpl.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private ThreadPoolExecutor threadPool;
	private AsynchronousServerSocketChannel channel;

	public AsynchronousSocketServiceImpl(InetSocketAddress endpoint)
			throws NFlightException {
		super(Config.getBoolean(SocketServiceConfiguration.KEY_BOO_SOCKET_ASYNC_RUNNING));
		this.threadPool = this.getThreadPoolExecutor(
				SocketServiceConfiguration.KEY_STR_SOCKET_ASYNC_SERVICE_THREAD_POOL_NAME,
				SocketServiceConfiguration.KEY_INT_SOCKET_ASYNC_SERVICE_THREAD_POOL_MONITORING_DELAY_SECONDS,
				SocketServiceConfiguration.KEY_BOO_SOCKET_ASYNC_SERVICE_THREAD_POOL_MONITORING);
		this.init(endpoint);
	}

	@Override
	public void init(InetSocketAddress endpoint) throws NFlightException {
		try {
			int initialSize = Config
					.getInt(SocketServiceConfiguration.KEY_INT_SOCKET_ASYNC_THREADPOOL_INITIALSIZE);
			// maximum number of pending connections
			int backlog = Config.getInt(SocketServiceConfiguration.KEY_INT_SOCKET_ASYNC_BIND_BACKLOG);
			this.channel = this.createServerChannelFactory()
					.createAsyncServerSocketChannel(this.threadPool,
							initialSize, endpoint, backlog);
		} catch (IOException | InterruptedException | ExecutionException e) {
			throw new SocketServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public void start() throws NFlightException {
		final Thread CURRENT_THREAD = Thread.currentThread();
		CURRENT_THREAD.getStackTrace()[1].getMethodName();
		try {
			while (this.isRunning) {
				Future<AsynchronousSocketChannel> future = this.channel
						.accept();
				AsynchronousSocketChannel socket = future.get();
				this.accept(socket);
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

		AsynchronousSocketChannel socket = (AsynchronousSocketChannel) socketChannel;
		try {
			LOGGER.logp(
					Level.FINER,
					CLAZZ_NAME,
					METHOD_NAME,
					"Accepted socket connection from "
							+ socket.getRemoteAddress());
			this.receive(socket);
		} catch (IOException e) {
			throw new SocketServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
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

		AsynchronousSocketChannel socket = (AsynchronousSocketChannel) socketChannel;
		try {
			int capacity = Config
					.getInt(SocketServiceConfiguration.KEY_INT_SOCKET_ASYNC_INCOMING_BUFFER_CAPACITY);
			ByteBuffer incomingByteBuffer = NetworkChannelHelper
					.getByteBuffer(capacity);
			Integer numRead = socket.read(incomingByteBuffer).get();
			incomingByteBuffer.flip();
			if (incomingByteBuffer.hasRemaining()) {
				incomingByteBuffer.compact();
			} else {
				incomingByteBuffer.clear();
			}
			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getSimpleName(),
					METHOD_NAME,
					new String(incomingByteBuffer.array(), "UTF-8") + " ["
							+ numRead + " bytes] from "
							+ socket.getRemoteAddress());

		} catch (InterruptedException | ExecutionException | IOException e) {
			throw new SocketServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public ServerSocketChannelFactory createServerChannelFactory() {
		return new ServerSocketChannelFactory();
	}
}
