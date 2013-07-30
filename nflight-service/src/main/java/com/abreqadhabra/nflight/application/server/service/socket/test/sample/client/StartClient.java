package com.abreqadhabra.nflight.application.server.service.socket.test.sample.client;

import java.net.InetAddress;

import com.abreqadhabra.nflight.application.server.service.socket.test.sample.client.runnable.NioClient;
import com.abreqadhabra.nflight.application.server.service.socket.test.sample.common.Message;
import com.abreqadhabra.nflight.application.server.service.socket.test.sample.common.RspHandler;

public class StartClient {
	public static void main(String[] args) {
		try {
			InetAddress DEFAULT_ADDRESS = InetAddress.getLocalHost();
			int DEFAULT_PORT = 5555;

			NioClient client = new NioClient(InetAddress.getByName("www.google.com"), 80);
			// NioClient client = new NioClient(DEFAULT_ADDRESS, DEFAULT_PORT);
			Thread t = new Thread(client);
			t.setDaemon(true);
			t.start();
			RspHandler handler = new RspHandler();
			client.sendObject("GET / HTTP/1.0\r\n\r\n".getBytes(), handler);
			// client.sendObject(new Message("client"), handler);
			handler.waitForResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}