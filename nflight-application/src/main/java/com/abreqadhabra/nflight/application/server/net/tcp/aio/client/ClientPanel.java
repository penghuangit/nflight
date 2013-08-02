package com.abreqadhabra.nflight.application.server.net.tcp.aio.client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.abreqadhabra.nflight.application.server.net.tcp.aio.MessageDTOImpl;
import com.abreqadhabra.nflight.application.server.net.tcp.aio.SocketServerSession;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

@SuppressWarnings("unchecked")
public class ClientPanel extends JPanel {

	private static final Class<SocketServerSession> THIS_CLAZZ = SocketServerSession.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private static final long serialVersionUID = 1L;

	public ClientFrame client;
	public JScrollPane leftPanel;
	public JPanel topPanel;
	public JPanel centerPanel;
	public JTextArea jta01;
	public JTextField jtf01;
	public JButton btn01;
	public JList list;
	private Image backImg;
	public DefaultListModel model;
	public ClientPanel(ClientFrame client) {
		this.client = client;
		initComponent();
	}

	private void initComponent() {
		backImg = new ImageIcon("resource/img/client.jpg").getImage();
		this.setLayout(null);
		model = new DefaultListModel();
		jta01 = new JTextArea(25, 25);
		jtf01 = new JTextField(16);
		btn01 = new JButton("发送");
		list = new JList(model);
		leftPanel = new JScrollPane();
		topPanel = new JPanel();
		centerPanel = new JPanel();
		leftPanel.getViewport().setView(list);
		topPanel.add(jta01);
		centerPanel.add(jtf01);
		centerPanel.add(btn01);
		leftPanel.setBounds(50, 20, 50, 300);
		topPanel.setBounds(100, 20, 400, 260);
		centerPanel.setBounds(80, 280, 400, 40);
		SwingUtils.setFont(list, jta01, btn01, jtf01);
		this.add(topPanel);
		this.add(leftPanel);
		this.add(centerPanel);
		topPanel.setOpaque(false);
		centerPanel.setOpaque(false);
		this.setOpaque(false);
		btn01.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jtf01.getText().trim().equals("")) {
					return;
				}
				MessageDTOImpl msg = new MessageDTOImpl();
				msg.setType(ChatType.CHAT);
				msg.setName(client.userNo);
				msg.setMessage(jtf01.getText());
				msg.setContent(ByteBuffer.wrap(msg.toString().getBytes(
						Charset.forName("UTF-8"))));
				try {
					if (client.channel.isOpen()) {
						LOGGER.info(msg.toString());
						client.channel.write(msg.getContent()).get();
					}
				} catch (Exception e1) {
				}
				jtf01.setText("");
			}
		});
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backImg, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
