package com.abreqadhabra.nflight.samples.socket.multicast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class multicastSenderReceiver {
	public static void main(String[] args) {
		try {
			int multicastPort = 3456;
			InetAddress group = InetAddress.getByName("224.0.0.1");
			MulticastSocket socket = new MulticastSocket(multicastPort);
			readThread rt = new readThread(group, multicastPort);
			rt.start();
			String message = args[0];
			byte[] msg = message.getBytes();
			DatagramPacket packet = new DatagramPacket(msg, msg.length, group,
					multicastPort);
			System.out.print("Hit return to send message\n\n");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			br.readLine();
			socket.send(packet);
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
