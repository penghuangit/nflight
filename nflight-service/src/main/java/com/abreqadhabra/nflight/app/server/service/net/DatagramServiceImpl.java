package com.abreqadhabra.nflight.app.server.service.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.app.server.service.ServiceDescriptor;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

//Strategy ConcreteStrategy
public class DatagramServiceImpl extends AbstractService {

	private static final Class<DatagramServiceImpl> THIS_CLAZZ = DatagramServiceImpl.class;
	private static Logger LOGGER = LoggingHelper
			.getLogger(DatagramServiceImpl.THIS_CLAZZ);

	protected DatagramChannel datagramChannel;

	final int MAX_PACKET_SIZE = 65507;

	public DatagramServiceImpl(final ServiceDescriptor sd) throws Exception {
		super(sd);
	}

	@Override
	public void init() throws Exception {
		Thread.currentThread().getStackTrace()[1].getMethodName();

		/* try { */

		// create a new datagram channel
		this.datagramChannel = DatagramChannel
				.open(StandardProtocolFamily.INET);

		this.server = this.datagramChannel;

		// set some options
		this.datagramChannel.setOption(StandardSocketOptions.SO_RCVBUF,
				4 * 1024).setOption(StandardSocketOptions.SO_SNDBUF, 4 * 1024);
		// bind the channel to local address
		this.datagramChannel.bind(new InetSocketAddress(this.desc.getAddress(),
				this.desc.getPort()));

		/*
		 * } catch (final Exception ex) { if (ex instanceof
		 * ClosedChannelException) { LOGGER.logp(Level.FINER,
		 * THIS_CLAZZ.getName(), METHOD_NAME,
		 * "The channel was unexpected closed ..."); } if (ex instanceof
		 * SecurityException) { LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(),
		 * METHOD_NAME, "A security exception occured ..."); } if (ex instanceof
		 * IOException) { LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(),
		 * METHOD_NAME, "An I/O error occured ..."); }
		 * 
		 * LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME, "\n" +
		 * ex); }
		 */

	}

	@Override
	public boolean isRunning() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String sayHello() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startup() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// check if it the channel was successfully opened
		if (this.datagramChannel.isOpen()) {

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Echo server was successfully opened!");

			// NetworkInterface networkInterface =
			// NetworkInterface.getByName("hme0");
			//
			// DatagramChannel dc =
			// DatagramChannel.open(StandardProtocolFamily.INET)
			// .setOption(StandardSocketOption.SO_REUSEADDR, true)
			// .bind(new InetSocketAddress(5000))
			// .setOption(StandardSocketOption.IP_MULTICAST_IF,
			// networkInterface);
			//
			// InetAddress group = InetAddress.getByName("225.4.5.6");
			//
			// MembershipKey key = dc.join(group, networkInterface);

			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getName(),
					METHOD_NAME,
					"Echo server was binded on: "
							+ this.datagramChannel.getLocalAddress());
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Echo server is ready to echo ...");

			final ByteBuffer echoText = ByteBuffer
					.allocateDirect(this.MAX_PACKET_SIZE);

			// transmitting data packets
			while (true) {

				SocketAddress clientAddress;
				try {
					clientAddress = this.datagramChannel.receive(echoText);
					echoText.flip();
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
							"I have received " + echoText.limit()
									+ " bytes from " + clientAddress.toString()
									+ "! Sending them back ...");
					this.datagramChannel.send(echoText, clientAddress);
					echoText.clear();
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} else {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"The channel cannot be opened!");
		}
	}

	@Override
	public boolean status() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
