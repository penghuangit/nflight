package com.abreqadhabra.nflight.application.service.net.stream;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class BlockingNetworkClientTest {
	private static final Class<BlockingNetworkClientTest> THIS_CLAZZ = BlockingNetworkClientTest.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static void main(final String[] args) throws UnknownHostException {

		InetAddress DEFAULT_ADDRESS = InetAddress.getLocalHost();
		int ASYNC_DEFAULT_PORT = 7777;
		int BLOCKING_DEFAULT_PORT = 8888;
		int NON_BLOCKING_DEFAULT_PORT = 9999;

		
		ThreadGroup clientThreadGroup = new ThreadGroup(
				"NF-Service-Client-ThreadGroup");
		
		InetSocketAddress socketAddress = new InetSocketAddress(
				DEFAULT_ADDRESS, BLOCKING_DEFAULT_PORT);

		new Thread(clientThreadGroup, blockingTypeTest(socketAddress, 15000), "NF-Service-Client-Blocking").start();

		socketAddress = new InetSocketAddress(DEFAULT_ADDRESS,
				NON_BLOCKING_DEFAULT_PORT);

		new Thread(clientThreadGroup, blockingTypeTest(socketAddress, 15000), "NF-Service-Client-Non-Blocking").start();
		
		socketAddress = new InetSocketAddress(DEFAULT_ADDRESS,
				ASYNC_DEFAULT_PORT);

		new Thread(clientThreadGroup, asyncTypeTest(socketAddress, 15000), "NF-Service-Client-Async").start();
		
	}

	private static Runnable asyncTypeTest(InetSocketAddress socketAddress, int num) {
		return new Runnable() {

			InetSocketAddress socketAddress;
			int num;

			public Runnable init(InetSocketAddress socketAddress, int num) {
				this.socketAddress = socketAddress;
				this.num = num;
				return (this);
			}

			@Override
			public void run() {
				final String METHOD_NAME = Thread.currentThread()
						.getStackTrace()[1].getMethodName();

				if (LOGGER.isLoggable(Level.FINER)) {
					String currentThreadName = Thread.currentThread().getName();
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
							METHOD_NAME, "current thread is "
									+ currentThreadName);
				}

				for (int i = 0; i < num; i++) {
					try {
						AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
						
						LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
								METHOD_NAME, socketAddress.toString());
						
						channel.connect(socketAddress).get();
						channel.write(ByteBuffer
								.wrap(("Hello World! ---------------------> " + socketAddress
										.toString()).getBytes())).get();
						Thread.sleep(10);
						System.out.println(i+1 +":" +channel );
					} catch (IOException | InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			}

		}.init(socketAddress, num);
	}

	private static Runnable blockingTypeTest(InetSocketAddress socketAddress, int num) {
		return new Runnable() {

			InetSocketAddress socketAddress;
			int num;

			public Runnable init(InetSocketAddress socketAddress, int num) {
				this.socketAddress = socketAddress;
				this.num = num;
				return (this);
			}

			@Override
			public void run() {
				final String METHOD_NAME = Thread.currentThread()
						.getStackTrace()[1].getMethodName();

				if (LOGGER.isLoggable(Level.FINER)) {
					String currentThreadName = Thread.currentThread().getName();
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
							METHOD_NAME, "current thread is "
									+ currentThreadName);
				}

				for (int i = 0; i < num; i++) {
					try {
						SocketChannel channel = SocketChannel.open();
						
						LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
								METHOD_NAME, socketAddress.toString());
						
						channel.connect(socketAddress);
						channel.write(ByteBuffer
								.wrap(("Hello World! ---------------------> " + socketAddress
										.toString()).getBytes()));
						Thread.sleep(10);
						System.out.println(i+1 +":" +channel );
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}.init(socketAddress, num);
	}

}