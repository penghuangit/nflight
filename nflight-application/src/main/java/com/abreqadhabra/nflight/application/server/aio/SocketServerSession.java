package com.abreqadhabra.nflight.application.server.aio;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class SocketServerSession implements Session {
	private static final Class<SocketServerSession> THIS_CLAZZ = SocketServerSession.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private Long id;
	private AsynchronousSocketChannel channel;

	public SocketServerSession(long id, AsynchronousSocketChannel channel) {
		this.id = id;
		this.channel = channel;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void init(Configure config) {
		try {
			channel.setOption(StandardSocketOptions.SO_KEEPALIVE,
					Boolean.parseBoolean(config.get("keep_alive").trim()));
			channel.setOption(StandardSocketOptions.SO_REUSEADDR,
					Boolean.parseBoolean(config.get("reuse_addr").trim()));
			channel.setOption(StandardSocketOptions.TCP_NODELAY,
					Boolean.parseBoolean(config.get("tcp_nodelay").trim()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void open() {
		LOGGER.info("...");
	}

	@Override
	public void read() {
		ByteBuffer readBuf = ByteBuffer.allocate(1024);
		readBuf.clear();
		try {
			Future<Integer> future = channel.read(readBuf);
			int length = future.get();
			LOGGER.info("length :" + length);
			// future.get(100, TimeUnit.SECONDS);
			readBuf.flip();
			LOGGER.info("Messaage From Client :  "
					+ new String(readBuf.array(), 0, length));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
