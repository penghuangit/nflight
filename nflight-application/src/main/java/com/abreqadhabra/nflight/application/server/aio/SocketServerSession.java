package com.abreqadhabra.nflight.application.server.aio;

import java.io.IOException;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class SocketServerSession implements Session {
	private static final Class<SocketServerSession> THIS_CLAZZ = SocketServerSession.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private Long sessionId;
	private AsynchronousSocketChannel asyncSocketChannel;

	public SocketServerSession(long sessionId,
			AsynchronousSocketChannel asyncSocketChannel) {
		this.sessionId = sessionId;
		this.asyncSocketChannel = asyncSocketChannel;
	}

	@Override
	public Long getSessionId() {
		return sessionId;
	}

	@Override
	public void init(Configure config) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		if (LOGGER.isLoggable(Level.FINER)) {
			Set<SocketOption<?>> options = asyncSocketChannel
					.supportedOptions();
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"서버 소켓 채널의 지원 옵션:  " + options);
		}

		try {
			asyncSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE,
					Boolean.parseBoolean(config.get("keep_alive").trim()));
			asyncSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR,
					Boolean.parseBoolean(config.get("reuse_addr").trim()));
			asyncSocketChannel.setOption(StandardSocketOptions.TCP_NODELAY,
					Boolean.parseBoolean(config.get("tcp_nodelay").trim()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void open() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME, "...");
	}

	@Override
	public void read() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		ByteBuffer readBuf = ByteBuffer.allocate(1024);
		readBuf.clear();
		try {
			Future<Integer> future = asyncSocketChannel.read(readBuf);
			int length = future.get();
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"length :" + length);
			// future.get(100, TimeUnit.SECONDS);
			readBuf.flip();
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"Messaage From Client :  "
							+ new String(readBuf.array(), 0, length));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "SocketServerSession [sessionId=" + sessionId
				+ ", asyncSocketChannel=" + asyncSocketChannel + "]";
	}

}
