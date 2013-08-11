package com.abreqadhabra.nflight.application.service.net.stream.asynchronous;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.stream.AbstractNetworkServiceImpl;

public class AsyncNetworkServiceImpl extends AbstractNetworkServiceImpl {

	public AsyncNetworkServiceImpl(final Configure configure,
			final ThreadPoolExecutor threadPool,
			final InetSocketAddress endpoint) {
		super(configure, threadPool, endpoint);
		// maximum number of pending connections
		this.backlog = this.configure.getInt(Configure.ASYNC_BIND_BACKLOG);
	}

	@Override
	public void run() {

		this.isRunning = true;
		final int initialSize = this.configure
				.getInt(Configure.ASYNC_THREADPOOL_INITIALSIZE);

		final AsynchronousServerSocketChannel asyncServerSocketChannel = this
				.createServerChannelFactory().createAsyncServerSocketChannel(
						this.threadPool, initialSize, this.endpoint,
						this.backlog);
		// wait for incoming connections
		this.pendingConnections(asyncServerSocketChannel);
	}

	private void pendingConnections(
			final AsynchronousServerSocketChannel asyncServerSocketChannel) {
		try {
			asyncServerSocketChannel.accept(null,
					new AsyncReceiveCompletionHandler(this.configure,
							asyncServerSocketChannel));
			System.in.read();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
