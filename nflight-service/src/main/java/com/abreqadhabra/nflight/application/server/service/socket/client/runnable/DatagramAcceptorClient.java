package com.abreqadhabra.nflight.application.server.service.socket.client.runnable;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.service.socket.tcp.common.ChangeRequest;
import com.abreqadhabra.nflight.application.server.service.socket.tcp.common.ResponseHandler;
import com.abreqadhabra.nflight.application.server.service.socket.udp.common.Attachment;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class DatagramAcceptorClient implements Runnable {
	private static final Class<DatagramAcceptorClient> THIS_CLAZZ = DatagramAcceptorClient.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	// The host:port combination to connect to
	private InetAddress hostAddress;
	private int port;

	// The selector we'll be monitoring
	private Selector selector;

	// The buffer into which we'll read data when it's available
	private ByteBuffer readBuffer = ByteBuffer.allocate(8192);

	// A list of PendingChange instances
	private List<ChangeRequest> pendingChanges = new LinkedList<ChangeRequest>();

	// Maps a SocketChannel to a list of ByteBuffer instances
	private Map<DatagramChannel, List> pendingData = new HashMap<DatagramChannel, List>();

	// Maps a SocketChannel to a ResponseHandler
	private Map<DatagramChannel, ResponseHandler> rspHandlers = Collections
			.synchronizedMap(new HashMap<DatagramChannel, ResponseHandler>());

	public DatagramAcceptorClient(InetAddress hostAddress, int port)
			throws IOException {
		this.hostAddress = hostAddress;
		this.port = port;
		this.selector = this.initSelector();
	}

	public void sendObject(Object object, ResponseHandler handler)
			throws IOException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		// Start a new connection
		DatagramChannel datagram = this.initiateConnection();

		// Register the response handler
		this.rspHandlers.put(datagram, handler);

		
		
		// And queue the data we want written
		synchronized (this.pendingData) {
			List<ByteBuffer> queue = this.pendingData.get(datagram);
			

			if (queue == null) {
				queue = new ArrayList<ByteBuffer>();
				this.pendingData.put(datagram, queue);
			}
			
			queue.add(ResponseHandler.serializeObject(object));


		}

		// Finally, wake up our selecting thread so it can make the required
		// changes
		this.selector.wakeup();
	}

	@Override
	public void run() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		final String orgName = Thread.currentThread().getName();
		Thread.currentThread().setName(
				orgName + "-" + THIS_CLAZZ.getSimpleName());

		while (true) {
			try {
				// Process any pending changes
				synchronized (this.pendingChanges) {
					Iterator<ChangeRequest> changes = this.pendingChanges
							.iterator();
					while (changes.hasNext()) {
						ChangeRequest change = changes.next();
						switch (change.type) {
						case ChangeRequest.CHANGE_OPS:
							SelectionKey key = change.datagramChannel
									.keyFor(this.selector);

							LOGGER.logp(
									Level.FINER,
									THIS_CLAZZ.getSimpleName(),
									METHOD_NAME,
									"보류 변경 유형: "
											+ change.type
											+ ":"
											+ ChangeRequest.getOpsName(key
													.interestOps())
											+ "->"
											+ ChangeRequest
													.getOpsName(change.ops)
											+ "\n" + change);
							
							key.interestOps(change.ops);
							break;
						case ChangeRequest.REGISTER:

							LOGGER.logp(
									Level.FINER,
									THIS_CLAZZ.getSimpleName(),
									METHOD_NAME,
									"보류 변경 유형: "
											+ change.type
											+ ":"
											+ this.selector
											+ "->"
											+ ChangeRequest
													.getOpsName(change.ops)
											+ "\n" + change);
							
							change.datagramChannel.register(this.selector,
									change.ops);
							break;
						}
						
					}
					this.pendingChanges.clear();
				}

				// Wait for an event one of the registered channels
				this.selector.select();

				// Iterate over the set of keys for which events are available
				Iterator<?> selectedKeys = this.selector.selectedKeys()
						.iterator();
				while (selectedKeys.hasNext()) {
					SelectionKey key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();

					if (!key.isValid()) {
						continue;
					}

					// Check what event is available and deal with it
					if (key.isReadable()) {
						this.read(key);
					} else if (key.isWritable()) {
						this.write(key);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void read(SelectionKey key) throws IOException {
		DatagramChannel datagram = (DatagramChannel) key.channel();

		// Clear out our read buffer so it's ready for new data
		this.readBuffer.clear();

		// Attempt to read off the channel
		int numRead;
		try {
			numRead = datagram.read(this.readBuffer);
		} catch (IOException e) {
			// The remote forcibly closed the connection, cancel
			// the selection key and close the channel.
			key.cancel();
			datagram.close();
			return;
		}

		if (numRead == -1) {
			// Remote entity shut the socket down cleanly. Do the
			// same from our end and cancel the channel.
			key.channel().close();
			key.cancel();
			return;
		}

		// Handle the response
		this.handleResponse(datagram, this.readBuffer.array(), numRead);
	}

	private void handleResponse(DatagramChannel datagram, byte[] data,
			int numRead) throws IOException {
		// Make a correctly sized copy of the data before handing it
		// to the client
		byte[] rspData = new byte[numRead];
		System.arraycopy(data, 0, rspData, 0, numRead);

		// Look up the handler for this channel
		ResponseHandler handler = this.rspHandlers.get(datagram);

		// And pass the response to it
		if (handler.handleResponse(rspData)) {
			// The handler has seen enough, close the connection
			datagram.close();
			datagram.keyFor(this.selector).cancel();
		}
	}

	private void write(SelectionKey key) throws IOException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		DatagramChannel datagram = (DatagramChannel) key.channel();

		synchronized (this.pendingData) {
			List<?> queue = this.pendingData.get(datagram);


			// Write until there's not more data ...
			while (!queue.isEmpty()) {
				ByteBuffer buf = (ByteBuffer) queue.get(0);
				
			
				//datagram.write(buf);
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

	private DatagramChannel initiateConnection() throws IOException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		DatagramChannel datagramChannel = DatagramChannel
				.open(StandardProtocolFamily.INET);
		datagramChannel.configureBlocking(false);

		// set some options
		datagramChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
		datagramChannel.setOption(StandardSocketOptions.SO_SNDBUF, 4 * 1024);

		// Kick off connection establishment
		datagramChannel.connect(new InetSocketAddress(this.hostAddress,
				this.port));

		// check if it the channel was successfully connected
		if (datagramChannel.isConnected()) {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
					METHOD_NAME,"the channel was successfully connected!");
		} else {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
					METHOD_NAME, "The channel cannot be connected!");
		}
		
		// Queue a channel registration since the caller is not the
		// selecting thread. As part of the registration we'll register
		// an interest in connection events. These are raised when a channel
		// is ready to complete connection establishment.
		synchronized (this.pendingChanges) {
			this.pendingChanges.add(new ChangeRequest(datagramChannel,
					ChangeRequest.REGISTER, SelectionKey.OP_WRITE));
		}

		return datagramChannel;
	}

	private Selector initSelector() throws IOException {
		// Create a new selector
		return SelectorProvider.provider().openSelector();
	}

}
