package com.abreqadhabra.nflight.application.examples.forkjoin;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

public class AsyncCompletionHandler
		implements
			CompletionHandler<AsynchronousSocketChannel, Void> {

	ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
	AsynchronousServerSocketChannel channel;

	public AsyncCompletionHandler(AsynchronousServerSocketChannel channel) {
		this.channel = channel;
	}

	@Override
	public void completed(AsynchronousSocketChannel result, Void attachment) {

		channel.accept(null, this);

		try {
			System.out.println("Incoming connection from: "
					+ result.getRemoteAddress());

			// transmitting data
			while (result.read(buffer).get() != -1) {

				buffer.flip();

				result.write(buffer).get();

				if (buffer.hasRemaining()) {
					buffer.compact();
				} else {
					buffer.clear();
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
		channel.accept(null, this);
		throw new UnsupportedOperationException("Cannot accept cponnections!");
	}
}