package com.abreqadhabra.nflight.application.server.service.socket.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.service.socket.tcp.client.runnable.SocketClient;
import com.abreqadhabra.nflight.application.server.service.socket.tcp.common.Message;
import com.abreqadhabra.nflight.application.server.service.socket.tcp.common.ResponseHandler;
import com.abreqadhabra.nflight.application.server.service.socket.tcp.impl.AcceptorWorker;
import com.abreqadhabra.nflight.application.server.service.socket.tcp.impl.StreamAcceptorImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class StreamAcceptorTest {
	private static final Class<StreamAcceptorTest> THIS_CLAZZ = StreamAcceptorTest.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static void main(String[] args) {

		try {
			InetAddress DEFAULT_ADDRESS = InetAddress.getLocalHost();
			int DEFAULT_PORT = 5555;

			startServer(DEFAULT_ADDRESS, DEFAULT_PORT);
			Thread.sleep(1000);
			startClient(DEFAULT_ADDRESS, DEFAULT_PORT, new Message("가나다라"));
			Thread.sleep(1000);
			startClient(DEFAULT_ADDRESS, DEFAULT_PORT, new Message("家"));
			System.exit(-2);

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void startClient(InetAddress DEFAULT_ADDRESS,
			int DEFAULT_PORT, Object sendObject) throws IOException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		// SocketClient client = new
		// SocketClient(InetAddress.getByName("www.google.com"), 80);
		SocketClient client = new SocketClient(DEFAULT_ADDRESS, DEFAULT_PORT);
		Thread t = new Thread(client);
		t.setDaemon(true);
		t.start();
		ResponseHandler handler = new ResponseHandler();
		// client.sendObject("GET / HTTP/1.0\r\n\r\n".getBytes(), handler);
		client.sendObject(sendObject, handler);

		Object object = handler.waitForResponse();
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,object.toString());

	}

	private static void startServer(InetAddress DEFAULT_ADDRESS,
			int DEFAULT_PORT) throws IOException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		AcceptorWorker worker = new AcceptorWorker();
		new Thread(worker).start();
		new Thread(
				new StreamAcceptorImpl(DEFAULT_ADDRESS, DEFAULT_PORT, worker))
				.start();

	}
}