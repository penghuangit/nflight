package com.abreqadhabra.nflight.application.server.concurrent.runnable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.service.IService;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

//Strategy ConcreteStrategy
public class StreamAcceptor implements IService {

	private static Class<StreamAcceptor> THIS_CLAZZ = StreamAcceptor.class;
	private static Logger LOGGER = LoggingHelper
			.getLogger(StreamAcceptor.THIS_CLAZZ);

	AsynchronousChannelGroup threadGroup = null;
	private AsynchronousServerSocketChannel asyncServerSocketChannel;
	private boolean isOpen;

	public StreamAcceptor(String address, int port)
			throws IOException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		ExecutorService executorService = Executors
				.newCachedThreadPool(Executors.defaultThreadFactory());
		try {
			threadGroup = AsynchronousChannelGroup.withCachedThreadPool(
					executorService, 1);
		} catch (IOException ex) {
			System.err.println(ex);
		}

		// create asynchronous acceptor-socket channel bound to the default group
		try {
			asyncServerSocketChannel = AsynchronousServerSocketChannel
					.open(threadGroup);

			// check if it the channel was successfully opened
			isOpen = asyncServerSocketChannel.isOpen();

			if (isOpen) {
				// set some options
				asyncServerSocketChannel.setOption(
						StandardSocketOptions.SO_RCVBUF, 4 * 1024);
				asyncServerSocketChannel.setOption(
						StandardSocketOptions.SO_REUSEADDR, true);
				// bind the acceptor-socket channel to local socketAddress
				asyncServerSocketChannel.bind(new InetSocketAddress(address,
						port));

				// display a waiting message while ... waiting clients
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"Waiting for connections ...");

				this.startup();

			} else {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
						"The channel cannot be opened!");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	

	@Override
	public void startup() throws Exception {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				Boolean.toString(isOpen));

		if (isOpen) {

			ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

			asyncServerSocketChannel.accept(null,
					new StreamAcceptorServerHandler(asyncServerSocketChannel));
			// Wait
			System.in.read();
		} else {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"The asynchronous acceptor-socket channel cannot be opened!");
		}
	}

	@Override
	public boolean isRunning() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String sayHello() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void shutdown() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean status() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DatagramChannel getDatagramChannel() {
		// TODO Auto-generated method stub
		return null;
	}

}
