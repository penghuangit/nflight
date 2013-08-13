package com.abreqadhabra.nflight.application.service.net.stream.asynchronous;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.AbstractNetworkServiceImpl;

public class AsyncNetworkServiceImpl extends AbstractNetworkServiceImpl {

	public AsyncNetworkServiceImpl(Configure configure,
			ThreadPoolExecutor threadPool,
			InetSocketAddress endpoint) {
		super(configure, threadPool, endpoint);
		// maximum number of pending connections
		this.backlog = this.configure.getInt(Configure.ASYNC_BIND_BACKLOG);
	}

	@Override
	public void run() {
		try {
			this.isRunning = true;
			int initialSize = this.configure
					.getInt(Configure.ASYNC_THREADPOOL_INITIALSIZE);

			AsynchronousServerSocketChannel asyncServerSocketChannel = this
					.createServerChannelFactory()
					.createAsyncServerSocketChannel(this.threadPool,
							initialSize, this.endpoint, this.backlog);

			// wait for incoming connections
			this.pendingConnections(asyncServerSocketChannel);
		} catch (IOException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void pendingConnections(
			AsynchronousServerSocketChannel asyncServerSocketChannel) {
		try {
			asyncServerSocketChannel.accept(null,
					new AsyncReceiveCompletionHandler(this.configure,
							asyncServerSocketChannel));
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
