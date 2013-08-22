package com.abreqadhabra.nflight.application.service.network.socket.impl.stream;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.NetworkChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.Config;
import com.abreqadhabra.nflight.application.service.network.socket.AbstractSocketServiceTask;
import com.abreqadhabra.nflight.application.service.network.socket.SocketService;
import com.abreqadhabra.nflight.application.service.network.socket.SocketServiceDescriptor;
import com.abreqadhabra.nflight.application.service.network.socket.conf.SocketServiceConfig;
import com.abreqadhabra.nflight.application.service.network.socket.exception.SocketServiceException;
import com.abreqadhabra.nflight.application.service.network.socket.helper.SocketServiceHelper;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.UnexpectedException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class NonblockingSocketServiceImpl.
 */
public class NonblockingSocketServiceImpl extends AbstractSocketServiceTask
		implements
			SocketService {

	/** The this clazz. */
	private static Class<NonblockingSocketServiceImpl> THIS_CLAZZ = NonblockingSocketServiceImpl.class;

	/** The clazz name. */
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();

	/** The logger. */
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	/** The channel. */
	private ServerSocketChannel channel;

	/** The endpoint. */
	private InetSocketAddress endpoint;

	/** The selector. */
	private Selector selector;

	/**
	 * Instantiates a new nonblocking socket service impl.
	 * 
	 * @param serviceDescriptor
	 *            the service descriptor
	 * @throws NFlightException
	 *             the n flight exception
	 */
	public NonblockingSocketServiceImpl(
			SocketServiceDescriptor serviceDescriptor) throws NFlightException {
		super(
				Config.getBoolean(SocketServiceConfig.KEY_BOO_SOCKET_NONBLOCKING_RUNNING));
		this.channel = serviceDescriptor.getServerSocketChannel();
		this.endpoint = serviceDescriptor.getEndpoint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.abreqadhabra.nflight.application.common.launcher.concurrent.
	 * AbstractRunnable#start()
	 */
	@Override
	public void behavior() {
		final Thread CURRENT_THREAD = Thread.currentThread();
		final String METHOD_NAME = CURRENT_THREAD.getStackTrace()[1]
				.getMethodName();
		try {
			this.bind();
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
					LOGGER.logp(Level.FINER, CLAZZ_NAME, METHOD_NAME,
							SocketServiceHelper.getReadySetString(selectionKey));

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
						LOGGER.logp(
								Level.FINER,
								CLAZZ_NAME,
								METHOD_NAME,
								"Unexpected ops in select "
										+ selectionKey.readyOps());
					}
				}
			}
		} catch (IOException | NFlightException e) {

		}
	}

	@Override
	public boolean status() {
		return this.isRunning;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.abreqadhabra.nflight.application.common.launcher.concurrent.
	 * AbstractRunnable#stop()
	 */
	@Override
	public void shutdown() {
		Thread CURRENT_THREAD = Thread.currentThread();
		if (!"main".equals(CURRENT_THREAD.getName())) {
			try {
				this.isRunning = false;
				this.selector.close();
				this.channel.close();
				this.interrupt(CLAZZ_NAME);
			} catch (IOException e) {

			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.abreqadhabra.nflight.application.service.network.socket.SocketService
	 * #bind()
	 */
	@Override
	public void bind() throws NFlightException {
		final Thread CURRENT_THREAD = Thread.currentThread();
		final String METHOD_NAME = CURRENT_THREAD.getStackTrace()[1]
				.getMethodName();
		try {
			// create a new server-socket channel & selector
			this.selector = Selector.open();
			// check that both of them were successfully opened
			if (this.selector.isOpen() && this.channel.isOpen()) {
				int backlog = Config
						.getInt(SocketServiceConfig.KEY_INT_SOCKET_NONBLOCKING_BIND_BACKLOG);
				// create a new server-socket channel
				this.channel.bind(this.endpoint, backlog);
				// Register the server socket channel, indicating an interest in
				// accepting new connections
				this.channel.register(this.selector, SelectionKey.OP_ACCEPT);
				// display a waiting message while ... waiting clients
				LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, "Waiting for connections ..."
								+ this.endpoint);

			} else {
				throw new SocketServiceException("서버 소켓 채널 또는 셀렉터가 열려있지 않습니다.");
			}
		} catch (IOException e) {
			throw new SocketServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.abreqadhabra.nflight.application.service.network.socket.SocketService
	 * #accept(java.nio.channels.NetworkChannel)
	 */
	@Override
	public void accept(NetworkChannel socketChannel) throws NFlightException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			SocketChannel socket = (SocketChannel) socketChannel;
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
			throw new SocketServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.abreqadhabra.nflight.application.service.network.socket.SocketService
	 * #send(java.nio.channels.SocketChannel)
	 */
	@Override
	public void send(SocketChannel socket) {
		// 큐에 있는 스트림을 전송
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.abreqadhabra.nflight.application.service.network.socket.SocketService
	 * #send(java.nio.channels.SocketChannel, java.lang.Object)
	 */
	@Override
	public void send(SocketChannel socket, Object message)
			throws NFlightException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		LOGGER.logp(Level.FINER, CLAZZ_NAME, METHOD_NAME, socket.toString());
		LOGGER.logp(Level.FINER, CLAZZ_NAME, METHOD_NAME, "send");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.abreqadhabra.nflight.application.service.network.socket.SocketService
	 * #receive(java.nio.channels.NetworkChannel)
	 */
	@Override
	public void receive(NetworkChannel socketChannel) throws NFlightException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		SocketChannel socket = (SocketChannel) socketChannel;
		try {
			int capacity = Config
					.getInt(SocketServiceConfig.KEY_INT_SOCKET_NONBLOCKING_INCOMING_BUFFER_CAPACITY);
			ByteBuffer incomingByteBuffer = SocketServiceHelper
					.getByteBuffer(capacity);
			int numRead = socket.read(incomingByteBuffer);
			incomingByteBuffer.flip();
			if (incomingByteBuffer.hasRemaining()) {
				incomingByteBuffer.compact();
			} else {
				incomingByteBuffer.clear();
			}
			LOGGER.logp(Level.FINER, CLAZZ_NAME, METHOD_NAME, new String(
					incomingByteBuffer.array(), "UTF-8")
					+ " ["
					+ numRead
					+ " bytes] from " + socket.getRemoteAddress());
		} catch (IOException e) {
			throw new SocketServiceException(e);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		} finally {
			try {
				socket.close();
				LOGGER.logp(Level.FINER, CLAZZ_NAME, METHOD_NAME, "isOpen="
						+ Boolean.toString(socket.isOpen()) + ", isConnected="
						+ Boolean.toString(socket.isConnected()));
			} catch (IOException e) {
				throw new SocketServiceException(e);
			} catch (Exception e) {
				throw new UnexpectedException(e);
			}
		}
	}

	@Override
	public String sayHello() {
		// TODO Auto-generated method stub
		return null;
	}
}
