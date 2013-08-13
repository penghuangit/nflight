package com.abreqadhabra.nflight.application.service.net.datagram.unicast.transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.service.net.Acceptor;
import com.abreqadhabra.nflight.application.service.net.NetworkServiceHelper;
import com.abreqadhabra.nflight.application.service.net.ServerChannelFactory;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class UnicastBlockingAcceptor implements Runnable, Acceptor {
	private static Class<UnicastBlockingAcceptor> THIS_CLAZZ = UnicastBlockingAcceptor.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private boolean isRunning;
	private Configure configure;
	private DatagramChannel channel;
	private Selector selector;

	public UnicastBlockingAcceptor(boolean isRunning,
			InetSocketAddress endpoint,
			/* ThreadPoolExecutor threadPool, */Configure configure)
			throws IOException, InterruptedException, ExecutionException {
		this.isRunning = isRunning;
		this.configure = configure;
		this.init(endpoint);
	}

	@Override
	public void init(InetSocketAddress endpoint) throws IOException,
			InterruptedException, ExecutionException {

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
			throw new IllegalStateException("서버 소켓 채널 또는 셀렉터가 열려있지 않습니다.");
		}

	}

	@Override
	public ServerChannelFactory createServerChannelFactory() {
		return new ServerChannelFactory();
	}

	@Override
	public void run() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {

			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getSimpleName(),
					METHOD_NAME,
					"current thread is "
							+ LoggingHelper.getThreadName(Thread
									.currentThread()));
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
						this.receive(this.selector, selectionKey);

					} else if (selectionKey.isWritable()) {
						this.send(this.selector, selectionKey);
					} else {
						LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
								METHOD_NAME, "Unexpected ops in select "
										+ selectionKey.readyOps());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void send(Selector selector, SelectionKey selectionKey) {
		// TODO Auto-generated method stub
	}

	private void receive(Selector selector, SelectionKey selectionKey) {

		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			DatagramChannel channel = (DatagramChannel) selectionKey.channel();

			int capacity = this.configure
					.getInt(Configure.UNICAST_INCOMING_BUFFER_CAPACITY);
			ByteBuffer incomingByteBuffer = NetworkServiceHelper
					.getByteBuffer(capacity);

			SocketAddress clientEndpoint = channel.receive(incomingByteBuffer);

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
			e.printStackTrace();
		}
	}
}
