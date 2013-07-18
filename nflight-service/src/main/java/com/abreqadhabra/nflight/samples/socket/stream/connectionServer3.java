package com.abreqadhabra.nflight.samples.socket.stream;

import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

class connectionServer3 {
	public static void main(String[] args) {
		try {
			String message = args[0];
			int serverPortNum = Integer.parseInt(args[1]);
			ServerSocket connectionSocket = new ServerSocket(serverPortNum);
			while (true) {
				Socket dataSocket = connectionSocket.accept();
				PrintStream socketOutput = new PrintStream(
						dataSocket.getOutputStream());
				socketOutput.println(message);
				socketOutput.flush();
				dataSocket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}