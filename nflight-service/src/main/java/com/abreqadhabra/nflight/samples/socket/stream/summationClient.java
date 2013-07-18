package com.abreqadhabra.nflight.samples.socket.stream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class summationClient {
	public static void main(String[] args) {
		try {
			InetAddress serverHost = InetAddress.getByName(args[0]);
			int serverPort = Integer.parseInt(args[1]);
			long startTime = System.currentTimeMillis();
			int count = Integer.parseInt(args[2]);
			Socket clientSocket = new Socket(serverHost, serverPort);
			PrintStream ps = new PrintStream(clientSocket.getOutputStream());
			ps.println(count);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			int sum = Integer.parseInt(br.readLine());
			System.out.println(" sum = " + sum);
			long endTime = System.currentTimeMillis();
			System.out
					.println("Time consumed for receiving the feedback from the server:"
							+ (endTime - startTime) + " milliseconds");
			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
