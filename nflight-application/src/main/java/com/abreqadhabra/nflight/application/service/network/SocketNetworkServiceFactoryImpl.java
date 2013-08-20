package com.abreqadhabra.nflight.application.service.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

import com.abreqadhabra.nflight.application.common.launcher.Config;
import com.abreqadhabra.nflight.application.common.launcher.concurrent.executor.ThreadPoolImpl;
import com.abreqadhabra.nflight.application.service.conf.ServiceConfig.ENUM_SERVICE_TYPE;
import com.abreqadhabra.nflight.application.service.network.socket.conf.SocketServiceConfig;
import com.abreqadhabra.nflight.application.service.network.socket.exception.SocketServiceException;
import com.abreqadhabra.nflight.application.service.network.socket.helper.SocketServiceHelper;
import com.abreqadhabra.nflight.application.service.network.socket.impl.datagram.MulticastDatagramServiceImpl;
import com.abreqadhabra.nflight.application.service.network.socket.impl.datagram.UnicastSocketServiceImpl;
import com.abreqadhabra.nflight.application.service.network.socket.impl.stream.AsynchronousSocketServiceImpl;
import com.abreqadhabra.nflight.application.service.network.socket.impl.stream.BlockingSocketServiceImpl;
import com.abreqadhabra.nflight.application.service.network.socket.impl.stream.NonblockingSocketServiceImpl;
import com.abreqadhabra.nflight.common.exception.NFlightException;

public class SocketNetworkServiceFactoryImpl extends NetworkServiceFactory {

	private ENUM_SERVICE_TYPE serviceType;

	public SocketNetworkServiceFactoryImpl(ENUM_SERVICE_TYPE serviceType) {
		this.serviceType = serviceType;
	}
	@Override
	public Runnable createService() throws NFlightException {

		try {
			if (this.serviceType.equals(ENUM_SERVICE_TYPE.network_blocking)) {
				//새로운 채널을 생성
				ServerSocketChannel channel = ServerSocketChannel.open();
				//생성된 채널이 성공적으로 오픈하였는지를 확인
				if (channel.isOpen()) {
					// 생성된 채널을 블로킹 모드로 설정 
					channel.configureBlocking(true);
					// ServerSocketChannel에 설정 가능한 옵션들을 자동 설정
					SocketServiceHelper.setChannelOption(channel,
							this.serviceType);
					// 서비스 할 소켓주소 객체를 생성 (IP 주소는 로컬 주소를 자동 할당)
					InetSocketAddress endpoint = getEndpoint(Config
							.getInt(SocketServiceConfig.KEY_INT_SOCKET_BLOCKING_DEFAULT_PORT));
					// 블로킹 소켓 서비스에서 사용할 쓰레드풀 객체 생성
					ThreadPoolExecutor threadPool = new ThreadPoolImpl()
					.getThreadPoolExecutor(
							Config.get(SocketServiceConfig.KEY_INT_SOCKET_BLOCKING_SERVICE_THREAD_POOL_NAME),
							Config.getBoolean(SocketServiceConfig.KEY_INT_SOCKET_BLOCKING_SERVICE_THREAD_POOL_MONITORING),
							Config.getInt(SocketServiceConfig.KEY_INT_SOCKET_BLOCKING_SERVICE_THREAD_POOL_MONITORING_DELAY_SECONDS));
					// 블로킹 소켓 서비스 객체 생성
					return new BlockingSocketServiceImpl(channel, endpoint,
							threadPool);
				} else {
					throw new SocketServiceException("채널이 열려있지 않습니다.");
				}
			} else if (this.serviceType
					.equals(ENUM_SERVICE_TYPE.network_nonblocking)) {
				Config.getInt(SocketServiceConfig.KEY_INT_SOCKET_NONBLOCKING_BIND_BACKLOG);
				ServerSocketChannel channel = ServerSocketChannel.open();
				if (channel.isOpen()) {
					channel.configureBlocking(false);
					// set some options
					SocketServiceHelper.setChannelOption(channel,
							this.serviceType);
					InetSocketAddress endpoint = getEndpoint(Config
							.getInt(SocketServiceConfig.KEY_INT_SOCKET_NONBLOCKING_DEFAULT_PORT));
					return new NonblockingSocketServiceImpl(channel, endpoint);
				} else {
					throw new SocketServiceException("채널이 열려있지 않습니다.");
				}
			} else if (this.serviceType.equals(ENUM_SERVICE_TYPE.network_async)) {
				ThreadPoolExecutor threadPool = new ThreadPoolImpl()
						.getThreadPoolExecutor(
								Config.get(SocketServiceConfig.KEY_STR_SOCKET_ASYNC_SERVICE_THREAD_POOL_NAME),
								Config.getBoolean(SocketServiceConfig.KEY_BOO_SOCKET_ASYNC_SERVICE_THREAD_POOL_MONITORING),
								Config.getInt(SocketServiceConfig.KEY_INT_SOCKET_ASYNC_SERVICE_THREAD_POOL_MONITORING_DELAY_SECONDS));
				int initialSize = Config
						.getInt(SocketServiceConfig.KEY_INT_SOCKET_ASYNC_THREADPOOL_INITIALSIZE);
				// create asynchronous server-socket channel bound to the thread
				// Group
				AsynchronousChannelGroup threadGroup = AsynchronousChannelGroup
						.withCachedThreadPool(threadPool, initialSize);
				AsynchronousServerSocketChannel channel = AsynchronousServerSocketChannel
						.open(threadGroup);
				if (channel.isOpen()) {
					// set some options
					SocketServiceHelper.setChannelOption(channel,
							this.serviceType);
					InetSocketAddress endpoint = getEndpoint(Config
							.getInt(SocketServiceConfig.KEY_INT_SOCKET_ASYNC_DEFAULT_PORT));
					return new AsynchronousSocketServiceImpl(channel, endpoint,
							threadPool);
				} else {
					throw new SocketServiceException("채널이 열려있지 않습니다.");
				}
			} else if (this.serviceType
					.equals(ENUM_SERVICE_TYPE.network_unicast)) {
				StandardProtocolFamily protocolFamily = StandardProtocolFamily.INET;// Config???
				DatagramChannel channel = DatagramChannel.open(protocolFamily);
				if (channel.isOpen()) {
					// set some options
					SocketServiceHelper.setChannelOption(channel,
							this.serviceType);
					channel.configureBlocking(false);
					InetSocketAddress endpoint = getEndpoint(Config
							.getInt(SocketServiceConfig.KEY_INT_SOCKET_UNICAST_DEFAULT_PORT));
					return new UnicastSocketServiceImpl(channel, endpoint);
				} else {
					throw new SocketServiceException("채널이 열려있지 않습니다.");
				}
			} else if (this.serviceType
					.equals(ENUM_SERVICE_TYPE.network_multicast)) {
				String networkInterfaceName = SocketServiceHelper
						.getNetworkInterfaceName(InetAddress.getLocalHost()
								.getHostAddress());
				StandardProtocolFamily protocolFamily = StandardProtocolFamily.INET;
				DatagramChannel channel = DatagramChannel.open(protocolFamily);
				if (channel.isOpen()) {
					// set some options
					SocketServiceHelper.setMulticastChannelOption(channel,
							networkInterfaceName, this.serviceType);
					InetSocketAddress endpoint = getEndpoint(Config
							.getInt(SocketServiceConfig.KEY_INT_SOCKET_MULTICAST_DEFAULT_PORT));
					return new MulticastDatagramServiceImpl(channel, endpoint);
				} else {
					throw new SocketServiceException("채널이 열려있지 않습니다.");
				}
			} else {
				throw new SocketServiceException("서비스 객체 생성에 실패하였습니다.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
