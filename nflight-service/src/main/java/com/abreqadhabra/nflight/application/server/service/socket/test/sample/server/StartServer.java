package com.abreqadhabra.nflight.application.server.service.socket.test.sample.server;

import java.io.IOException;
import java.net.InetAddress;

import com.abreqadhabra.nflight.application.server.service.socket.test.sample.server.runnable.StreamServerImpl;

public class StartServer {
	public static void main(String[] args) {

		try {
			InetAddress DEFAULT_ADDRESS = InetAddress.getLocalHost();
			int DEFAULT_PORT = 5555;

			EchoWorker worker = new EchoWorker();
			new Thread(worker).start();
			new Thread(new StreamServerImpl(DEFAULT_ADDRESS, DEFAULT_PORT,
					worker)).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}