package com.abreqadhabra.nflight.samples.socket.stream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

class connectionClient1 {
	public static void main(String[] args) {
		try {
			InetAddress acceptorHost = InetAddress.getByName(args[0]);
			int serverPortNum = Integer.parseInt(args[1]);
			Socket clientSocket = new Socket(acceptorHost, serverPortNum);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			System.out.println(br.readLine());
			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}