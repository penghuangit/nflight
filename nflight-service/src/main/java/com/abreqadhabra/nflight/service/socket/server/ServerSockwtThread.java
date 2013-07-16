package com.abreqadhabra.nflight.service.socket.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.NFService;

public class ServerSockwtThread extends Thread {
	private static final Class<ServerSockwtThread> THIS_CLAZZ = ServerSockwtThread.class;
	private Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	protected Socket socket;
	protected NFService service;
	protected DataInputStream dis;
	protected DataOutputStream dos;
	protected ObjectInputStream ois;
	protected ObjectOutputStream oos;
	public String client;
	public int port;
	public String hostName;

	public ServerSockwtThread(Socket soc, NFService service) {

		this.socket = soc;
		this.service = service;
		try {
			client = (socket.getInetAddress()).getHostAddress();
			port = socket.getPort();
			hostName = socket.getInetAddress().getHostName();
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			sendMessage("status", service.isRunning());
		} catch (IOException e) {
		}
	}

	@Override
	public void run() {

		try {
			// Object obj = null;
			String cmd = null;
			// String value = null;
			// String msg = null;
			while (true) {
				cmd = (String) ois.readObject();
				// value = (String) ois.readObject();
				// obj = ois.readObject();
				if ("status".equals(cmd)) {
					sendMessage(cmd, service.isRunning());
				}
			}

		} catch (EOFException eofe) {
			close();
		} catch (SocketException se) {
			close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void sendMessage(String cmd, Object value) {
		try {
			oos.writeObject(cmd);
			oos.writeObject(value);
			dos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printResult(ArrayList<?> valueArrayList) {
		for (int i = 0; i < valueArrayList.size(); i++) {
			if (i == 0) {
				String msg = (String) valueArrayList.get(i);
				System.out.println("[" + i + "]\t" + msg);
			} else {
				String[] temp = (String[]) valueArrayList.get(i);
				System.out.print("[" + i + "]\t");
				for (int j = 0; j < temp.length; j++) {
					System.out.print("\t" + temp[j]);
				}
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}