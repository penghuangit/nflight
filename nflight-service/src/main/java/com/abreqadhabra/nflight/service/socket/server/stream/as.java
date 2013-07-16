package com.abreqadhabra.nflight.service.socket.server.stream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

class connectionServer2 {
	public static void main(String[] args) {
		try {
			String message = args[0];
			int serverPortNum = Integer.parseInt(args[1]);
			ServerSocket connectionSocket = new ServerSocket(serverPortNum);
			Socket dataSocket = connectionSocket.accept();
			PrintStream socketOutput = new PrintStream(
					dataSocket.getOutputStream());
			socketOutput.println(message);
			socketOutput.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					dataSocket.getInputStream()));
			System.out.println(br.readLine());
			dataSocket.close();
			connectionSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}