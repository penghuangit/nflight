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

	public static void main(final String[] args) throws Exception {

		final ServerTest test = new ServerTest();
		test.doTest();

	}

	String host = InetAddress.getLocalHost().getHostName();
	String address = InetAddress.getLocalHost().getHostAddress();

	int port = 9999;

	public ServerTest() throws Exception {
		this.doTest();
	}

	private void doTest() throws Exception {

		String serviceName = "unicast";

		final ServiceDescriptor sd = new ServiceDescriptor();
		sd.setServiceName(serviceName);
		sd.setHost(this.host);
		sd.setAddress(this.address);
		sd.setPort(this.port);

		// this.testService(sd);

		serviceName = "activatable";
		sd.setServiceName(serviceName);
		sd.setCodeBase(IOStream.getCodebase(THIS_CLAZZ.getName()));

		// this.testService(sd);

		serviceName = "datagram";
		sd.setServiceName(serviceName);

		// this.testService(sd);

		serviceName = "multicast";
		sd.setServiceName(serviceName);

		this.testService(sd);

		// server.init();
		// server.startup();
		// server.status();
		// server.shutdown();

	}

	public IServer getServer(final ServiceDescriptor sd) throws Exception {

		final IServer _server = new ServerImpl(sd.getServiceName());
		final IService _service = ServiceFactory.getService(sd);
		_server.setService(_service);

		return _server;

	}

	private void testService(final ServiceDescriptor sd) throws Exception {
		Command _cmd = null;
		final Invoker _invoker = new Invoker();

		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"service name : " + sd.getServiceName());

		IServer _server = null;

		_server = this.getServer(sd);

		try {
			_cmd = new StartupServerCommand(_server);
			_invoker.execute(_cmd);
			_cmd = new StatusServerCommand(_server);
			_invoker.execute(_cmd);
		} catch (Exception e) {
			_cmd = new ShutdownServerCommand(_server);
			_invoker.execute(_cmd);
			e.printStackTrace();
		}

		if (sd.getServiceName().equals("unicast")
				| sd.getServiceName().equals("activatable")) {
			final Registry registry = RMIServiceHelper.getRegistry(this.host,
					this.port);

			final String name1 = "rmi://" + this.host + ":" + this.port
					+ "/unicast";
			final String name2 = "rmi://" + this.host + ":" + this.port
					+ "/activatable";
			String response = null;

			try {
				final IService stub1 = (IService) registry.lookup(name1);
				// registry.lookup("rmi://192.168.0.100:9999/NFlight/UnicastRemoteObjectNFlightServiceImpl");
				response = stub1.sayHello();
				System.out.println(stub1 + "\t:\t" + response);
			} catch (final Exception e) {
				System.out.println(WrapperException.getStackTrace(e));
			}
			try {

				final IService stub2 = (IService) registry.lookup(name2);

				response = stub2.sayHello();
				System.out.println(stub2 + "\t:\t" + response);

			} catch (final Exception e) {
				System.out.println(WrapperException.getStackTrace(e));
			}
		}
	}

}
