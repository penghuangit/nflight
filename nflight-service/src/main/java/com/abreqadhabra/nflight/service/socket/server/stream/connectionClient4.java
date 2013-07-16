package com.abreqadhabra.nflight.service.socket.server.stream;

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class connectionClient4 {
	public static void main(String[] args) {
		try {
			InetAddress serverHost = InetAddress.getByName(args[0]);
			int serverPortNum = Integer.parseInt(args[1]);
			Socket clientSocket = new Socket(serverHost, serverPortNum);
			PrintStream ps = new PrintStream(clientSocket.getOutputStream());
			ps.println(2);
			ps.flush();
			ps.println(3);
			ps.flush();
			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
