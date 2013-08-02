package com.abreqadhabra.nflight.application.server.net.tcp.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.server.net.tcp.Session;
import com.abreqadhabra.nflight.application.server.net.tcp.aio.logic.BusinessLogicHandler;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class SocketServerSession implements Session {
	private static final Class<SocketServerSession> THIS_CLAZZ = SocketServerSession.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private Long sessionId;
	private AsynchronousSocketChannel asyncSocketChannel;
	private MessageDTO messageDTO;
	private BusinessLogicHandler logicHandler;

	private ByteBuffer buffer;
	/** 입력데이터 */
	private final LinkedBlockingQueue<MessageDTO> inputQueue = new LinkedBlockingQueue<MessageDTO>();

	/** 출력데이터 */
	private final LinkedBlockingQueue<MessageDTO> outputQueue = new LinkedBlockingQueue<MessageDTO>();
	private ConcurrentHashMap<String, Object> attrs = new ConcurrentHashMap<String, Object>();

	// public SocketServerSession(long sessionId,
	// AsynchronousSocketChannel asyncSocketChannel) {
	// this.sessionId = sessionId;
	// this.asyncSocketChannel = asyncSocketChannel;
	// }

	public SocketServerSession(long sessionId,
			AsynchronousSocketChannel asyncSocketChannel,
			MessageDTO messageDTO, BusinessLogicHandler logicHandler) {
		this.sessionId = sessionId;
		this.asyncSocketChannel = asyncSocketChannel;
		this.messageDTO = messageDTO;
		this.logicHandler = logicHandler;
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

			asyncSocketChannel.setOption(
					StandardSocketOptions.TCP_NODELAY,
					Boolean.parseBoolean(config.get(
							"nflight.socketserver.option.tcp_nodelay").trim()));
			asyncSocketChannel
					.setOption(StandardSocketOptions.SO_REUSEADDR, Boolean
							.parseBoolean(config.get(
									"nflight.socketserver.option.so_reuseaddr")
									.trim()));
			asyncSocketChannel.setOption(
					StandardSocketOptions.SO_SNDBUF,
					Integer.parseInt(config.get(
							"nflight.socketserver.option.so_sndbuf").trim()));
			asyncSocketChannel
					.setOption(StandardSocketOptions.SO_KEEPALIVE, Boolean
							.parseBoolean(config.get(
									"nflight.socketserver.option.so_keepalive")
									.trim()));
			asyncSocketChannel.setOption(
					StandardSocketOptions.SO_RCVBUF,
					Integer.parseInt(config.get(
							"nflight.socketserver.option.so_rcvbuf").trim()));

		} catch (IOException e) {
			e.printStackTrace();
		}

		int buffer_size = 1024*2; //Integer.parseInt(config.get("buffer_size").trim());
		buffer = ByteBuffer.allocate(buffer_size);
	}

	@Override
	public void open() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"Open Session");
	}

	@Override
	public void read() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		buffer.clear();
		asyncSocketChannel.read(buffer, this,
				new CompletionHandler<Integer, SocketServerSession>() {
					public void completed(Integer result,
							SocketServerSession session) {
						if (result > 0) {
							buffer.flip();
							MessageDTO dto = messageDTO.transfer(buffer);
							try {
								inputQueue.put(dto);
							} catch (InterruptedException e) {

								e.printStackTrace();
							}
							logicHandler.doLogic(session, inputQueue,
									outputQueue);
							buffer.clear();
						}
						if (result < 0) {
							session.close();
							LOGGER.logp(Level.FINER,
									THIS_CLAZZ.getSimpleName(), METHOD_NAME,
									"데이터 읽기 오류로 채널을 닫습니다.");
							logicHandler.endCallBack();
							return;
						}
						read();
					}
					public void failed(Throwable exc,
							SocketServerSession session) {
					}
				});
	}

	@Override
	public void setAttribute(String key, Object value) {
		attrs.put(key, value);
	}

	@Override
	public Object getAttribute(String key) {
		return attrs.get(key);
	}

	@Override
	public void add(MessageDTO messageDTO) {
		try {
			outputQueue.put(messageDTO);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			MessageDTO messageDTO;
			if (asyncSocketChannel.isOpen()) {
				while ((messageDTO = outputQueue.poll()) != null) {
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
							METHOD_NAME, "Session Id:"
									+ sessionId
									+ new String(messageDTO.getContent()
											.array(), Charset.forName("utf-8")));
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
							METHOD_NAME, asyncSocketChannel.toString());
					asyncSocketChannel
							.write(messageDTO.getContent(),
									this,
									new CompletionHandler<Integer, SocketServerSession>() {
										public void completed(Integer result,
												SocketServerSession attachment) {
											if (result > 0) {
												LOGGER.logp(
														Level.FINER,
														THIS_CLAZZ
																.getSimpleName(),
														METHOD_NAME, "write ok");
											}
										}
										public void failed(Throwable exc,
												SocketServerSession attachment) {
										}
									});
					messageDTO.getContent().flip();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			if (asyncSocketChannel.isOpen()) {
				SocketAddress remote = asyncSocketChannel.getRemoteAddress();
				if (remote instanceof InetSocketAddress) {
					InetSocketAddress rInet = (InetSocketAddress) remote;
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
							METHOD_NAME, rInet.getHostName());
				}
				read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			asyncSocketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "SocketServerSession [sessionId=" + sessionId
				+ ", asyncSocketChannel=" + asyncSocketChannel + "]";
	}

	public LinkedBlockingQueue<MessageDTO> getOutputQueue() {
		return outputQueue;
	}
}
