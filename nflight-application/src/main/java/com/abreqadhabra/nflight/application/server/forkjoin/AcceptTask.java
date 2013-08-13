package com.abreqadhabra.nflight.application.server.forkjoin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class AcceptTask extends RecursiveTask<AsynchronousServerSocketChannel> {
	private static Class<AcceptTask> THIS_CLAZZ = AcceptTask.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private static long serialVersionUID = 1L;

	InetSocketAddress socketAddress;

	public AcceptTask(InetSocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}

	@Override
	protected AsynchronousServerSocketChannel compute() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// create asynchronous server-socket channel bound to the default group
		try (AsynchronousServerSocketChannel channel = AsynchronousServerSocketChannel
				.open()) {

			if (channel.isOpen()) {

				// set some options
				channel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
				channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
				// bind the server-socket channel to local address
				channel.bind(this.socketAddress);

				LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME,
						"바인딩된 서버 소켓 채널 주소 :" + channel.getLocalAddress());

				// display a waiting message while ... waiting clients
				System.out.println("Waiting for connections ...");

				channel.accept(null, new AsyncCompletionHandler(channel));

				// Wait

				System.in.read();

			} else {
				System.out
						.println("The asynchronous server-socket channel cannot be opened!");
			}
			return channel;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	class AsyncCompletionHandler
			implements
				CompletionHandler<AsynchronousSocketChannel, Void> {

		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		AsynchronousServerSocketChannel channel;

		public AsyncCompletionHandler(AsynchronousServerSocketChannel channel) {
			this.channel = channel;
		}

		@Override
		public void completed(AsynchronousSocketChannel result, Void attachment) {

			this.channel.accept(null, this);
			try {
				System.out.println("Incoming connection from: "
						+ result.getRemoteAddress());
				// transmitting data
				while (result.read(this.buffer).get() != -1) {
					this.buffer.flip();
					result.write(this.buffer).get();
					if (this.buffer.hasRemaining()) {
						this.buffer.compact();
					} else {
						this.buffer.clear();
					}
				}
			} catch (IOException | InterruptedException | ExecutionException ex) {
				System.err.println(ex);
			} finally {
				try {
					result.close();
				} catch (IOException e) {
					System.err.println(e);
				}
			}
		}

		@Override
		public void failed(Throwable exc, Void attachment) {
			this.channel.accept(null, this);
			throw new UnsupportedOperationException(
					"Cannot accept cponnections!");
		}
	}

}