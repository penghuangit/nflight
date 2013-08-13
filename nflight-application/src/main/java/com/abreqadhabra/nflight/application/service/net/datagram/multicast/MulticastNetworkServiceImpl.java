package com.abreqadhabra.nflight.application.service.net.datagram.multicast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.AbstractNetworkServiceImpl;
import com.abreqadhabra.nflight.application.service.net.NetworkServiceHelper;
import com.abreqadhabra.nflight.application.service.net.datagram.multicast.tranport.MulticastAcceptor;
import com.abreqadhabra.nflight.application.service.net.stream.asynchronous.transport.AsyncAcceptor;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class MulticastNetworkServiceImpl extends AbstractNetworkServiceImpl {
	private static Class<MulticastNetworkServiceImpl> THIS_CLAZZ = MulticastNetworkServiceImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public MulticastNetworkServiceImpl(Configure configure,
			ThreadPoolExecutor threadPool, InetSocketAddress endpoint) {
		super(configure, threadPool, endpoint);
	}

	@Override
	public void startup() {
		try {
			this.isRunning = true;
			// wait for incoming connections
			MulticastAcceptor acceptor = new MulticastAcceptor(isRunning, this.endpoint,
					/*this.threadPool,*/ this.configure);
			new Thread(acceptor).start();
		} catch (IOException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}


}
