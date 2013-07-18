package com.abreqadhabra.nflight.samples.socket.multicast;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class multicastSender {
	public static void main(String[] args) {
		try {
			InetAddress group = InetAddress.getByName("224.0.0.1");
			MulticastSocket multicastSock = new MulticastSocket(3456);
			String msg = "Hello How are you?";
			DatagramPacket packet = new DatagramPacket(msg.getBytes(),
					msg.length(), group, 3456);
			multicastSock.send(packet);
			multicastSock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}