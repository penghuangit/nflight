package com.abreqadhabra.nflight.application.server.net.tcp.aio.logic;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.abreqadhabra.nflight.application.server.net.tcp.Session;
import com.abreqadhabra.nflight.application.server.net.tcp.SocketServer;
import com.abreqadhabra.nflight.application.server.net.tcp.aio.MessageDTO;
import com.abreqadhabra.nflight.application.server.net.tcp.aio.MessageDTOImpl;
import com.abreqadhabra.nflight.application.server.net.tcp.aio.SocketServerSession;
import com.abreqadhabra.nflight.application.server.net.tcp.aio.client.ChatType;

public class BusinessLogicHandlerImpl implements BusinessLogicHandler {

	private SocketServer server;
	private Vector<String> names = new Vector<String>();

	@Override
	public void doLogic(SocketServerSession session,
			LinkedBlockingQueue<MessageDTO> input,
			LinkedBlockingQueue<MessageDTO> output) {

		ConcurrentHashMap<Long, Session> sessionMap = this.server
				.getSessionMap();

		MessageDTOImpl msg;
		// log.info("会话池size ：" + sessionMap.size());
		try {
			while ((msg = (MessageDTOImpl) input.take()) != null) {
				if (msg.getType().equals(ChatType.LOGIN)) {
					session.setAttribute("userName", msg.getName());
					this.names.add(msg.getName());
					for (Session s : sessionMap.values()) {
						MessageDTOImpl freshName = new MessageDTOImpl();
						freshName.setType(ChatType.FRESH);
						freshName.setName(msg.getName());
						String strs = "";
						for (int i = 0; i < this.names.size(); i++) {
							strs += this.names.get(i) + "@";
						}
						freshName.setMessage(strs);
						freshName
								.setContent(ByteBuffer.wrap(freshName
										.toString().getBytes(
												Charset.forName("UTF-8"))));
						s.add(freshName);
					}
				} else if (msg.getType().equals(ChatType.CHAT)) {
					for (Session s : sessionMap.values()) {
						MessageDTOImpl freshName = new MessageDTOImpl();
						byte[] bs = new byte[msg.getContent().limit()];
						byte[] bs2 = msg.getContent().array();
						System.arraycopy(bs2, 0, bs, 0, bs.length);
						freshName.setType(ChatType.CHAT);
						freshName.setName(msg.getName());
						freshName.setMessage(msg.getMessage());
						freshName.setContent(ByteBuffer.wrap(bs));
						s.add(freshName);
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (Session s : sessionMap.values()) {
			s.write();
		}
	}

	@Override
	public void setServer(SocketServer socketServer) {
		this.server = socketServer;
	}

	@Override
	public void endCallBack() {
		// TODO Auto-generated method stub

	}

}
