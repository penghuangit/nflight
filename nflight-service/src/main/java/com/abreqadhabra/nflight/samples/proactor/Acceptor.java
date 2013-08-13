package com.abreqadhabra.nflight.samples.proactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements CompletionHandler {
	private SocketAddress address;
	private ServerSocketChannel ssc;
	private Proactor proactor;

	public Acceptor(Proactor p, String host, int port) {
		address = new InetSocketAddress(host, port);
		proactor = p;
	}

	public void accept() throws IOException {
		ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ssc.socket().bind(this.address);
		proactor.register(ssc, SelectionKey.OP_ACCEPT, this);
	}

	public void handleEvent(SelectionKey sk) throws IOException {
		ServerSocketChannel handle = (ServerSocketChannel) sk.channel();
		SocketChannel s = null;
		s = handle.accept();
		s.configureBlocking(false);
		Worker w = new Worker(proactor, s);
		w.work();
	}

	@Override
	public void completed(Object result, Object attachment) {
		// TODO Auto-generated method stub

	}

	@Override
	public void failed(Throwable exc, Object attachment) {
		// TODO Auto-generated method stub

	}
}