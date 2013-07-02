package com.abreqadhabra.nflight.server.ns.rmi.bin;

import java.rmi.RemoteException;

import com.abreqadhabra.nflight.server.ns.rmi.INFlightRMIServer;
import com.abreqadhabra.nflight.server.ns.rmi.ServerController;


public class NFlightRMIServer implements INFlightRMIServer, Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Main to start the <code> ServerApplication </code> and creates the
	 * Instance of <code> ServerController </code>. Create a RMI server with the
	 * specified port(1024-65535).
	 * 
	 * @param args
	 *            The port of naming service like rmi registry.
	 */
	public static void main(String[] args) {
		boolean isValid = false;
		String defaultPort = "44444";
		int port = 0;
		if (args != null && args.length == 1) {
			isValid = ServerController.portValidation(args[0]);
			if (isValid) {
				port = Integer.parseInt(args[0]);
			}
		} else {
			isValid = ServerController.portValidation(defaultPort);
			if (isValid) {
				port = Integer.parseInt(defaultPort);
			}
		}
		if (Boolean.TRUE.equals(Boolean.valueOf(isValid))) {
			new ServerController(port);
		} else {
			System.err.println("Usage: java -jar ServerApplication.jar "
					+ "<port>");
			System.exit(-1);
		}
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void check() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean checkStatus() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

}
