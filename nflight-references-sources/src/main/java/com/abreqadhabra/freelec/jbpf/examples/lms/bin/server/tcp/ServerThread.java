package com.abreqadhabra.freelec.jbpf.examples.lms.bin.server.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import com.abreqadhabra.freelec.jbpf.examples.lms.dao.IUserDAO;

class ServerThread extends Thread {

	protected Socket socket;
	protected IUserDAO dao;
	protected DataInputStream dis;
	protected DataOutputStream dos;
	protected ObjectInputStream ois;
	protected ObjectOutputStream oos;
	public String client;
	public int port;
	public String hostName;

	public ServerThread(Socket soc, IUserDAO dao) {

		this.socket = soc;
		this.dao = dao;
		try {
			client = (socket.getInetAddress()).getHostAddress();// ������Ŭ���̾�Ʈ�� IP��
			port = socket.getPort();
			hostName = socket.getInetAddress().getHostName();
			//System.out.println("Ŭ���̾�Ʈ ��������: " + hostName + "/" + client + ":"					+ port + "\n");
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			sendMessage("list", dao.listUser());
		} catch (IOException e) {
		}
	}

	// Ŭ���̾�Ʈ�� ������ ������ ��������� ����ؼ�Ŭ���̾�Ʈ�� �����͸� ���
	public void run() {
		Object obj = null;
		ArrayList daoResult = null;
		String cmd = null;
		String value = null;
		String msg = null;
	//	System.out.println("Ŭ���̾�Ʈ: " + client + "�� ������ �Ϸ��߽��ϴ�.\n");
		try {
			while (true) {
				cmd = (String) ois.readObject();
				value = (String) ois.readObject();
				obj = ois.readObject();
		//		System.out.println("Ŭ���̾�Ʈ " + client + "�κ��� \"��ɾ�: " + cmd 						+ ",  ��ü����: " + obj + "\"  �� ��۹޾ҽ��ϴ�\n");
				if ("create".equals(cmd)) {
					String[] temp = (String[]) obj;
					String[] defaultInfo = new String[4];
					String[] addInfo = new String[3];
					defaultInfo[0] = temp[1];
					defaultInfo[1] = temp[2];
					defaultInfo[2] = temp[3];
					defaultInfo[3] = temp[4];
					addInfo[0] = temp[0];
					addInfo[1] = temp[3];
					addInfo[2] = temp[5];
					String type = temp[6];
					msg = dao.createUser(type, defaultInfo, addInfo);
					System.out.println(msg);
					sendMessage(cmd, msg);
				} else if ("update".equals(cmd)) {
					String[] temp = (String[]) obj;
					String[] defaultInfo = new String[4];
					String[] addInfo = new String[2];
					defaultInfo[0] = temp[1];
					defaultInfo[1] = temp[2];
					defaultInfo[2] = temp[4];
					defaultInfo[3] = temp[3];
					addInfo[0] = temp[5];
					addInfo[1] = temp[3];
					String sequenceCategory = temp[7];
					String category = temp[6];
					String userNo = temp[8];
					msg = dao.modifyUser(category, sequenceCategory,
							defaultInfo, addInfo, userNo);
					System.out.println(msg);
					sendMessage(cmd, msg);
				} else if ("delete".equals(cmd)) {
					msg = dao.dropUser(value);
					System.out.println(msg);
					sendMessage(cmd, msg);
				} else if ("ssn".equals(cmd)) {
					daoResult = dao.findUser(cmd, value);
					printResult(daoResult);
					sendMessage(cmd, daoResult);
				} else if ("name".equals(cmd)) {
					daoResult = dao.findUser(cmd, value);
					printResult(daoResult);
					sendMessage(cmd, daoResult);
				} else if ("list".equals(cmd)) {
					daoResult = dao.listUser();
					printResult(daoResult);
					sendMessage(cmd, daoResult);
				} else if ("seq".equals(cmd)) {
					String seq = dao.getSequence(value);
					System.out.println("���ο� �������� " + msg);
					sendMessage(cmd, seq);
				}
			//	System.out.println("Ŭ���̾�Ʈ " + client + "���� " + cmd 						+ "�� ���� ��� ����Ͽ����ϴ�.\n");
			}// while

		} catch (EOFException eofe) {
			close();
		} catch (SocketException se) {
			close();
			// System.out.println(client + "���� ������ ������ϴ�. ������ �����ϴ�.\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}// try
		}// try
	} // run()

	public void sendMessage(String cmd, Object value) {
		try {
			oos.writeObject(cmd);
			oos.writeObject(value);
			dos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}// try
	}

	public void printResult(ArrayList valueArrayList) {
		for (int i = 0; i < valueArrayList.size(); i++) {
			if (i == 0) {
				String msg = (String) valueArrayList.get(i);
				System.out
						.println("------------------------------------------------------------------------------------");
				System.out.println("�޽���[" + i + "]\t" + msg);
			} else {
				System.out
						.println("------------------------------------------------------------------------------------");
				String[] temp = (String[]) valueArrayList.get(i);
				System.out.print("�迭[" + i + "]\t");
				for (int j = 0; j < temp.length; j++) {
					System.out.print("\t" + temp[j]);
				}
				System.out
						.println("\n------------------------------------------------------------------------------------");
			}
		}
	}

	public void close() {
		try {
			if (dis != null)
				dis.close();
			if (dos != null)
				dos.close();
			if (ois != null)
				ois.close();
			if (oos != null)
				oos.close();
			//System.out.println("Ŭ���̾�Ʈ: " + client + "���� ���������߽��ϴ�.\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

}// class ServerThread