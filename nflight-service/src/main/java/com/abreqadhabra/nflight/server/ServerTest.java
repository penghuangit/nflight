package com.abreqadhabra.nflight.server;

import java.net.InetAddress;

import com.abreqadhabra.nflight.server.command.ShutdownServerCommand;
import com.abreqadhabra.nflight.server.command.StartupServerCommand;
import com.abreqadhabra.nflight.server.command.StatusServerCommand;
import com.abreqadhabra.nflight.server.service.IService;
import com.abreqadhabra.nflight.server.service.ServiceDescriptor;
import com.abreqadhabra.nflight.server.service.ServiceFactory;
import com.abreqadhabra.nflight.service.core.command.Command;
import com.abreqadhabra.nflight.service.core.command.Invoker;

//Strategy Context
public class ServerTest {

	public static void main(String[] args) throws Exception {
		
		Command _cmd = null;
		Invoker _invoker = new Invoker();
		
		
		String serviceName = "unicast";
		String host = InetAddress.getLocalHost().getHostName();
		String address = InetAddress.getLocalHost().getHostAddress();
		int port = 9999;
		
		ServiceDescriptor sd = new ServiceDescriptor();
		sd.setServiceName(serviceName);
		sd.setHost(host);
		sd.setAddress(address);
		sd.setPort(port);
		
		IServer _server = getServer(sd);
		
		_cmd = new StartupServerCommand(_server);
		_invoker.execute(_cmd);
		
		_cmd = new StatusServerCommand(_server);
		_invoker.execute(_cmd);
		
		_cmd = new ShutdownServerCommand(_server);
		_invoker.execute(_cmd);
		
//		server.init();
//		server.startup();
//		server.status();
//		server.shutdown();
		
		

	}
	
	public static IServer getServer(ServiceDescriptor sd) throws Exception {

		IServer _server = new ServerImpl(sd.getServiceName());
		IService _service = (IService) ServiceFactory
				.getOperation(sd);
		_server.setService(_service);

		return _server;

	}

}
