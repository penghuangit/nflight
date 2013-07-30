package com.abreqadhabra.nflight.application.server.service.socket.tcp.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.service.socket.tcp.AbstractSocketAcceptor;
import com.abreqadhabra.nflight.application.server.service.socket.tcp.common.ChangeRequest;
import com.abreqadhabra.nflight.application.server.service.socket.tcp.common.ResponseHandler;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class StreamAcceptorImpl extends AbstractSocketAcceptor {
	private static final Class<StreamAcceptorImpl> THIS_CLAZZ = StreamAcceptorImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public StreamAcceptorImpl(final InetAddress hostAddress, final int port,
			final AcceptorWorker worker) throws IOException {
		super(hostAddress, port, worker);
	}

	@Override
	public void run() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// display a waiting message while ... waiting!
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"Waiting for connections ...");

		while (true) {
			try {
				// Process any pending changes
				synchronized (pendingChanges) {
					final Iterator<ChangeRequest> changes = pendingChanges
							.iterator();
					while (changes.hasNext()) {
						final ChangeRequest change = changes.next();
						switch (change.type) {
						case ChangeRequest.CHANGEOPS:
							final SelectionKey key = change.socket
									.keyFor(super.selector);

							key.interestOps(change.ops);
						}
					}
					pendingChanges.clear();
				}

				// Wait for an event one of the registered channels
				selector.select();

				// Iterate over the set of keys for which events are available
				final Iterator<?> selectedKeys = selector.selectedKeys()
						.iterator();
				while (selectedKeys.hasNext()) {
					final SelectionKey key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();

					if (!key.isValid()) {
						continue;
					}

					// Check what event is available and deal with it
					if (key.isAcceptable()) {
						this.accept(key);
					} else if (key.isReadable()) {
						this.read(key);
					} else if (key.isWritable()) {
						this.write(key);
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void execute(final DataEvent dataEvent) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Object object = ResponseHandler.deserializeObject(dataEvent.data);
		if (object == null) {
			object = new String(dataEvent.data);
		}

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				object.toString());

		final String response = "acceptor";

		final ByteBuffer data = ResponseHandler.serializeObject(response);

		this.send(dataEvent.socket, data);

	}
}