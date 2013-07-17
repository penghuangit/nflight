package com.abreqadhabra.nflight.service.server.socket.datagram;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

class datagramReceiver {
	public static void main(String[] args) {
		try {
			int MAX_LEN = 40;
			int localPortNum = Integer.parseInt(args[0]);
			DatagramSocket mySocket = new DatagramSocket(localPortNum);
			byte[] buffer = new byte[MAX_LEN];
			DatagramPacket packet = new DatagramPacket(buffer, MAX_LEN);
			mySocket.receive(packet);
			String message = new String(buffer);
			System.out.println(message);
			mySocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}