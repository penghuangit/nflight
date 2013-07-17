package com.abreqadhabra.nflight.service.server.socket.stream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class summationServer1 {
	public static void main(String[] args) {
		try {
			int serverPort = Integer.parseInt(args[0]);
			ServerSocket calcServer = new ServerSocket(serverPort);
			while (true) {
				Socket clientSocket = calcServer.accept();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));
				int count = Integer.parseInt(br.readLine());
				int sum = 0;
				for (int ctr = 1; ctr <= count; ctr++) {
					sum += ctr;
					Thread.sleep(200);
				}
				PrintStream ps = new PrintStream(clientSocket.getOutputStream());
				ps.println(sum);
				ps.flush();
				clientSocket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
