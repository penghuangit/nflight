/*
 * 
 */
package com.abreqadhabra.nflight.application.service.network.socket.impl.stream;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.NetworkChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.Config;
import com.abreqadhabra.nflight.application.common.launcher.concurrent.executor.ThreadPoolImpl;
import com.abreqadhabra.nflight.application.service.network.socket.AbstractSocketServiceTask;
import com.abreqadhabra.nflight.application.service.network.socket.SocketService;
import com.abreqadhabra.nflight.application.service.network.socket.SocketServiceDescriptor;
import com.abreqadhabra.nflight.application.service.network.socket.conf.SocketServiceConfig;
import com.abreqadhabra.nflight.application.service.network.socket.exception.SocketServiceException;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.UnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class BlockingSocketServiceImpl.
 */
public class BlockingSocketServiceImpl extends AbstractSocketServiceTask
		implements
			SocketService {

	/** The this clazz. */
	private static Class<BlockingSocketServiceImpl> THIS_CLAZZ = BlockingSocketServiceImpl.class;

	/** The clazz name. */
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();

	/** The logger. */
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	/** The channel. */
	private ServerSocketChannel channel;

	/** The endpoint. */
	private InetSocketAddress endpoint;

	/** The thread pool. */
	private ThreadPoolExecutor threadPool;

	/**
	 * Instantiates a new blocking socket service impl.
	 * 
	 * @param serviceDescriptor
	 *            the service descriptor
	 * @throws NFlightException
	 *             the n flight exception
	 */
	public BlockingSocketServiceImpl(SocketServiceDescriptor serviceDescriptor)
			throws NFlightException {
		super(
				Config.getBoolean(SocketServiceConfig.KEY_BOO_SOCKET_BLOCKING_RUNNING));
		this.channel = serviceDescriptor.getServerSocketChannel();
		this.endpoint = serviceDescriptor.getEndpoint();
		// 서비스에서 사용할 쓰레드풀 객체 생성
		this.threadPool = new ThreadPoolImpl()
				.getThreadPoolExecutor(
						Config.get(SocketServiceConfig.KEY_INT_SOCKET_BLOCKING_SERVICE_THREAD_POOL_NAME),
						Config.getBoolean(SocketServiceConfig.KEY_INT_SOCKET_BLOCKING_SERVICE_THREAD_POOL_MONITORING),
						Config.getInt(SocketServiceConfig.KEY_INT_SOCKET_BLOCKING_SERVICE_THREAD_POOL_MONITORING_DELAY_SECONDS));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.abreqadhabra.nflight.application.common.launcher.concurrent.
	 * AbstractRunnable#start()
	 */
	@Override
	public void startup() throws NFlightException {
		try {
			this.bind();
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
	public boolean status() {
		return this.isRunning;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.abreqadhabra.nflight.application.common.launcher.concurrent.
	 * AbstractRunnable#stop()
	 */
	@Override
	public void shutdown() throws NFlightException {
		try {
			this.channel.close();
			this.threadPool.shutdown();
			if (!"main".equals(Thread.currentThread().getName())) {
				this.interrupt();
			}
		} catch (IOException e) {
			throw new SocketServiceException(e);
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.abreqadhabra.nflight.application.service.network.socket.SocketService
	 * #bind()
	 */
	@Override
	public void bind() throws NFlightException {
		final Thread CURRENT_THREAD = Thread.currentThread();
		final String METHOD_NAME = CURRENT_THREAD.getStackTrace()[1]
				.getMethodName();

		int backlog = Config
				.getInt(SocketServiceConfig.KEY_INT_SOCKET_BLOCKING_BIND_BACKLOG);
		try {
			this.channel.bind(this.endpoint, backlog);
			// display a waiting message while ... waiting clients
			LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"Waiting for connections ..." + this.endpoint);
		} catch (IOException e) {
			throw new SocketServiceException(e).addContextValue("endpoint",
					this.endpoint).addContextValue("backlog", backlog);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.abreqadhabra.nflight.application.service.network.socket.SocketService
	 * #accept(java.nio.channels.NetworkChannel)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.abreqadhabra.nflight.application.service.network.socket.SocketService
	 * #send(java.nio.channels.SocketChannel)
	 */
	@Override
	public void send(SocketChannel socket) {
		// 큐에 있는 스트림을 전송
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.abreqadhabra.nflight.application.service.network.socket.SocketService
	 * #send(java.nio.channels.SocketChannel, java.lang.Object)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.abreqadhabra.nflight.application.service.network.socket.SocketService
	 * #receive(java.nio.channels.NetworkChannel)
	 */
	@Override
	public void receive(NetworkChannel socketChannel) {
		SocketChannel socket = (SocketChannel) socketChannel;
		this.threadPool.submit(new BlockingSocketReceiver(socket));
	}



}
