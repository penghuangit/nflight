package com.abreqadhabra.nflight.application.service.network.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.StandardProtocolFamily;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.NetworkChannel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

import com.abreqadhabra.nflight.application.common.launcher.Config;
import com.abreqadhabra.nflight.application.common.launcher.concurrent.executor.ThreadPoolImpl;
import com.abreqadhabra.nflight.application.service.conf.ServiceConfig.ENUM_SERVICE_TYPE;
import com.abreqadhabra.nflight.application.service.network.socket.conf.SocketServiceConfig;
import com.abreqadhabra.nflight.application.service.network.socket.exception.SocketServiceException;
import com.abreqadhabra.nflight.application.service.network.socket.helper.SocketServiceHelper;
import com.abreqadhabra.nflight.common.exception.NFlightException;

public class NetworkChannelFactory {

	public static NetworkChannel getNetworkChannelFactory(
			ENUM_SERVICE_TYPE serviceType) throws IOException, NFlightException {
		switch (serviceType) {
			case network_blocking :
				return getServerSocketChannel(serviceType, true);
			case network_nonblocking :
				return getServerSocketChannel(serviceType, false);
			case network_async :
				return getAsynchronousServerSocketChannel(serviceType);
			case network_unicast :
				return getDatagramChannel(serviceType);
			case network_multicast :
				return getMulticastDatagramChannel(serviceType);
			default :
				break;
		}
		return null;

	}

	private static ServerSocketChannel getServerSocketChannel(
			ENUM_SERVICE_TYPE serviceType, boolean blocking)
			throws IOException, SocketServiceException {
		// 새로운 채널을 생성
		ServerSocketChannel channel = ServerSocketChannel.open();
		// 생성된 채널이 성공적으로 오픈하였는지를 확인
		if (channel.isOpen()) {
			// 생성된 채널을 블로킹 모드로 설정
			channel.configureBlocking(blocking);
			// ServerSocketChannel에 설정 가능한 옵션들을 자동 설정
			SocketServiceHelper.setChannelOption(channel, serviceType);
		}
		return channel;
	}

	private static AsynchronousServerSocketChannel getAsynchronousServerSocketChannel(
			ENUM_SERVICE_TYPE serviceType) throws NFlightException, IOException {
		ThreadPoolExecutor executor = new ThreadPoolImpl()
				.getThreadPoolExecutor(
						Config.get(SocketServiceConfig.KEY_STR_SOCKET_ASYNC_SERVICE_THREAD_POOL_NAME),
						Config.getBoolean(SocketServiceConfig.KEY_BOO_SOCKET_ASYNC_SERVICE_THREAD_POOL_MONITORING),
						Config.getInt(SocketServiceConfig.KEY_INT_SOCKET_ASYNC_SERVICE_THREAD_POOL_MONITORING_DELAY_SECONDS));
		int initialSize = Config
				.getInt(SocketServiceConfig.KEY_INT_SOCKET_ASYNC_THREADPOOL_INITIALSIZE);
		// create asynchronous server-socket channel bound to the thread
		// Group
		AsynchronousChannelGroup threadGroup = AsynchronousChannelGroup
				.withCachedThreadPool(executor, initialSize);
		AsynchronousServerSocketChannel channel = AsynchronousServerSocketChannel
				.open(threadGroup);
		if (channel.isOpen()) {
			// set some options
			SocketServiceHelper.setChannelOption(channel, serviceType);

		} else {
			throw new SocketServiceException("채널이 열려있지 않습니다.");
		}
		return channel;
	}

	private static DatagramChannel getDatagramChannel(
			ENUM_SERVICE_TYPE serviceType) throws IOException,
			SocketServiceException {

		StandardProtocolFamily protocolFamily = StandardProtocolFamily.INET;// Config???
		DatagramChannel channel = DatagramChannel.open(protocolFamily);
		if (channel.isOpen()) {
			// set some options
			SocketServiceHelper.setChannelOption(channel, serviceType);
			channel.configureBlocking(false);

			return channel;
		} else {
			throw new SocketServiceException("채널이 열려있지 않습니다.");
		}
	}

	private static DatagramChannel getMulticastDatagramChannel(
			ENUM_SERVICE_TYPE serviceType) throws IOException,
			SocketServiceException {
		String networkInterfaceName = SocketServiceHelper
				.getNetworkInterfaceName(InetAddress.getLocalHost()
						.getHostAddress());
		StandardProtocolFamily protocolFamily = StandardProtocolFamily.INET;
		DatagramChannel channel = DatagramChannel.open(protocolFamily);
		if (channel.isOpen()) {
			// set some options
			SocketServiceHelper.setMulticastChannelOption(channel,
					networkInterfaceName, serviceType);

			return channel;
		} else {
			throw new SocketServiceException("채널이 열려있지 않습니다.");
		}
	}

}
