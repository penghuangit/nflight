package com.abreqadhabra.nflight.application.server.net.socket.logic;

import java.nio.ByteBuffer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.net.socket.AsyncSocketServerAcceptor;
import com.abreqadhabra.nflight.application.server.net.socket.ISocketAcceptor;
import com.abreqadhabra.nflight.application.server.net.socket.ISocketServer;
import com.abreqadhabra.nflight.application.server.net.socket.MessageDTO;
import com.abreqadhabra.nflight.application.server.net.socket.MessageDTOImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.dao.AirlineDAO;
import com.abreqadhabra.nflight.dao.DAOFactory;
import com.abreqadhabra.nflight.dao.dto.Airline;

public class BusinessLogicHandlerImpl implements IBusinessLogicHandler {
	private static final Class<BusinessLogicHandlerImpl> THIS_CLAZZ = BusinessLogicHandlerImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private ISocketServer server;
	private Vector<String> names = new Vector<String>();

	DAOFactory daoFactory;

	public BusinessLogicHandlerImpl() {
		daoFactory = DAOFactory.getDAOFactory(DAOFactory.DATABASE_TYPE_DERBY);
	}

	@Override
	public void doLogic(AsyncSocketServerAcceptor acceptor,
			LinkedBlockingQueue<MessageDTO> input,
			LinkedBlockingQueue<MessageDTO> output) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		ConcurrentHashMap<Long, ISocketAcceptor> sessionMap = this.server
				.getSessionMap();

		MessageDTOImpl msg;
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				sessionMap + ":" + Integer.toString(sessionMap.size()));

		// while ((msg = (MessageDTOImpl) input.take()) != null) {
		while (input.peek() != null) {

			msg = (MessageDTOImpl) input.poll();

			if (msg.getType().equals("AirlineDAO.findAll")) {
				
				AirlineDAO airlineDAO = null;
				Airline[] airlines = null;
				try {
					airlineDAO = daoFactory.getAirlineDAO();
					airlines = airlineDAO.findAll();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				MessageDTOImpl messageDTO = new MessageDTOImpl();
				
				messageDTO.setType("fresh");
				
				ByteBuffer content = messageDTO.serializeObject(airlines);
				
				messageDTO.setContent(content);
				
//			/	ByteBuffer outputByteBuffer =  messageDTO.serializeObject(messageDTO);
				
//				acceptor.setAttribute("userName", msg.getName());
//				this.names.add(msg.getName());
				for (ISocketAcceptor socketAcceptor : sessionMap.values()) {
//					MessageDTOImpl freshName = new MessageDTOImpl();
//					freshName.setType("fresh");
//					freshName.setName(msg.getName());
//					String strs = "";
//					for (int i = 0; i < this.names.size(); i++) {
//						strs += this.names.get(i) + "@";
//					}
//					freshName.setMessage(strs);
//					freshName.setContent(ByteBuffer.wrap(freshName.toString()
//							.getBytes(Charset.forName("UTF-8"))));
					
					
					socketAcceptor.addOutputQueue(messageDTO);

				}
			}

			// else if (msg.getType().equals(ChatType.CHAT)) {
			// for (ISocketAcceptor s : sessionMap.values()) {
			// MessageDTOImpl freshName = new MessageDTOImpl();
			// byte[] bs = new byte[msg.getContent().limit()];
			// byte[] bs2 = msg.getContent().array();
			// System.arraycopy(bs2, 0, bs, 0, bs.length);
			// freshName.setType(ChatType.CHAT);
			// freshName.setName(msg.getName());
			// freshName.setMessage(msg.getMessage());
			// freshName.setContent(ByteBuffer.wrap(bs));
			// s.add(freshName);
			// }
			// }
		}

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"sessions: " + Integer.toString(sessionMap.size()) + " "
						+ sessionMap + "/" + Integer.toString(input.size())
						+ ":" + Integer.toString(output.size()));

		for (ISocketAcceptor socketAcceptor : sessionMap.values()) {
			socketAcceptor.send();
		}
	}

	@Override
	public void setServer(ISocketServer socketServer) {
		this.server = socketServer;
	}

	@Override
	public void endCallBack() {
		// TODO Auto-generated method stub

	}

}
