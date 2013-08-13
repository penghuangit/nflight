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

	private static Class<MulticastServiceImpl> THIS_CLAZZ = MulticastServiceImpl.class;
	private static Logger LOGGER = LoggingHelper
			.getLogger(MulticastServiceImpl.THIS_CLAZZ);

	protected DatagramChannel datagramChannel;
	protected boolean isOpen;

	public MulticastServiceImpl(ServiceDescriptor sd) throws Exception {
		super(sd);
	}

	private String getNetworkInterfaceName(String address)
			throws SocketException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Enumeration<NetworkInterface> eni = NetworkInterface
				.getNetworkInterfaces();

		String networkInterfaceName = null;

		while (eni.hasMoreElements()) {
			NetworkInterface ni = eni.nextElement();
			Enumeration<InetAddress> inetAddresses = ni
					.getInetAddresses();

			while (inetAddresses.hasMoreElements()) {
				InetAddress ia = inetAddresses.nextElement();
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
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		/* try { */

		// create a new datagram channel
		datagramChannel = DatagramChannel.open(StandardProtocolFamily.INET);

		// check if it the channel was successfully opened
		isOpen = datagramChannel.isOpen();

		if (isOpen) {

			server = datagramChannel;

			String networkInterfaceName = this
					.getNetworkInterfaceName(address);

			// get the network interface used for multicast
			NetworkInterface networkInterface = NetworkInterface
					.getByName(networkInterfaceName);

			// set some options
			datagramChannel.setOption(StandardSocketOptions.IP_MULTICAST_IF,
					networkInterface);
			datagramChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

			// bind the channel to the local socketAddress
			datagramChannel.bind(new InetSocketAddress(port));
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Date-time acceptor is ready ... shortly I'll start sending ...");
		} else {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"The channel cannot be opened!");
		}
		/*
		 * } catch (Exception ex) { if (ex instanceof
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
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// check if it the channel was successfully opened
		if (isOpen) {

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Echo acceptor was successfully opened!");

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
					"Echo acceptor was binded on: "
							+ datagramChannel.getLocalAddress());
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Echo acceptor is ready to echo ...");

			ByteBuffer datetime = null;

			// transmitting datagrams
			while (true) {

				// sleep for 10 seconds
				try {
					Thread.sleep(10000);
				} catch (InterruptedException ex) {
				}
				System.out.println("Sending data ...");

				datetime = ByteBuffer.wrap(new Date().toString().getBytes());
				datagramChannel.send(datetime, new InetSocketAddress(
						InetAddress.getByName(host), port));
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
