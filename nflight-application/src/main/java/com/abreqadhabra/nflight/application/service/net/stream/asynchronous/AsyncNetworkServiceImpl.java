package com.abreqadhabra.nflight.application.service.net.stream.asynchronous;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.AbstractNetworkServiceImpl;
import com.abreqadhabra.nflight.application.service.net.stream.asynchronous.transport.AsyncAcceptor;

public class AsyncNetworkServiceImpl extends AbstractNetworkServiceImpl {

	public AsyncNetworkServiceImpl(Configure configure,
			ThreadPoolExecutor threadPool, InetSocketAddress endpoint) {
		super(configure, threadPool, endpoint);

	}

	@Override
	public void startup() {
		try {
			// wait for incoming connections
			AsyncAcceptor acceptor = new AsyncAcceptor(this.endpoint,
					this.threadPool, this.configure);
			new Thread(acceptor).start();
		} catch (IOException | InterruptedException | ExecutionException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

}
