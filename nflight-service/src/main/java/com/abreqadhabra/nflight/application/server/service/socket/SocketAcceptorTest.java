package com.abreqadhabra.nflight.application.server.service.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.service.socket.client.runnable.DatagramAcceptorClient;
import com.abreqadhabra.nflight.application.server.service.socket.client.runnable.StreamAcceptorClient;
import com.abreqadhabra.nflight.application.server.service.socket.tcp.common.Message;
import com.abreqadhabra.nflight.application.server.service.socket.tcp.common.ResponseHandler;
import com.abreqadhabra.nflight.application.server.service.socket.tcp.impl.AcceptorWorker;
import com.abreqadhabra.nflight.application.server.service.socket.tcp.impl.StreamAcceptorImpl;
import com.abreqadhabra.nflight.application.server.service.socket.udp.impl.DatagramAcceptorImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class SocketAcceptorTest {
	private static final Class<SocketAcceptorTest> THIS_CLAZZ = SocketAcceptorTest.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static void main(String[] args) throws Exception {

		InetAddress DEFAULT_ADDRESS = InetAddress.getLocalHost();
		int DEFAULT_PORT = 5555;

	//	testStreamAcceptor(DEFAULT_ADDRESS, DEFAULT_PORT);

		DEFAULT_PORT = 6666;

		testDatagramAcceptor(DEFAULT_ADDRESS, DEFAULT_PORT);

		// System.exit(0);

	}

	private static void testDatagramAcceptor(InetAddress DEFAULT_ADDRESS,
			int DEFAULT_PORT) throws Exception {

		startDatagramAcceptor(DEFAULT_ADDRESS, DEFAULT_PORT);
		Thread.sleep(1000);
		startDatagramAcceptorClient(DEFAULT_ADDRESS, DEFAULT_PORT, new Message(
				"shutdown"));

	}

	private static void startDatagramAcceptorClient(
			InetAddress DEFAULT_ADDRESS,
			int DEFAULT_PORT, Object sendObject) throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		// SocketClient client = new
		// SocketClient(InetAddress.getByName("www.google.com"), 80);
		DatagramAcceptorClient client = new DatagramAcceptorClient(
				DEFAULT_ADDRESS, DEFAULT_PORT);
		Thread t = new Thread(client);

		t.setDaemon(true);
		t.start();
		ResponseHandler handler = new ResponseHandler();
		// client.sendObject("GET / HTTP/1.0\r\n\r\n".getBytes(), handler);
		client.sendObject(sendObject, handler);

		Object object = handler.waitForResponse();
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				object.toString());

	}

	private static void startDatagramAcceptor(InetAddress DEFAULT_ADDRESS,
			int DEFAULT_PORT) throws Exception {

		AcceptorWorker worker = new AcceptorWorker();
		new Thread(worker).start();
		new Thread(new DatagramAcceptorImpl(DEFAULT_ADDRESS, DEFAULT_PORT,
				worker)).start();

	}

	private static void testStreamAcceptor(InetAddress DEFAULT_ADDRESS,
			int DEFAULT_PORT) {
		try {
			startStreamAcceptor(DEFAULT_ADDRESS, DEFAULT_PORT);
			Thread.sleep(1000);
			startStreamAcceptorClient(DEFAULT_ADDRESS, DEFAULT_PORT,
					new Message("shutdown"));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void startStreamAcceptor(InetAddress DEFAULT_ADDRESS,
			int DEFAULT_PORT) throws IOException {

		AcceptorWorker worker = new AcceptorWorker();
		new Thread(worker).start();
		new Thread(
				new StreamAcceptorImpl(DEFAULT_ADDRESS, DEFAULT_PORT, worker))
				.start();

	}

	private static void startStreamAcceptorClient(InetAddress DEFAULT_ADDRESS,
			int DEFAULT_PORT, Object sendObject) throws IOException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// SocketClient client = new
		// SocketClient(InetAddress.getByName("www.google.com"), 80);
		StreamAcceptorClient client = new StreamAcceptorClient(DEFAULT_ADDRESS,
				DEFAULT_PORT);
		Thread t = new Thread(client);

		t.setDaemon(true);
		t.start();
		ResponseHandler handler = new ResponseHandler();
		// client.sendObject("GET / HTTP/1.0\r\n\r\n".getBytes(), handler);
		client.sendObject(sendObject, handler);

		Object object = handler.waitForResponse();
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				object.toString());

	}

}