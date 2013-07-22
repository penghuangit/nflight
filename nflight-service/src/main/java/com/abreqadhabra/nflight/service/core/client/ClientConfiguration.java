package com.abreqadhabra.nflight.service.core.client;

import java.awt.Container;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.service.core.Profile;
import com.abreqadhabra.nflight.service.core.ProfileImpl;

public class ClientConfiguration {

	private static final Class<ClientConfiguration> THIS_CLAZZ = ClientConfiguration.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);


	public void ClientConfiguration() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

	}

	public static void main(String[] args) throws Exception {

		

		Properties props = PropertyFile.readPropertyFilePath(THIS_CLAZZ.getName(),
				Profile.FILE_BOOT_PROPERTIES);
		
		ClientConfiguration client = new ClientConfiguration();
		
		client.execute(props);
		
	}

	private void execute(Properties props) throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, props.toString());

		ProfileImpl profile = new ProfileImpl(props);
		
		CreateGUI();
		
		
	}

	private void CreateGUI() {
		JFrame frame = new JFrame("Server Status");
		frame.setSize(400,300);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		Container container = frame.getContentPane();

		
		frame.setVisible(true);
	}
}
