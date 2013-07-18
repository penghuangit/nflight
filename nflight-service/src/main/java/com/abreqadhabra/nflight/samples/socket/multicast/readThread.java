package com.abreqadhabra.nflight.samples.socket.multicast;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class readThread extends Thread {
	InetAddress group;
	int multicastPort;
	int MAX_MSG_LEN = 100;

	readThread(InetAddress g, int port) {
		group = g;
		multicastPort = port;
	}

	public void run() {
		try {
			MulticastSocket readSocket = new MulticastSocket(multicastPort);
			readSocket.joinGroup(group);
			while (true) {
				byte[] message = new byte[MAX_MSG_LEN];
				DatagramPacket packet = new DatagramPacket(message,
						message.length, group, multicastPort);
				readSocket.receive(packet);
				String msg = new String(packet.getData());
				System.out.println(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
