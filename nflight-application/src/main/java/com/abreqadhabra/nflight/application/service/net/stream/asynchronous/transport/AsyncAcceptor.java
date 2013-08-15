package com.abreqadhabra.nflight.application.service.net.stream.asynchronous.transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.exception.ServiceException;
import com.abreqadhabra.nflight.application.service.net.Acceptor;
import com.abreqadhabra.nflight.application.service.net.AcceptorShutdownHook;
import com.abreqadhabra.nflight.application.service.net.ServerChannelFactory;
import com.abreqadhabra.nflight.common.concurrency.AbstractRunnable;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.UnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class AsyncAcceptor extends AbstractRunnable implements Acceptor {
	private static Class<AsyncAcceptor> THIS_CLAZZ = AsyncAcceptor.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private ThreadPoolExecutor threadPool;
	private Configure configure;
	private AsynchronousServerSocketChannel channel;

	public AsyncAcceptor(InetSocketAddress endpoint,
			ThreadPoolExecutor threadPool, Configure configure)
			throws NFlightException {
		super.setShutdownHookThread(new AcceptorShutdownHook(this).getThread());
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
			this.accept(null);
			System.in.read();
		} catch (IOException e) {
			throw new ServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
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
	public void accept(SocketChannel socket) throws NFlightException {
		this.channel.accept(null, new AsyncReceiveCompletionHandler(
				this.configure, this.channel));
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
	public void receive(SocketChannel socket) throws NFlightException {
		// TODO Auto-generated method stub

	}

	@Override
	public ServerChannelFactory createServerChannelFactory() {
		return new ServerChannelFactory();
	}
}
