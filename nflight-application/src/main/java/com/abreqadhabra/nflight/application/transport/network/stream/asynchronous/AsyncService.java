package com.abreqadhabra.nflight.application.transport.network.stream.asynchronous;

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

import com.abreqadhabra.nflight.application.server.net.socket.NetworkChannelHelper;
import com.abreqadhabra.nflight.application.transport.exception.NetworkServiceException;
import com.abreqadhabra.nflight.application.transport.network.Service;
import com.abreqadhabra.nflight.application.transport.network.ServiceShutdownHook;
import com.abreqadhabra.nflight.application.transport.network.ServerChannelFactory;
import com.abreqadhabra.nflight.application.concurrency.AbstractRunnable;
import com.abreqadhabra.nflight.application.concurrency.thread.ThreadHelper;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.UnexpectedException;
import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class AsyncService extends AbstractRunnable implements Service {
	private static Class<AsyncService> THIS_CLAZZ = AsyncService.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private boolean isRunning;
	private ThreadPoolExecutor threadPool;
	private Configure configure;
	private AsynchronousServerSocketChannel channel;

	public AsyncService(boolean isRunning, Configure configure,
			InetSocketAddress endpoint) throws NFlightException {
		super.setShutdownHookThread(new ServiceShutdownHook(this).getThread());
		this.isRunning = isRunning;
		this.configure = configure;
		String threadPoolName = this.configure
				.get(Configure.BLOCKING_SERVICE_THREAD_POOL_NAME);
		int threadPoolMonitoringDelaySeconds = this.configure
				.getInt(Configure.BLOCKING_SERVICE_THREAD_POOL_MONITORING_DELAY_SECONDS);
		boolean isThreadPoolMonitoring = this.configure
				.getBoolean(Configure.BLOCKING_SERVICE_THREAD_POOL_MONITORING);
		this.threadPool = ThreadHelper.getThreadPoolExecutor(
				threadPoolName,
				isThreadPoolMonitoring,
				threadPoolMonitoringDelaySeconds);
		this.init(endpoint);
	}

	@Override
	public void init(InetSocketAddress endpoint) throws NFlightException {
		try {
			int initialSize = this.configure
					.getInt(Configure.ASYNC_THREADPOOL_INITIALSIZE);
			// maximum number of pending connections
			int backlog = this.configure.getInt(Configure.ASYNC_BIND_BACKLOG);
			this.channel = this.createServerChannelFactory()
					.createAsyncServerSocketChannel(this.threadPool,
							initialSize, endpoint, backlog);
		} catch (IOException | InterruptedException | ExecutionException e) {
			throw new NetworkServiceException(e);
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
				Future<AsynchronousSocketChannel> future = channel.accept();
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
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			LOGGER.logp(Level.SEVERE, CLAZZ_NAME, METHOD_NAME, "Stopping...");
			this.channel.close();
			this.threadPool.shutdown();
			LOGGER.logp(Level.SEVERE, CLAZZ_NAME, METHOD_NAME, "Stopped");
		} catch (IOException e) {
			throw new NetworkServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		} finally {
			super.interrupt();
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
			receive(socket);
		} catch (IOException e) {
			throw new NetworkServiceException(e);
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
			int capacity = this.configure
					.getInt(Configure.ASYNC_INCOMING_BUFFER_CAPACITY);
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
			throw new NetworkServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public ServerChannelFactory createServerChannelFactory() {
		return new ServerChannelFactory();
	}
}
