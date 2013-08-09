package com.abreqadhabra.nflight.application.server.proactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Worker implements CompletionHandler<Object, Object> {
	private Proactor proactor;
	private SocketChannel socket;
	private ByteBuffer b_read;
	private ByteBuffer b_write;

	public Worker(Proactor p, SocketChannel s) {
		proactor = p;
		socket = s;
		b_read = ByteBuffer.allocateDirect(1024);
	}

	@Override
	public void completed(Object result, Object attachment) {
		// TODO Auto-generated method stub

	}

	@Override
	public void failed(Throwable exc, Object attachment) {
		// TODO Auto-generated method stub

	}

	public void work() throws IOException {
		socket.read(b_read);
		proactor.register(socket, SelectionKey.OP_READ, this);
	}

	public void handleEvent(SelectionKey sk) {
		System.out.println(new String(b_read.array()));
	}
}