package com.abreqadhabra.nflight.application.server.concurrent.runnable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

public class StreamAcceptorServerHandler implements
		CompletionHandler<AsynchronousSocketChannel, Void> {

    final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
	private final AsynchronousServerSocketChannel asyncServerSocketChannel;

	public StreamAcceptorServerHandler(
			final AsynchronousServerSocketChannel asyncServerSocketChannel) {
		this.asyncServerSocketChannel = asyncServerSocketChannel;
	}

	@Override
	public void completed(final AsynchronousSocketChannel result,
			final Void attachment) {

		this.asyncServerSocketChannel.accept(null, this);

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
			} catch (final IOException e) {
				System.err.println(e);
			}
		}
	}

	@Override
	public void failed(final Throwable exc, final Void attachment) {
		this.asyncServerSocketChannel.accept(null, this);
		throw new UnsupportedOperationException("Cannot accept cponnections!");
	}

}
