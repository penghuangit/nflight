package com.abreqadhabra.nflight.application.server.service.socket.udp.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.service.socket.tcp.common.ChangeRequest;
import com.abreqadhabra.nflight.application.server.service.socket.tcp.impl.AcceptorWorker;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class DatagramAcceptorImpl extends AbstractDatagramAcceptor {
	private static final Class<DatagramAcceptorImpl> THIS_CLAZZ = DatagramAcceptorImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public DatagramAcceptorImpl(final InetAddress hostAddress, final int port,
			final AcceptorWorker worker) throws IOException {
		super(hostAddress, port, worker);
	}

	@Override
	public void run() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// 현재 실행 중인 쓰레드명을 변경 설정
		final String threadName = Thread.currentThread().getName();
		Thread.currentThread().setName(
				threadName + "-" + THIS_CLAZZ.getSimpleName());

		// 메시지를 표시하고 연결 유입을 대기
		LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"Waiting for connections...");

		while (true) {
			try {
				// 보류 중인 변경 내용들에 대한 처리
				synchronized (this.pendingChanges) {
					final Iterator<ChangeRequest> changes = this.pendingChanges
							.iterator();
					// 보류 변경 내용이 있을 경우
					while (changes.hasNext()) {
						final ChangeRequest change = changes.next();
						// OPS의 변경일 경우
						if (change.type.equals(ChangeRequest.CHANGE_OPS)) {
							// 소켓에서 셀렉터에 해당하는 셀렉션키를 취득
							final SelectionKey key = change.socketChannel
									.keyFor(super.selector);
							// 셀렉션키의 OPS를 요청값으로 변경
							key.interestOps(change.ops);
								LOGGER.logp(
									Level.FINER,
									THIS_CLAZZ.getSimpleName(),
									METHOD_NAME,
									"보류 변경 유형: "
											+ change.type
											+ ":"
											+ ChangeRequest.getOpsName(key
													.interestOps())
											+ "->"
											+ ChangeRequest
													.getOpsName(change.ops)
											+ "\n" + change);
						}
					}
					// 모든 보류 변경 내용을 소거
					this.pendingChanges.clear();
				}
				// 등록된 데이터그램 채널에 대한 이벤트 발생을 대기
				int readyChannels = this.selector.select();

				if (readyChannels == 0) {
					// 0이면 가장 가까운 반복문의 초기위치로 이동
					continue;
				} else {
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
							METHOD_NAME, "number of keys: " + readyChannels);
				}

				// 이벤트를 사용할 수 있는 셀렉터의 키 집합들을 확인
				final Iterator<?> selectedKeys = this.selector.selectedKeys()
						.iterator();

				// 이벤트가 있을 경우
				while (selectedKeys.hasNext()) {
					// 셀렉터의 키를 취득
					final SelectionKey key = (SelectionKey) selectedKeys.next();
					// 취득한 키를 키 집합에서 제거
					selectedKeys.remove();

					
					
					//Object o = ResponseHandler.deserializeObject(buf.array());
//					/LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),					METHOD_NAME, buffer.array().length + "-------------------> " + client.);
					try {
						Thread.sleep(100000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if (!key.isValid()) {
						// false이면 가장 가까운 반복문의 초기위치로 이동
						continue;
					}

					// 이벤트가 사용할 수 있는지 확인하고 처리
					this.processSelectionKey(key);

				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void processSelectionKey(SelectionKey key) throws IOException {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		if (LOGGER.isLoggable(Level.FINER)) {
			int readySet = key.readyOps();
			int interestSet = key.interestOps();
			boolean isValid = key.isValid();
			boolean isAcceptable = key.isAcceptable();
			boolean isConnectable = key.isConnectable();
			boolean isReadable = key.isReadable();
			boolean isWritable = key.isWritable();
			if (key.attachment() != null) {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, "attachment: "
								+ key.attachment().getClass().getName());
			}

			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getSimpleName(),
					METHOD_NAME,
					"readySet: " + ChangeRequest.getOpsName(readySet)
							+ " interestSet: "
							+ ChangeRequest.getOpsName(interestSet)
							+ " isValid: " + isValid + " isConnectable: "
							+ isConnectable + " isAcceptable: " + isAcceptable
							+ " isReadable: " + isReadable + " isWritable: "
							+ isWritable);

		}


		if (key.isAcceptable()) {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"a connection was accepted by a ServerSocketChannel.");
			key.channel();
			this.accept(key);
		}
		if (key.isReadable()) {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"a channel is ready for reading");
			
	

			
			// Get channel with bytes to read
			this.read(key);
			// See Reading from a SocketChannel
		}
		if (key.isValid() && key.isWritable()) {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"a channel is ready for writing");
			this.write(key);
		}

	}
	

}