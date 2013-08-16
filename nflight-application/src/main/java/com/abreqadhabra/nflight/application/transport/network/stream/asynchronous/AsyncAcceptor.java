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

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.server.net.socket.NetworkChannelHelper;
import com.abreqadhabra.nflight.application.transport.exception.ServiceException;
import com.abreqadhabra.nflight.application.transport.network.Acceptor;
import com.abreqadhabra.nflight.application.transport.network.AcceptorShutdownHook;
import com.abreqadhabra.nflight.application.transport.network.ServerChannelFactory;
import com.abreqadhabra.nflight.common.concurrency.AbstractRunnable;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.UnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class AsyncAcceptor extends AbstractRunnable implements Acceptor {
	private static Class<AsyncAcceptor> THIS_CLAZZ = AsyncAcceptor.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private boolean isRunning;
	private ThreadPoolExecutor threadPool;
	private Configure configure;
	private AsynchronousServerSocketChannel channel;

	public AsyncAcceptor(boolean isRunning, InetSocketAddress endpoint,
			ThreadPoolExecutor threadPool, Configure configure)
			throws NFlightException {
		super.setShutdownHookThread(new AcceptorShutdownHook(this).getThread());
		this.isRunning = isRunning;
		this.threadPool = threadPool;
		this.configure = configure;
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
			throw new ServiceException(e);
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
			throw new ServiceException(e);
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
			throw new ServiceException(e);
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
			throw new ServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public ServerChannelFactory createServerChannelFactory() {
		return new ServerChannelFactory();
	}
}
