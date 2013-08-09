package com.abreqadhabra.nflight.app.server.service.net;

import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.ServerSocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.app.server.service.IService;
import com.abreqadhabra.nflight.app.server.service.ServiceDescriptor;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

//Strategy ConcreteStrategy  / Flyweight ConcreteFlyweight
public abstract class AbstractService implements IService {

	private static final Class<AbstractService> THIS_CLAZZ = AbstractService.class;
	private static Logger LOGGER = LoggingHelper
			.getLogger(AbstractService.THIS_CLAZZ);

	protected ServiceDescriptor desc;
	protected String host;
	protected String address;
	protected int port;

	SelectableChannel server;

	public AbstractService() {
	}

	public AbstractService(final ServiceDescriptor _desc) throws Exception {
		this();
		this.desc = _desc;
		this.host = this.desc.getHost(); // InetAddress.getLocalHost().getHostAddress();
		this.address = this.desc.getAddress();
		this.port = this.desc.getPort(); // ; super.profile.getServicePort();
		this.init();
	}

	@Override
	public void shutdown() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {

			if (this.server instanceof DatagramChannel) {
				((DatagramChannel) this.server).disconnect();
			} else if (this.server instanceof ServerSocketChannel) {
				((ServerSocketChannel) this.server).close();
			}

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"UDP monitoring acceptor has been stopped.");

		} catch (final IOException e) {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Error shutting down the UDP monitor acceptor");
		}
	}
}
