package com.abreqadhabra.nflight.service.socket.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.core.server.AbstractServerReceiver;
import com.abreqadhabra.nflight.service.core.server.IService;


public class NFServerSocketImpl extends AbstractServerReceiver {

	private static final Class<NFServerSocketImpl> THIS_CLAZZ = NFServerSocketImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private ServerSocket serverSocket;

	public NFServerSocketImpl(ProfileImpl bootProfile, IService service) throws Exception {
		super(bootProfile, service);
		init();
	}

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startup() throws Exception {
		int port = super.profile.getServicePort();
		serverSocket = new ServerSocket(port);
	//	service = new NFServiceSocketImpl();
		socketListening();

	}

	private void socketListening() {
		while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	//		ServerSocketThread serverThread = new ServerSocketThread(socket, service);
//			serverThread.start();
		}
	}

	@Override
	public void shutdown() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean status() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}




}
