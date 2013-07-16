package com.abreqadhabra.nflight.service.socket.server.stream;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

class connectionClient3 {
	public static void main(String[] args) {
		try {
			InetAddress serverHost = InetAddress.getByName(args[0]);
			int serverPortNum = Integer.parseInt(args[1]);
			Socket clientSocket = new Socket(serverHost, serverPortNum);
		//	EmployeeData empData = new EmployeeData();
			Scanner input = new Scanner(System.in);
			System.out.print("Enter employee id: ");
			int id = input.nextInt();
			System.out.print("Enter employee name: ");
			String name = input.next();
			System.out.print("Enter employee salary: ");
			double salary = input.nextDouble();
		//	empData.setID(id);
		//	empData.setName(name);
		//	empData.setSalary(salary);
			ObjectOutputStream oos = new ObjectOutputStream(
					clientSocket.getOutputStream());
		//	oos.writeObject(empData);
			oos.flush();
			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}