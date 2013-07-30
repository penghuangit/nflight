package com.abreqadhabra.nflight.application.server.service.socket.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.service.socket.tcp.common.ChangeRequest;
import com.abreqadhabra.nflight.application.server.service.socket.tcp.impl.AcceptorWorker;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public abstract class AbstractSocketAcceptor implements SocketAcceptor,
		Runnable {
	private static final Class<AbstractSocketAcceptor> THIS_CLAZZ = AbstractSocketAcceptor.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	// The host:port combination to listen on
	private final InetAddress hostAddress;
	private final int port;

	// The channel on which we'll accept connections
	private ServerSocketChannel serverChannel;
	// The selector we'll be monitoring
	protected Selector selector;

	private final AcceptorWorker worker;

	// The buffer into which we'll read data when it's available
	private final ByteBuffer readBuffer = ByteBuffer.allocate(8192);

	// Maps a SocketChannel to a list of ByteBuffer instances
	private Map<SocketChannel, List<ByteBuffer>> pendingData = new HashMap<SocketChannel, List<ByteBuffer>>();

	// A list of PendingChange instances
	protected List<ChangeRequest> pendingChanges = new LinkedList<ChangeRequest>();

	
	public AbstractSocketAcceptor(final InetAddress hostAddress,
			final int port, final AcceptorWorker worker) throws IOException {
		this.hostAddress = hostAddress;
		this.port = port;
		this.worker = worker;
		this.init();
	}

	@Override
	public void init() throws IOException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// Create a new selector
		this.selector = SelectorProvider.provider().openSelector();
		// Create a new non-blocking acceptor socket channel
		serverChannel = ServerSocketChannel.open();

		// check that both of them were successfully opened
		if ((this.selector.isOpen()) && (serverChannel.isOpen())) {

			// configure non-blocking mode
			serverChannel.configureBlocking(false);

			// set some options
			serverChannel
					.setOption(StandardSocketOptions.SO_RCVBUF, 256 * 1024);
			serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

			// Bind the acceptor socket to the specified address and port
			final InetSocketAddress isa = new InetSocketAddress(hostAddress,
					port);
			
			// bind the acceptor socket channel to port
			serverChannel.socket().bind(isa);

			// Register the acceptor socket channel, indicating an interest in
			// accepting new connections
			// register the current channel with the given selector

			serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
		} else {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"The socket channel or selector cannot be opened!");
		}
	}
	
	@Override
	public void accept(final SelectionKey key) throws IOException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		// For an accept to be pending the channel must be a acceptor socket
		// channel.
		final ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key
				.channel();

		// Accept the connection and make it non-blocking
		final SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.socket();
		socketChannel.configureBlocking(false);
		
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,"Incoming connection from: "
				+ socketChannel.getRemoteAddress());
		
		// Register the new SocketChannel with our Selector, indicating
		// we'd like to be notified when there's data waiting to be read
		socketChannel.register(this.selector, SelectionKey.OP_READ);
	}

	@Override
	public void read(final SelectionKey key) throws IOException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		final SocketChannel socketChannel = (SocketChannel) key.channel();

		// Clear out our read buffer so it's ready for new data
		readBuffer.clear();

		// Attempt to read off the channel
		int numRead = -1;
		try {
			numRead = socketChannel.read(readBuffer);
		} catch (final IOException e) {
			// The remote forcibly closed the connection, cancel
			// the selection key and close the channel.
			key.cancel();
			socketChannel.close();
			return;
		}

		if (numRead == -1) {
			// Remote entity shut the socket down cleanly. Do the
			// same from our end and cancel the channel.
			key.channel().close();
			key.cancel();
			return;
		}

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				numRead + " bytes from " + socketChannel.getRemoteAddress());

		// Hand the data off to our worker thread
		worker.processData(this, socketChannel, readBuffer.array(), numRead);
	}

	@Override
	public void write(final SelectionKey key) throws IOException {
		final SocketChannel socketChannel = (SocketChannel) key.channel();

		synchronized (this.pendingData) {
			final List<?> queue = this.pendingData.get(socketChannel);

			// Write until there's not more data ...
			while (!queue.isEmpty()) {
				final ByteBuffer buf = (ByteBuffer) queue.get(0);
				socketChannel.write(buf);
				if (buf.remaining() > 0) {
					// ... or the socket's buffer fills up
					break;
				}
				queue.remove(0);
			}

			if (queue.isEmpty()) {
				// We wrote away all data, so we're no longer interested
				// in writing on this socket. Switch back to waiting for
				// data.
				key.interestOps(SelectionKey.OP_READ);
			}
		}

	}

	@Override
	public void send(SocketChannel socket, ByteBuffer data) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		synchronized (this.pendingChanges) {
			// Indicate we want the interest ops set changed
			this.pendingChanges.add(new ChangeRequest(socket,
					ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));
	
			// And queue the data we want written
			synchronized (this.pendingData) {
				List<ByteBuffer> queue = this.pendingData.get(socket);
				if (queue == null) {
					queue = new ArrayList<ByteBuffer>();
					this.pendingData.put(socket, queue);
				}
				queue.add(data);
			}
		}

		// Finally, wake up our selecting thread so it can make the required
		// changes
		this.selector.wakeup();
	}

}
