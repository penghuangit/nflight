package com.abreqadhabra.nflight.samples.proactor;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

public class Proactor {
	private Selector selector;

	public Proactor() throws IOException {
		selector = SelectorProvider.provider().openSelector();
	}

	public void register(SelectableChannel handle, int eventType,
			Object completionHandler) throws IOException {
		handle.register(selector, eventType, completionHandler);
	}

	public void handleEvent() throws IOException {
		while (selector.select() > 0) {
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator<SelectionKey> i = readyKeys.iterator();
			while (i.hasNext()) {
				SelectionKey sk = (SelectionKey) i.next();
				i.remove();
				((Acceptor) sk.attachment()).handleEvent(sk);
			}
		}
	}
}