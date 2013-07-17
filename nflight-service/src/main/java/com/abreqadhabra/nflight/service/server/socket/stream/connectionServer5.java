package com.abreqadhabra.nflight.service.server.socket.stream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

class connectionServer5 {
	public static void main(String[] args) {
		try {
			int serverListenPortNum = Integer.parseInt(args[0]);
			ServerSocket connectionSocket = new ServerSocket(
					serverListenPortNum);
			Socket dataSocket = connectionSocket.accept();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					dataSocket.getInputStream()));
			int num1 = Integer.parseInt(br.readLine());
			int num2 = Integer.parseInt(br.readLine());
			System.out.println(num1 + " + " + num2 + " = " + (num1 + num2));
			dataSocket.close();
			connectionSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}