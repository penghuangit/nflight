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

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.transport.exception.ServiceException;
import com.abreqadhabra.nflight.application.transport.network.Acceptor;
import com.abreqadhabra.nflight.application.transport.network.AcceptorShutdownHook;
import com.abreqadhabra.nflight.application.transport.network.ServerChannelFactory;
import com.abreqadhabra.nflight.common.concurrency.AbstractRunnable;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.UnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class BlockingAcceptor extends AbstractRunnable implements Acceptor {
	private static Class<BlockingAcceptor> THIS_CLAZZ = BlockingAcceptor.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private boolean isRunning;
	private ThreadPoolExecutor threadPool;
	private Configure configure;
	private ServerSocketChannel channel;

	public BlockingAcceptor(boolean isRunning, InetSocketAddress endpoint,
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
		int backlog = this.configure.getInt(Configure.BLOCKING_BIND_BACKLOG);
		try {
			// create a new server-socket channel
			this.channel = this.createServerChannelFactory()
					.createBlockingServerSocketChannel(endpoint, backlog);
		} catch (IOException | InterruptedException | ExecutionException e) {
			throw new ServiceException(e).addContextValue("endpoint", endpoint)
					.addContextValue("backlog", backlog);
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
			this.isRunning = false;
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
			throw new ServiceException(e);
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
			throw new ServiceException(e);
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
