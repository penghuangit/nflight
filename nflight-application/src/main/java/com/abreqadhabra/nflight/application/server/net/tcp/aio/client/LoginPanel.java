package com.abreqadhabra.nflight.application.server.net.tcp.aio.client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.abreqadhabra.nflight.application.server.net.tcp.aio.MessageDTOImpl;


public class LoginPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	public ClientFrame client;
	public JButton loginBtn;
	public JLabel    label01;
	public JLabel    label02;
	public JTextField userNo;
	public JPasswordField userPwd;
	private Image backImg;
	public LoginPanel(ClientFrame client){
		this.client=client;
		initComponent();
	}
	private void initComponent() {
		backImg=new ImageIcon("resource/img/login.jpg").getImage();
		label01=new JLabel("계정 :");
		label02=new JLabel("비번 :");
		userNo=new JTextField(16);
		userPwd=new JPasswordField(16);
		loginBtn=new JButton("등록");
		
		SwingUtils.setFont(label01,label02,userNo,loginBtn);
		this.add(label01);
		this.add(userNo);
		this.add(label02);
		this.add(userPwd);
		this.add(loginBtn);
		
		label01.setBounds(200, 100,100,30);
		userNo.setBounds(300, 100,150,30);
		label02.setBounds(200, 150,100,30);
		userPwd.setBounds(300, 150,150,30);
		loginBtn.setBounds(250, 230,80,30);

		this.setLayout(null);
		loginBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				MessageDTOImpl msg = new MessageDTOImpl();
				msg.setType(ChatType.LOGIN);
				msg.setName(userNo.getText());
				msg.setMessage("등록");
				msg.setContent(ByteBuffer.wrap(msg.toString().getBytes(Charset.forName("UTF-8"))));
				client.userNo=msg.getName();
				client.setTitle("사용자:"+msg.getName());
				try {
					client.channel.write(msg.getContent()).get();
				} catch(Exception e1) {
				}
				client.layout.show(client.getContentPane(),"clientPanel");
			}
		});
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backImg,0,0,this.getWidth(),this.getHeight(),this);
	}
}
