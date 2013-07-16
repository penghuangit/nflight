package com.abreqadhabra.nflight.service.socket.server.stream;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

class connectionServer4 {
	public static void main(String[] args) {
		try {
			int serverListenPortNum = Integer.parseInt(args[0]);
			ServerSocket connectionSocket = new ServerSocket(
					serverListenPortNum);
			Socket dataSocket = connectionSocket.accept();
			ObjectInputStream ois = new ObjectInputStream(
					dataSocket.getInputStream());
	//		EmployeeData eData = (EmployeeData) ois.readObject();
//			System.out.println("Employee id : " + eData.getID());
//			System.out.println("Employee name : " + eData.getName());
//			System.out.println("Employee salary : " + eData.getSalary());
			dataSocket.close();
			connectionSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}