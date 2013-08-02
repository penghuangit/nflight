package com.abreqadhabra.nflight.application.server.net.tcp.aio.client;

import java.awt.Component;
import java.awt.Font;


public class SwingUtils {
	public static void setFont(Component ...components){
		Font font =new Font("맑은고딕",Font.PLAIN,13);
		for(Component c:components){
			c.setFont(font);
		}
	}
}
