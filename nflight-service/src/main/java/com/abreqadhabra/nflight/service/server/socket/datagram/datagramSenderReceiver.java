package com.abreqadhabra.nflight.service.server.socket.datagram;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class datagramSenderReceiver {
	public static void main(String[] args) {
		try {
			InetAddress receiverHost = InetAddress.getByName(args[0]);
			int receiverPort = Integer.parseInt(args[1]);
			String message = args[2];
			DatagramSocket mySocket = new DatagramSocket();
			byte[] sendBuffer = message.getBytes();
			DatagramPacket packet = new DatagramPacket(sendBuffer,
					sendBuffer.length, receiverHost, receiverPort);
			mySocket.send(packet);
			// to receive a message
			int MESSAGE_LEN = 60;
			byte[] recvBuffer = new byte[MESSAGE_LEN];
			DatagramPacket datagram = new DatagramPacket(recvBuffer,
					MESSAGE_LEN);
			mySocket.receive(datagram);
			String recvdString = new String(recvBuffer);
			System.out.println("\n" + recvdString);
			mySocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}