package com.abreqadhabra.freelec.jbpf.examples.lms.bin.server.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.abreqadhabra.freelec.jbpf.examples.lms.bin.server.db.EmbeddedJavaDB;
import com.abreqadhabra.freelec.jbpf.examples.lms.dao.IUserDAO;
import com.abreqadhabra.freelec.jbpf.examples.lms.dao.UserDAOFactory;

public class TCPServer{

	public IUserDAO dao;
	private ServerSocket serverSocket;

	public TCPServer(){
		this(6666);
	}

	public TCPServer(int port){
		try{
			serverSocket = new ServerSocket(port);
			dao = (UserDAOFactory.instance()).getUserDAO(1);
		//	System.out.println("�л� �� �ý��� ����("+port +")�� ���۵Ǿ���ϴ�.");
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
				ServerThread serverThread = new ServerThread(socket, dao);
				serverThread.start();					
			}catch(Exception e) { 
				e.printStackTrace();		
			} //try
		} //while
	} // socketListening()

	public static void main(String args[]){
		if(args.length<1){
			new EmbeddedJavaDB();
			new TCPServer();			
		}else{
			new TCPServer(Integer.parseInt(args[0]));
		}//if
	}//main


} //class TCPServer
