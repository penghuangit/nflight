package com.abreqadhabra.nflight.application.server.service.socket.tcp.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.service.socket.tcp.common.ChangeRequest;
import com.abreqadhabra.nflight.common.exception.WrapperException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class StreamAcceptorImplBAK extends AbstractStreamAcceptorBAK {
    private static Class<StreamAcceptorImplBAK> THIS_CLAZZ = StreamAcceptorImplBAK.class;
    private static String CLASS_NAME = THIS_CLAZZ.getName();
    private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

    public StreamAcceptorImplBAK(InetAddress hostAddress, int port, AcceptorWorker worker) throws IOException {
	super(hostAddress, port, worker);
    }

    @Override
    public void run() {
	String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();
	try {
	    // 현재 실행 중인 쓰레드명을 변경 설정
	    String threadName = Thread.currentThread().getName();
	    Thread.currentThread().setName(threadName + "-" + THIS_CLAZZ.getSimpleName());

	    // 메시지를 표시하고 연결 유입을 대기
	    LOGGER.logp(Level.INFO, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "Waiting for connections...");

	    while (true) { // Run forever
		// 보류 중인 변경 내용들에 대한 확인
		synchronized (pendingChanges) {
		    // 보류 변경 내용이 있을 경우를 확인
		    for (ChangeRequest change : pendingChanges) {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, change.toString());
			// OPS의 변경일 경우
			switch (change.type) {
			case ChangeRequest.CHANGE_OPS:
			    // 소켓에서 셀렉터에 해당하는 셀렉션키를 취득
			    SelectionKey key = change.socketChannel.keyFor(super.selector);
			    // 셀렉션키의 OPS를 요청값으로 변경
			    key.interestOps(change.ops);
			    LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, change.toString());
			    break;
			default:
			    break;
			}

		    }
		    // 모든 보류 변경 내용을 소거
		    pendingChanges.clear();
		}

		// 등록된 서버 소켓 채널에 대한 이벤트 발생을 대기

		// int readyChannels = selector.select(TIMEOUT);
		selector.select();

		// 이벤트를 가진 셀렉터의 키 집합들을 확인
		for (SelectionKey key : selector.selectedKeys()) {

		    if (!key.isValid()) {
			continue;
		    }

		    if (key.isAcceptable()) {
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "a connection was accepted by a ServerSocketChannel.");
			this.accept(key);
		    } else if (key.isReadable()) {
			this.read(key);
		    } else if (key.isWritable()) {
			this.write(key);
		    }
		}
	    }
	} catch (Exception e) {
	    LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "\n" + WrapperException.getStackTrace(e));
	    AbstractStreamAcceptorBAK.exit();
	}
    }

    private void processSelectionKey(SelectionKey key) throws IOException {
	String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();

	if (LOGGER.isLoggable(Level.FINER)) {
	    int readySet = key.readyOps();
	    int interestSet = key.interestOps();
	    boolean isValid = key.isValid();
	    boolean isAcceptable = key.isAcceptable();
	    boolean isConnectable = key.isConnectable();
	    boolean isReadable = key.isReadable();
	    boolean isWritable = key.isWritable();
	    if (key.attachment() != null) {
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "attachment: " + key.attachment().getClass().getName());
	    }

	    LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "readySet: " + ChangeRequest.getOpsName(readySet) + " interestSet: "
		    + ChangeRequest.getOpsName(interestSet) + " isValid: " + isValid + " isConnectable: " + isConnectable + " isAcceptable: " + isAcceptable
		    + " isReadable: " + isReadable + " isWritable: " + isWritable);
	}

	// Get channel with connection request
	if (key.isConnectable()) {
	    LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "a connection was established with a remote server.");
	    SocketChannel sc = (SocketChannel) key.channel();
	    boolean success = sc.finishConnect();
	    if (!success) {
		// An error occurred; handle it
		// Unregister the channel with this selector
		key.cancel();
	    }
	}
	if (key.isAcceptable()) {
	    LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "a connection was accepted by a ServerSocketChannel.");
	    this.accept(key);
	}
	if (key.isReadable()) {
	    LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "a channel is ready for reading");
	    // Get channel with bytes to read
	    this.read(key);
	    // See Reading from a SocketChannel
	}
	if (key.isValid() && key.isWritable()) {
	    LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "a channel is ready for writing");
	    this.write(key);
	}

    }

}