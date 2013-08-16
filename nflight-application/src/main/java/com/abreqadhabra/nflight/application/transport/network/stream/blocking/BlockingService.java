package com.abreqadhabra.nflight.application.transport.network.stream.blocking;

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

import com.abreqadhabra.nflight.application.transport.exception.NetworkServiceException;
import com.abreqadhabra.nflight.application.transport.network.ServiceShutdownHook;
import com.abreqadhabra.nflight.application.transport.network.ServerChannelFactory;
import com.abreqadhabra.nflight.application.transport.network.Service;
import com.abreqadhabra.nflight.common.concurrency.AbstractRunnable;
import com.abreqadhabra.nflight.common.concurrency.thread.ThreadHelper;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.UnexpectedException;
import com.abreqadhabra.nflight.common.launcher.Configure;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class BlockingService extends AbstractRunnable implements Service {
	private static Class<BlockingService> THIS_CLAZZ = BlockingService.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private boolean isRunning;
	private ThreadPoolExecutor threadPool;
	private Configure configure;
	private ServerSocketChannel channel;

	public BlockingService(boolean isRunning, Configure configure,
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
		int backlog = this.configure.getInt(Configure.BLOCKING_BIND_BACKLOG);
		try {
			// create a new server-socket channel
			this.channel = this.createServerChannelFactory()
					.createBlockingServerSocketChannel(endpoint, backlog);
		} catch (IOException | InterruptedException | ExecutionException e) {
			throw new NetworkServiceException(e).addContextValue("endpoint",
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
			throw new NetworkServiceException(e);
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
			this.isRunning = false;
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
			throw new NetworkServiceException(e);
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
			throw new NetworkServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public void receive(NetworkChannel socketChannel) {
		SocketChannel socket = (SocketChannel) socketChannel;
		@SuppressWarnings("unused")
		Future<?> future = this.threadPool.submit(new BlockingReceiver(
				this.configure, socket));
	}

	@Override
	public ServerChannelFactory createServerChannelFactory() {
		return new ServerChannelFactory();
	}

}
