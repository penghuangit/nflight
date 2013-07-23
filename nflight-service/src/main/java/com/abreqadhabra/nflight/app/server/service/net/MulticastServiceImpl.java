package com.abreqadhabra.nflight.app.server.service.net;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.app.server.service.ServiceDescriptor;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

//Strategy ConcreteStrategy
public class MulticastServiceImpl extends AbstractService {

	private static final Class<MulticastServiceImpl> THIS_CLAZZ = MulticastServiceImpl.class;
	private static Logger LOGGER = LoggingHelper
			.getLogger(MulticastServiceImpl.THIS_CLAZZ);

	protected DatagramChannel datagramChannel;

	public MulticastServiceImpl(final ServiceDescriptor sd) throws Exception {
		super(sd);
	}

	private String getNetworkInterfaceName(final String address)
			throws SocketException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		final Enumeration<NetworkInterface> eni = NetworkInterface
				.getNetworkInterfaces();

		String networkInterfaceName = null;

		while (eni.hasMoreElements()) {
			final NetworkInterface ni = eni.nextElement();
			final Enumeration<InetAddress> inetAddresses = ni
					.getInetAddresses();

			while (inetAddresses.hasMoreElements()) {
				final InetAddress ia = inetAddresses.nextElement();
				if (!ia.isLinkLocalAddress()) {
					LOGGER.logp(
							Level.FINER,
							THIS_CLAZZ.getName(),
							METHOD_NAME,
							"Interface: " + ni.getName() + " IP: "
									+ ia.getHostAddress());
					if (address.equals(ia.getHostAddress())) {
						networkInterfaceName = ni.getName();
						LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(),
								METHOD_NAME, "networkInterfaceName : "
										+ networkInterfaceName);
					}
				}
			}
		}
		return networkInterfaceName;
	}

	@Override
	public void init() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		/* try { */

		// create a new datagram channel
		this.datagramChannel = DatagramChannel
				.open(StandardProtocolFamily.INET);

		this.server = this.datagramChannel;

		this.getNetworkInterfaceName(this.address);

		// get the network interface used for multicast
		final NetworkInterface networkInterface = NetworkInterface
				.getByName("eth5");

		// set some options
		this.datagramChannel.setOption(StandardSocketOptions.IP_MULTICAST_IF,
				networkInterface);
		this.datagramChannel
				.setOption(StandardSocketOptions.SO_REUSEADDR, true);

		// bind the channel to the local address
		this.datagramChannel.bind(new InetSocketAddress(this.port));
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
				"Date-time server is ready ... shortly I'll start sending ...");

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

			ByteBuffer datetime = null;

			// transmitting datagrams
			while (true) {

				// sleep for 10 seconds
				try {
					Thread.sleep(10000);
				} catch (final InterruptedException ex) {
				}
				System.out.println("Sending data ...");

				datetime = ByteBuffer.wrap(new Date().toString().getBytes());
				this.datagramChannel.send(datetime, new InetSocketAddress(
						InetAddress.getByName(this.host), this.port));
				datetime.flip();
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
