package com.abreqadhabra.nflight.service.client.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class TestClient {
	private Socket clientSocket = null;
	private DataOutputStream dos = null;
	private DataInputStream dis = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	Thread clientThread;

	public TestClient() throws Exception {
		this("127.0.0.1");
	}

	public TestClient(String url) throws Exception {
		this(url, 9999);
	}

	public TestClient(String url, int port) throws Exception {
		this.connect(url, port);
		this.socketListening();
	}

	public synchronized void connect(String url, int port) {
		if (url == null | port == 0) {
			url = "localhost";
			port = 6666;
		}
		if (clientSocket == null) {
			try {
				clientSocket = new Socket(url, port);
				dos = new DataOutputStream(clientSocket.getOutputStream());
				dis = new DataInputStream(clientSocket.getInputStream());
				oos = new ObjectOutputStream(clientSocket.getOutputStream());
				ois = new ObjectInputStream(clientSocket.getInputStream());
			} catch (IOException e) {
				System.out.println("시스템 (" + url + ":" + port
						+ ") 접속이 실패하였습니다.!!");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void socketListening() {
		clientThread = new Thread() {
			public void run() {
				try {
					while (clientSocket != null) {
						String cmd = (String) ois.readObject();
						Object obj = ois.readObject();
						Object resultObject = createResultObject(cmd, obj);
						System.out.println(cmd + ":" + resultObject);
						viewRecieveResult(cmd, obj);
					}
				} catch (UnknownHostException ukhe) {
					System.out.println("접속하려는 서버 ip를 정확히 입력해주세요");
				} catch (ConnectException ce) {
					System.out.println("서버와의 접속 에러");
				} catch (SocketException sockee) {
					System.out.println("서버와의 접속 종료 상태");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		clientThread.start();
	}

	public synchronized void close() {
		try {
			if (clientSocket != null) {
				if (dos != null) {
					dos.close();
				}
				if (dis != null) {
					dis.close();
				}
				if (oos != null) {
					oos.close();
				}
				if (ois != null) {
					ois.close();
				}
				clientSocket.close();
			}
		} catch (IOException e) {
			System.out.println("모든 스트림을 닫았습니다.");
		}
		clientSocket = null;
	}

	public static void main(String args[]) {
		TestClient tcpClient = null;
		try {
			switch (args.length) {
			case 1:
				tcpClient = new TestClient(args[0]);
				break;
			case 2:
				tcpClient = new TestClient(args[0],
						Integer.parseInt(args[1]));
				break;
			default:
				tcpClient = new TestClient();
				break;
			}

			tcpClient.status("status");

		} catch (UnknownHostException ukhe) {
			System.out.println("접속하려는 서버IP를 정확히 입력하세요.");
		} catch (ConnectException ce) {
			System.out.println("서버와의 접속이 이루어지지 않았습니다.");
		} catch (SocketException se) {
			System.out.println("서버와의 연결이 끊겼습니다!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void viewRecieveResult(String cmd, Object obj) {
		String value = null;
		System.out
				.println("------------------------------------------------------------------------------------\n");
		System.out.println("명령어:" + cmd + "에 대하여 서버에서 받은 결과:\n");
		if (obj instanceof String) {
			value = (String) obj;
			System.out
					.println("------------------------------------------------------------------------------------\n");
			System.out.println("메시지: " + value + "\n");
		} else if (obj instanceof ArrayList) {
			ArrayList valueArrayList = (ArrayList) obj;
			for (int i = 0; i < valueArrayList.size(); i++) {
				if (i == 0) {
					String msg = (String) valueArrayList.get(i);
					System.out
							.println("------------------------------------------------------------------------------------\n");
					System.out.println("메시지:" + msg + "\n");
					System.out
							.println("------------------------------------------------------------------------------------\n");
				} else {
					String[] temp = (String[]) valueArrayList.get(i);
					System.out.println("배열[" + i + "]      ");
					for (int j = 0; j < temp.length; j++) {
						System.out.println(temp[j] + "      ");
					}
					System.out
							.println("\n------------------------------------------------------------------------------------\n");
				}
			}
		}

	}

	public Object createResultObject(String cmd, Object obj) {
		if ("status".equals(cmd)) {
			System.out.println("서버로부터 상태 정보를 전송 받았습니다.\n");

			return obj;
		}
		System.out
				.println("\n------------------------------------------------------------------------------------\n");

		return obj;
	}

	public void sendUserObject(String cmd, String value, Object object) {
		try {
			if (cmd != null) {
				oos.writeObject(cmd);
				oos.writeObject(value);
				dos.flush();
				oos.writeObject(object);
				oos.flush();
			} else {
				System.out.println("전송 실패: 요청 명령어가 없습니다.\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void status(String cmd) {
		sendUserObject(cmd, null, null);
		System.out.println("전체출력요청: 원격서버에 전송되었습니다.\n");
	}

} // class TCPClient