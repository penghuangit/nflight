package com.abreqadhabra.nflight.service.socket.server.stream;

import java.net.ServerSocket;
import java.net.Socket;

public class summationServer2 {
	public static void main(String[] args) {
		try {
			int serverPort = Integer.parseInt(args[0]);
			ServerSocket calcServer = new ServerSocket(serverPort);
			while (true) {
				Socket clientSocket = calcServer.accept();
				summationThread thread = new summationThread(clientSocket);
				thread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
