package com.abreqadhabra.nflight.application.service.net.stream.nonblocking;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.NetworkServiceHelper;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class NonBlockingServerSessionImpl implements ServerSession {
	private static final Class<NonBlockingServerSessionImpl> THIS_CLAZZ = NonBlockingServerSessionImpl.class;
	private static final String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private Configure configure;
	private SelectionKey selectionKey;
	private SocketChannel socket;

	public NonBlockingServerSessionImpl(Configure configure,
			SocketChannel socket, SelectionKey selectionKey) {
		this.configure = configure;
		this.selectionKey = selectionKey;
		this.socket = socket;
		// this.factory = factory;
		// if (zk != null) {
		// outstandingLimit = zk.getGlobalOutstandingLimit();
		// }
		// sock.socket().setTcpNoDelay(true);
		// sock.socket().setSoLinger(true, 2);
		// InetAddress addr = ((InetSocketAddress) sock.socket()
		// .getRemoteSocketAddress()).getAddress();
		// authInfo.add(new Id("ip", addr.getHostAddress()));
	//	selectionKey.interestOps(SelectionKey.OP_READ);
	}

	@Override
	public void send(ServerSession session) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				NetworkServiceHelper.getReadySetString(selectionKey));
		
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"send");
	}

	@Override
	public void receive(ServerSession session) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				NetworkServiceHelper.getReadySetString(selectionKey));
		
		try {
			int capacity = configure
					.getInt(Configure.NONBLOCKING_INCOMING_BUFFER_CAPACITY);
			ByteBuffer incomingByteBuffer = NetworkServiceHelper.getByteBuffer(capacity);
			int numRead = socket.read(incomingByteBuffer);
			incomingByteBuffer.flip();
			if (incomingByteBuffer.hasRemaining()) {
				incomingByteBuffer.compact();
			} else {
				incomingByteBuffer.clear();
			}

			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getSimpleName(),
					METHOD_NAME,
					new String(incomingByteBuffer.array(), "UTF-8") + " ["
							+ numRead + " bytes] from "
							+ this.socket.getRemoteAddress());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				this.socket.close();
				LOGGER.logp(
						Level.FINER,
						THIS_CLAZZ.getSimpleName(),
						METHOD_NAME,
						"isOpen=" + Boolean.toString(this.socket.isOpen())
								+ ", isConnected="
								+ Boolean.toString(this.socket.isConnected()));
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

}
