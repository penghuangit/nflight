package com.abreqadhabra.nflight.samples.socket.stream;

import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

class connectionServer1 {
	public static void main(String[] args) {
		try {
			String message = args[0];
			int serverPortNumber = Integer.parseInt(args[1]);
			ServerSocket connectionSocket = new ServerSocket(serverPortNumber);
			Socket dataSocket = connectionSocket.accept();
			PrintStream socketOutput = new PrintStream(
					dataSocket.getOutputStream());
			socketOutput.println(message);
			System.out.println("sent responseByteBuffer to client…");
			socketOutput.flush();
			dataSocket.close();
			connectionSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}