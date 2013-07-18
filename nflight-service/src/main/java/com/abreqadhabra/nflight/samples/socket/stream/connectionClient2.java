package com.abreqadhabra.nflight.samples.socket.stream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

class connectionClient2 {
	public static void main(String[] args) {
		try {
			InetAddress acceptorHost = InetAddress.getByName(args[0]);
			int serverPortNum = Integer.parseInt(args[1]);
			Socket clientSocket = new Socket(acceptorHost, serverPortNum);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			System.out.println(br.readLine());
			PrintStream ps = new PrintStream(clientSocket.getOutputStream());
			ps.println("received your message.. Thanks");
			ps.flush();
			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}