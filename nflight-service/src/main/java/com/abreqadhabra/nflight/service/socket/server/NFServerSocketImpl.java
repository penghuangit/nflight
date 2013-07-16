package com.abreqadhabra.nflight.service.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.service.core.NFService;
import com.abreqadhabra.nflight.service.core.boot.BootProfile;
import com.abreqadhabra.nflight.service.core.server.NFServer;


public class NFServerSocketImpl implements NFServer {

	private static final Class<NFServerSocketImpl> THIS_CLAZZ = NFServerSocketImpl.class;
	private Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);


	private BootProfile bootPofile;
	private ServerSocket serverSocket;
	private NFService service;

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	public NFServerSocketImpl(BootProfile bootPofile) throws Exception {
		this.setBootPofile(bootPofile);
		init();
	}

	private void init() throws Exception {
		LOGGER.info("init: " + THIS_CLAZZ.getCanonicalName());
	}

	@Override
	public void startup() throws Exception {
		int port = this.bootPofile.getServicePort();
		serverSocket = new ServerSocket(port);
		service = new NFServiceSocketImpl();
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
			ServerSockwtThread serverThread = new ServerSockwtThread(socket, service);
			serverThread.start();
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

	public BootProfile getBootPofile() {
		return this.bootPofile;
	}

	public void setBootPofile(BootProfile bootPofile) {
		this.bootPofile = bootPofile;
	}



}
