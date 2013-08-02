package com.abreqadhabra.nflight.application.server.net.tcp.aio.client;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.abreqadhabra.nflight.application.server.net.tcp.aio.MessageDTOImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

@SuppressWarnings("unchecked")
public class ClientFrame extends JFrame {
	private static final Class<ClientFrame> THIS_CLAZZ = ClientFrame.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private static final long serialVersionUID = 1L;
	public LoginPanel loginPanel;
	public ClientPanel clientPanel;
	public CardLayout layout;
	public int width = 600;
	public int height = 400;
	public boolean running = true;
	public String userNo;
	public AsynchronousSocketChannel channel;
	public ByteBuffer buffer;
	public int count;
	public Future future;
	public ClientFrame() {
		initComponent();
	}
	private void initComponent() {
		buffer = ByteBuffer.allocate(1024);
		loginPanel = new LoginPanel(this);
		clientPanel = new ClientPanel(this);
		layout = new CardLayout();
		layout.addLayoutComponent(loginPanel, "loginPanel");
		layout.addLayoutComponent(clientPanel, "clientPanel");
		this.add(loginPanel);
		this.add(clientPanel);
		this.setLayout(layout);
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension ds = tool.getScreenSize();
		int screenHeight = (int) ds.getHeight();
		int screenWidth = (int) ds.getWidth();
		this.setTitle("데모 클라이언트");
		this.setSize(width, height);
		this.setLocation((screenWidth - width) / 2, (screenHeight - height) / 2);
		this.setResizable(false);
		String plaf = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
		try {
			UIManager.setLookAndFeel(plaf);
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
		}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		new Thread(new ClientRobot(this)).start();
	}

	private class ClientRobot implements Runnable {
		public ClientFrame client;
		public boolean running = true;
		public ClientRobot(ClientFrame client) {
			super();
			this.client = client;
		}
		public void run() {
			try {
				channel = AsynchronousSocketChannel.open();
				future = channel.connect(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), 9999));
				future.get();
				read();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		private void read() {
			buffer.clear();
			channel.read(buffer, client,
					new CompletionHandler<Integer, ClientFrame>() {
						public void completed(Integer result,
								ClientFrame attachment) {
							if (result > 1) {

								LOGGER.info(channel.toString());
								MessageDTOImpl msg = new MessageDTOImpl();
								buffer.flip();
								byte[] bs = new byte[buffer.limit()];
								buffer.get(bs);
								String content = new String(bs, Charset
										.forName("UTF-8"));
								LOGGER.info(content);
								String[] strs = content.split(":");
								msg.setType(strs[0]);
								msg.setName(strs[1]);
								msg.setMessage(strs[2]);
								msg.setContent(ByteBuffer.wrap(bs));
								if (msg.getType().equals(ChatType.FRESH)) {
									String name = msg.getMessage();
									String[] names = name.split("@");
									LOGGER.info("name size  --->"
											+ names.length);
									DefaultListModel model = clientPanel.model;
									model.clear();
									for (String s : names) {
										model.addElement(s);
									}
								} else if (msg.getType().equals(ChatType.CHAT)) {
									count++;
									if (count % 10 == 0) {
										clientPanel.jta01.setText("");
									}
									String oldMsg = clientPanel.jta01.getText();
									clientPanel.jta01.setText(oldMsg
											+ msg.getName() + " :"
											+ msg.getMessage() + "\n");
								}
								read();
							} else if (result < 1) {
								return;
							}
						}
						public void failed(Throwable exc, ClientFrame attachment) {

						}
					});
		}
	}
}
