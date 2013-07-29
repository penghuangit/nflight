package com.abreqadhabra.nflight.application.server.service.socket.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Apress
 */
public class TestServer {

	private Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<>();
	private ByteBuffer buffer = ByteBuffer.allocate(2 * 1024);

	private void startEchoServer() throws ClassNotFoundException {

		final int DEFAULT_PORT = 5555;

		// open Selector and ServerSocketChannel by calling the open() method
		try (Selector selector = Selector.open();
				ServerSocketChannel serverSocketChannel = ServerSocketChannel
						.open()) {

			// check that both of them were successfully opened
			if ((serverSocketChannel.isOpen()) && (selector.isOpen())) {

				// configure non-blocking mode
				serverSocketChannel.configureBlocking(false);

				// set some options
				serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF,
						256 * 1024);
				serverSocketChannel.setOption(
						StandardSocketOptions.SO_REUSEADDR, true);

				// bind the server socket channel to port
				serverSocketChannel.bind(new InetSocketAddress(DEFAULT_PORT));

				// register the current channel with the given selector
				serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

				// display a waiting message while ... waiting!
				System.out.println("Waiting for connections ...");

				while (true) {
					// wait for incomming events
					selector.select();

					// there is something to process on selected keys
					Iterator<SelectionKey> keys = selector.selectedKeys()
							.iterator();

					while (keys.hasNext()) {
						SelectionKey key = (SelectionKey) keys.next();

						// prevent the same key from coming up again
						keys.remove();

						
		                 if (!key.isValid()) {
                             continue;
                           }

                  		
                           if (key.isReadable()) {
                               read(key);
                          //     key.interestOps(SelectionKey.OP_WRITE);
                           } else if (key.isWritable()) {
                            //   write(key);
                               key.interestOps(SelectionKey.OP_READ);
                           }
                           
//					    try {
//				            processSelectionKey(key);
//				        } catch (IOException e) {
//				            // Handle error with channel and unregister
//				        	key.cancel();
//				        	e.printStackTrace();
//				        }
	
					}
				}

			} else {
				System.out
						.println("The server socket channel or selector cannot be opened!");
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	public void processSelectionKey(SelectionKey selKey) throws IOException, ClassNotFoundException {
	    // Since the ready operations are cumulative,
	    // need to check readiness for each operation

//		System.out.println("isValid : " + selKey.isValid()
//				+ " isConnectable : " + selKey.isConnectable()
//				+ " isAcceptable : " + selKey.isAcceptable() + " isReadable : "
//				+ selKey.isReadable() + " isWritable : " + selKey.isWritable());
		
		
	    if (selKey.isValid() && selKey.isConnectable()) {
	    	
	    	System.out.println("isConnectable");

	    	
	        // Get channel with connection request
	        SocketChannel sChannel = (SocketChannel)selKey.channel();

	        boolean success = sChannel.finishConnect();
	        if (!success) {
	            // An error occurred; handle it

	            // Unregister the channel with this selector
	            selKey.cancel();
	        }
	    }
	    
//		if (selKey.isValid() && selKey.isAcceptable()) {
//	    	System.out.println("isAcceptable");
//
//			acceptOP(selKey, selector);
//		}
	     	
	    if (selKey.isValid() && selKey.isReadable()) {
	    	System.out.println("isReadable");

	        // Get channel with bytes to read
	    	this.read(selKey);
	        // See Reading from a SocketChannel
	    	System.exit(-1);
	    }
	    if (selKey.isValid() && selKey.isWritable()) {
	    	System.out.println("isWritable");

	        this.write(selKey, new Message("write"));
	        System.exit(-1);
	    }
	}
	
	// isWritable returned true
	public void write(SelectionKey key, Message message) throws IOException {

		SocketChannel channel = (SocketChannel) key.channel();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);
		objectOutputStream.writeObject(message);
		objectOutputStream.flush();
		channel.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));

	}

	// isReadable returned true
	public void read(SelectionKey key) throws IOException,
			ClassNotFoundException {
		SocketChannel channel = (SocketChannel) key.channel();

		buffer.clear();

		// we open the channel and connect
		channel.read(buffer);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				buffer.array());
		ObjectInputStream objectInputStream = new ObjectInputStream(
				byteArrayInputStream);
		Message message = (Message) objectInputStream.readObject();

		System.out
				.println(message.getClass().getSimpleName() + " : " + message);
		

	}

	// isAcceptable returned true
	private void acceptOP(SelectionKey key, Selector selector)
			throws IOException {

		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = serverChannel.accept();
		socketChannel.configureBlocking(false);

		System.out.println("Incoming connection from: "
				+ socketChannel.getRemoteAddress());

		// write an welcome message
		socketChannel.write(ByteBuffer.wrap("Hello!\n".getBytes("UTF-8")));

		// register channel with selector for further I/O
		keepDataTrack.put(socketChannel, new ArrayList<byte[]>());
		socketChannel.register(selector, SelectionKey.OP_READ);
	}

	public static void main(String[] args) throws ClassNotFoundException {
		TestServer server = new TestServer();
		server.startEchoServer();
	}
}
