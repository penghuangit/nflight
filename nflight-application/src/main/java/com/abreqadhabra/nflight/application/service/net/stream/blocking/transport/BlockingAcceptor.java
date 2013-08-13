package com.abreqadhabra.nflight.application.service.net.stream.blocking.transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.Acceptor;
import com.abreqadhabra.nflight.application.service.net.ServerChannelFactory;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class BlockingAcceptor implements Runnable, Acceptor {
	private static Class<BlockingAcceptor> THIS_CLAZZ = BlockingAcceptor.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private boolean isRunning;
	private ThreadPoolExecutor threadPool;
	private Configure configure;
	private ServerSocketChannel channel;

	public BlockingAcceptor(boolean isRunning, InetSocketAddress endpoint,
			ThreadPoolExecutor threadPool, Configure configure)
			throws IOException, InterruptedException, ExecutionException {

		this.isRunning = isRunning;
		this.threadPool = threadPool;
		this.configure = configure;
		this.init(endpoint);
	}

	@Override
	public void init(InetSocketAddress endpoint) throws IOException,
			InterruptedException, ExecutionException {
		int backlog = this.configure.getInt(Configure.BLOCKING_BIND_BACKLOG);
		// create a new server-socket channel
		this.channel = this.createServerChannelFactory()
				.createBlockingServerSocketChannel(endpoint, backlog);
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
			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getSimpleName(),
					METHOD_NAME,
					"current thread is "
							+ LoggingHelper.getThreadName(Thread
									.currentThread()));
			while (this.isRunning) {
				SocketChannel socket = this.channel.accept();
				System.out.println("Accepted socket connection from "
						+ socket.getRemoteAddress());

				// write an welcome message
				String welcomeMessage = "Welcome to "
						+ socket.getLocalAddress().toString();
				socket.write(ByteBuffer.wrap(welcomeMessage.getBytes("UTF-8")));

				Future<?> f = this.threadPool.submit(new BlockingReceiver(
						this.configure, socket));

				System.out.println("Future<?>: " + f.getClass().getName());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
