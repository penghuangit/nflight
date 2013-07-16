package com.abreqadhabra.nflight.service.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.abreqadhabra.nflight.service.core.NFlightService;

public class TCPServer{

	public NFlightService service;
	private ServerSocket serverSocket;

	public TCPServer(){
		this(6666);
	}

	public TCPServer(int port){
		try{
			serverSocket = new ServerSocket(port);
			service = new NFlightServiceImpl();
			socketListening();
		} catch (IOException e) { 
				e.printStackTrace();		
		}catch(Exception e){
			e.printStackTrace();		
		}//try
	}//TCPServer(int port)

	public void socketListening(){
		while (true) {
			try {
			//	System.out.println("Ŭ���̾�Ʈ�� ������ ��ٸ��ϴ�.");
				Socket socket = serverSocket.accept();		
				ServerThread serverThread = new ServerThread(socket, service);
				serverThread.start();					
			}catch(Exception e) { 
				e.printStackTrace();		
			} //try
		} //while
	} // socketListening()

	public static void main(String args[]){
		if(args.length<1){
			new TCPServer();			
		}else{
			new TCPServer(Integer.parseInt(args[0]));
		}//if
	}//main


} //class TCPServer
