package com.abreqadhabra.nflight.application.transport.network.datagram.unicast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.NetworkChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.transport.exception.NetworkServiceException;
import com.abreqadhabra.nflight.application.transport.network.Service;
import com.abreqadhabra.nflight.application.transport.network.ServiceShutdownHook;
import com.abreqadhabra.nflight.application.transport.network.NetworkServiceHelper;
import com.abreqadhabra.nflight.application.transport.network.ServerChannelFactory;
import com.abreqadhabra.nflight.application.concurrency.AbstractRunnable;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.UnexpectedException;
import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class UnicastBlockingService extends AbstractRunnable
		implements
			Service {
	private static Class<UnicastBlockingService> THIS_CLAZZ = UnicastBlockingService.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private boolean isRunning;
	private Configure configure;
	private DatagramChannel channel;
	private Selector selector;

	public UnicastBlockingService(boolean isRunning, Configure configure,
			InetSocketAddress endpoint) throws NFlightException {
		super.setShutdownHookThread(new ServiceShutdownHook(this).getThread());
		this.isRunning = isRunning;
		this.configure = configure;
		this.init(endpoint);
	}

	@Override
	public void init(InetSocketAddress endpoint) throws NFlightException {
		try {

			// create a new server-socket channel & selector
			this.selector = Selector.open();
			this.channel = this.createServerChannelFactory()
					.createUnicastDatagramChannel(StandardProtocolFamily.INET,
							endpoint);
			// check that both of them were successfully opened
			if (this.selector.isOpen() && this.channel.isOpen()) {
				// Register the server socket channel, indicating an interest in
				// accepting new connections
				this.channel.register(this.selector, SelectionKey.OP_READ);

			} else {
				throw new NetworkServiceException("서버 소켓 채널 또는 셀렉터가 열려있지 않습니다.");
			}
		} catch (IOException | InterruptedException | ExecutionException e) {
			throw new NetworkServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}

	}

	@Override
	public ServerChannelFactory createServerChannelFactory() {
		return new ServerChannelFactory();
	}

	@Override
	public void start() throws NFlightException {
		final Thread CURRENT_THREAD = Thread.currentThread();
		final String METHOD_NAME = CURRENT_THREAD.getStackTrace()[1]
				.getMethodName();

		try {
			while (this.isRunning) {
				// wait for incoming an event one of the registered channels
				// 등록된 서버 소켓 채널에 대한 이벤트 발생을 대기
				long timeout = 1000;
				this.selector.select(timeout);
				// there is something to process on selected keys
				Iterator<SelectionKey> iterator = this.selector.selectedKeys()
						.iterator();
				while (iterator.hasNext()) {
					SelectionKey selectionKey = iterator.next();
					// 취득한 키를 키 집합에서 제거
					// prevent the same key from coming up again
					iterator.remove();
					if (!selectionKey.isValid()) {
						continue;
					}
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
							METHOD_NAME, NetworkServiceHelper
									.getReadySetString(selectionKey));
					// 이벤트가 사용할 수 있는지 확인하고 처리
					if (selectionKey.isReadable()) {
						DatagramChannel channel = (DatagramChannel) selectionKey
								.channel();
						this.receive(channel);
					} else if (selectionKey.isWritable()) {
						DatagramChannel channel = (DatagramChannel) selectionKey
								.channel();
						// this.send(channel);
					} else {
						LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
								METHOD_NAME, "Unexpected ops in select "
										+ selectionKey.readyOps());
					}
				}
			}
		} catch (IOException e) {
			throw new NetworkServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public void stop() throws NFlightException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			LOGGER.logp(Level.SEVERE, CLAZZ_NAME, METHOD_NAME, "Stopping...");
			this.isRunning = false;
			this.selector.close();
			this.channel.close();
			LOGGER.logp(Level.SEVERE, CLAZZ_NAME, METHOD_NAME, "Stopped");
		} catch (IOException e) {
			throw new NetworkServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		} finally {
			super.interrupt();
		}
	}

	@Override
	public void accept(NetworkChannel socketChannel) throws NFlightException {
	}

	@Override
	public void send(SocketChannel socket) {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(SocketChannel socket, Object message)
			throws NFlightException {
		// TODO Auto-generated method stub
	}

	@Override
	public void receive(NetworkChannel socketChannel) throws NFlightException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			int capacity = this.configure
					.getInt(Configure.UNICAST_INCOMING_BUFFER_CAPACITY);
			ByteBuffer incomingByteBuffer = NetworkServiceHelper
					.getByteBuffer(capacity);

			SocketAddress clientEndpoint = this.channel
					.receive(incomingByteBuffer);

			incomingByteBuffer.flip();
			if (incomingByteBuffer.hasRemaining()) {
				incomingByteBuffer.compact();
			} else {
				incomingByteBuffer.clear();
			}
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					new String(incomingByteBuffer.array(), "UTF-8") + " ["
							+ incomingByteBuffer.limit() + " bytes] from "
							+ clientEndpoint);
		} catch (IOException e) {
			throw new NetworkServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}
}