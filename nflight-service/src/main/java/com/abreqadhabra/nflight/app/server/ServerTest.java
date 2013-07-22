package com.abreqadhabra.nflight.app.server;

import java.net.InetAddress;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.app.core.command.Command;
import com.abreqadhabra.nflight.app.core.command.Invoker;
import com.abreqadhabra.nflight.app.core.rmi.RMIServiceHelper;
import com.abreqadhabra.nflight.app.server.command.ShutdownServerCommand;
import com.abreqadhabra.nflight.app.server.command.StartupServerCommand;
import com.abreqadhabra.nflight.app.server.command.StatusServerCommand;
import com.abreqadhabra.nflight.app.server.service.IService;
import com.abreqadhabra.nflight.app.server.service.ServiceDescriptor;
import com.abreqadhabra.nflight.app.server.service.ServiceFactory;
import com.abreqadhabra.nflight.common.exception.WrapperException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.IOStream;

//Strategy Context
public class ServerTest {

	private static final Class<ServerTest> THIS_CLAZZ = ServerTest.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	
	String host = InetAddress.getLocalHost().getHostName();
	String address = InetAddress.getLocalHost().getHostAddress();
	int port = 9999;
	
	public ServerTest() throws Exception {
		doTest();
	}

	public static void main(String[] args) throws Exception {

		ServerTest test = new ServerTest();
		test.doTest();

	}

	private  void doTest() throws Exception {

		String serviceName = "unicast";

		ServiceDescriptor sd = new ServiceDescriptor();
		sd.setServiceName(serviceName);
		sd.setHost(host);
		sd.setAddress(address);
		sd.setPort(port);
		
		testService(sd);
		
		serviceName ="activatable";
		sd.setServiceName(serviceName);
		sd.setCodeBase(IOStream.getCodebase(THIS_CLAZZ.getName()));
		
		testService(sd);
		

		
		
		// server.init();
		// server.startup();
		// server.status();
		// server.shutdown();

	}

	private void testService(ServiceDescriptor sd) throws Exception {
		Command _cmd = null;
		Invoker _invoker = new Invoker();

		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

		
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"service name : " + sd.getServiceName());
		
		IServer _server = getServer(sd);

		try {
			_cmd = new ShutdownServerCommand(_server);
			_invoker.execute(_cmd);
		} catch (Exception e) {
			_cmd = new StartupServerCommand(_server);
			_invoker.execute(_cmd);

			_cmd = new StatusServerCommand(_server);
			_invoker.execute(_cmd);

			_cmd = new ShutdownServerCommand(_server);
//			_invoker.execute(_cmd);

		}
		
		Registry registry = RMIServiceHelper.getRegistry(host, port);

		
		String name1 = "rmi://"+host+":"+port+"/unicast";
		String name2 = "rmi://"+host+":"+port+"/activatable";
		String response=null;
		
		
		try {
			IService stub1 = (IService) registry.lookup(name1);
			// registry.lookup("rmi://192.168.0.100:9999/NFlight/UnicastRemoteObjectNFlightServiceImpl");
			response = stub1.sayHello();
			System.out.println(stub1 + "\t:\t" + response);
		} catch (Exception e) {
			System.out.println(WrapperException.getStackTrace(e));
		}
		try {
		
			IService stub2 = (IService) registry.lookup(name2);

			response = stub2.sayHello();
			System.out.println(stub2 + "\t:\t" + response);

		} catch (Exception e) {
			System.out.println(WrapperException.getStackTrace(e));
		}
		
	}

	public  IServer getServer(ServiceDescriptor sd) throws Exception {

		IServer _server = new ServerImpl(sd.getServiceName());
		IService _service = (IService) ServiceFactory.getService(sd);
		_server.setService(_service);

		return _server;

	}

}
