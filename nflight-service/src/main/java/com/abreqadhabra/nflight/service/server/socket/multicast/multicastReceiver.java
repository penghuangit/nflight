package com.abreqadhabra.nflight.service.server.socket.multicast;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class multicastReceiver {
	public static void main(String[] args) {
		try {
			InetAddress group = InetAddress.getByName("224.0.0.1");
			MulticastSocket multicastSock = new MulticastSocket(3456);
			multicastSock.joinGroup(group);
			byte[] buffer = new byte[100];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			multicastSock.receive(packet);
			System.out.println(new String(buffer));
			multicastSock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
