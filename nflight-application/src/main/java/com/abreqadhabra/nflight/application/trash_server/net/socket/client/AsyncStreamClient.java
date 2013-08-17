package com.abreqadhabra.nflight.application.trash_server.net.socket.client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.abreqadhabra.nflight.application.trash_server.net.socket.MessageDTOImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class AsyncStreamClient extends JFrame {
	private static Class<AsyncStreamClient> THIS_CLAZZ = AsyncStreamClient.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	ClientControllerImpl controller;
	
	public AsyncStreamClient() {
		String plaf = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
		try {
			UIManager.setLookAndFeel(plaf);
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
		}
		this.setVisible(true);
		controller = new ClientControllerImpl();

	}

	public static void main(String[] args) throws Exception {

		InetSocketAddress socketAddress = new InetSocketAddress(InetAddress
				.getLocalHost().getHostAddress(), 9999);

		AsyncStreamClient client = new AsyncStreamClient();
		client.sendTest();
	}

	private  void sendTest() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		

		MessageDTOImpl msg = new MessageDTOImpl();
		msg.setType("AirlineDAO.findAll");
		msg.setName("1");
		msg.setMessage("2");
		msg.setContent(ByteBuffer.wrap(msg.toString().getBytes(
				Charset.forName("UTF-8"))));
		
		controller.send(msg);
		
//		
//	
//		try {
//			AsynchronousSocketChannel asyncSocketChannel = AsynchronousSocketChannel
//					.open();
//			Future<Void> future = asyncSocketChannel.connect(endpoint);
//			future.get();
//
//			MessageDTOImpl msg = new MessageDTOImpl();
//			msg.setType("login");
//			msg.setName("1");
//			msg.setMessage("2");
//			msg.setContent(ByteBuffer.wrap(msg.toString().getBytes(
//					Charset.forName("UTF-8"))));
//
//			if (asyncSocketChannel.isOpen()) {
//				try {
//					LOGGER.logp(
//							Level.FINER,
//							THIS_CLAZZ
//									.getSimpleName(),
//							METHOD_NAME,
//							asyncSocketChannel.getLocalAddress() + "----------> writing Message: " + asyncSocketChannel.getRemoteAddress());
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			
//				asyncSocketChannel.write(msg.getContent()).get();
//			}
//			asyncSocketChannel.close();
//		} catch (IOException | InterruptedException | ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
