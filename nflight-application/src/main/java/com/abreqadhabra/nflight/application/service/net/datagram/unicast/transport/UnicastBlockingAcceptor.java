package com.abreqadhabra.nflight.application.service.net.datagram.unicast.transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.exception.ServiceException;
import com.abreqadhabra.nflight.application.service.net.Acceptor;
import com.abreqadhabra.nflight.application.service.net.AcceptorShutdownHook;
import com.abreqadhabra.nflight.application.service.net.NetworkServiceHelper;
import com.abreqadhabra.nflight.application.service.net.ServerChannelFactory;
import com.abreqadhabra.nflight.common.concurrency.AbstractRunnable;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.UnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class UnicastBlockingAcceptor extends AbstractRunnable
		implements
			Acceptor {
	private static Class<UnicastBlockingAcceptor> THIS_CLAZZ = UnicastBlockingAcceptor.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private boolean isRunning;
	private Configure configure;
	private DatagramChannel channel;
	private Selector selector;

	public UnicastBlockingAcceptor(boolean isRunning,
			InetSocketAddress endpoint, Configure configure)
			throws NFlightException {
		super.setShutdownHookThread(new AcceptorShutdownHook(this).getThread());
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
				throw new ServiceException("서버 소켓 채널 또는 셀렉터가 열려있지 않습니다.");
			}
		} catch (IOException | InterruptedException | ExecutionException e) {
			throw new ServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
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
					if (selectionKey.isAcceptable()) {
						SocketChannel socket = ((ServerSocketChannel) selectionKey
								.channel()).accept();
						this.accept(socket);
					} else if (selectionKey.isReadable()) {
						SocketChannel socket = (SocketChannel) selectionKey
								.attachment();
						this.receive(socket);
					} else if (selectionKey.isWritable()) {
						SocketChannel socket = (SocketChannel) selectionKey
								.attachment();
						this.send(socket);
					} else {
						LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
								METHOD_NAME, "Unexpected ops in select "
										+ selectionKey.readyOps());
					}
				}
			}
		} catch (IOException e) {
			throw new ServiceException(e);
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
			throw new ServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		} finally {
			super.interrupt();
		}
	}

	@Override
	public void accept(SocketChannel socket) throws NFlightException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			LOGGER.logp(
					Level.FINER,
					CLAZZ_NAME,
					METHOD_NAME,
					"Accepted socket connection from "
							+ socket.getRemoteAddress());
			socket.configureBlocking(false);
			SelectionKey readyKey = socket.register(this.selector,
					SelectionKey.OP_READ);
			readyKey.attach(socket);
		} catch (IOException e) {
			throw new ServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
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
	public void receive(SocketChannel socket) throws NFlightException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			// DatagramChannel channel = (DatagramChannel)
			// selectionKey.channel();
			System.out.println(socket.getClass().getName());
			int capacity = this.configure
					.getInt(Configure.UNICAST_INCOMING_BUFFER_CAPACITY);
			ByteBuffer incomingByteBuffer = NetworkServiceHelper
					.getByteBuffer(capacity);
			int numRead = socket.read(incomingByteBuffer);
			incomingByteBuffer.flip();
			if (incomingByteBuffer.hasRemaining()) {
				incomingByteBuffer.compact();
			} else {
				incomingByteBuffer.clear();
			}
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					new String(incomingByteBuffer.array(), "UTF-8") + " ["
							+ numRead + " bytes] from ");
		} catch (IOException e) {
			throw new ServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public ServerChannelFactory createServerChannelFactory() {
		return new ServerChannelFactory();
	}

}
