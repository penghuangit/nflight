package com.abreqadhabra.nflight.application.server.net.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.server.net.socket.logic.IBusinessLogicHandler;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class AsyncSocketServerAcceptor implements ISocketAcceptor {
	private static final Class<AsyncSocketServerAcceptor> THIS_CLAZZ = AsyncSocketServerAcceptor.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private final Configure configure;
	private final Long sessionId;
	private final AsynchronousSocketChannel asyncSocketChannel;
	// private final MessageDTO messageDTO;
	private final IBusinessLogicHandler logic;

	private ByteBuffer readByteBuffer;
	/** 입력데이터 */
	private final LinkedBlockingQueue<MessageDTO> inputQueue = new LinkedBlockingQueue<MessageDTO>();

	/** 출력데이터 */
	private final LinkedBlockingQueue<MessageDTO> outputQueue = new LinkedBlockingQueue<MessageDTO>();
	private final ConcurrentHashMap<String, Object> attrs = new ConcurrentHashMap<String, Object>();

	public AsyncSocketServerAcceptor(final Configure configure,
			final long sessionId,
			final AsynchronousSocketChannel asyncSocketChannel,
			final MessageDTO messageDTO,
			final IBusinessLogicHandler logicHandler) {
		this.configure = configure;
		this.sessionId = sessionId;
		this.asyncSocketChannel = asyncSocketChannel;
		// this.messageDTO = messageDTO;
		this.logic = logicHandler;
		init();
	}

	@Override
	public void init() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		NetworkChannelHelper.setChannelOption(this.getAsyncSocketChannel());

		this.setReadByteBuffer(NetworkChannelHelper.getByteBuffer(this.configure
				.getInt("nflight.socketserver.socket.async.bytebuffer.capacity")));
	}

	@Override
	public void start() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			if (this.getAsyncSocketChannel().isOpen()) {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME, "Incoming connection from: "
								+ this.getAsyncSocketChannel()
										.getRemoteAddress());
				this.receive();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			if (this.getAsyncSocketChannel().isOpen()) {
				LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
						METHOD_NAME,
						"close channel" + this.getAsyncSocketChannel());
				this.getAsyncSocketChannel().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void receive() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		this.getReadByteBuffer().clear();
		this.getAsyncSocketChannel().read(this.getReadByteBuffer(), this,
		// new ReadHandler(this)
				new CompletionHandler<Integer, AsyncSocketServerAcceptor>() {
					@Override
					public void completed(final Integer result,
							final AsyncSocketServerAcceptor acceptor) {

						try {
							LOGGER.logp(
									Level.FINER,
									THIS_CLAZZ.getSimpleName(),
									METHOD_NAME,

									AsyncSocketServerAcceptor.this.asyncSocketChannel
											+ "----------> reading Message: "
											+ AsyncSocketServerAcceptor.this.asyncSocketChannel
													.getRemoteAddress());
						} catch (IOException e1) {
							e1.printStackTrace();
						}

						if (result > 0) {
							AsyncSocketServerAcceptor.this.readByteBuffer
									.flip();

							// ?? 직렬화???
							final MessageDTO dto = null;
//									final MessageDTO dto = AsyncSocketServerAcceptor.this.messageDTO
//									.transfer(AsyncSocketServerAcceptor.this.readByteBuffer);

							LOGGER.logp(
									Level.FINER,
									THIS_CLAZZ.getSimpleName(),
									METHOD_NAME,
									dto.getClass().getName() + ":"
											+ dto.toString());
							try {
								AsyncSocketServerAcceptor.this.inputQueue
										.put(dto);
							} catch (final InterruptedException e) {
								e.printStackTrace();
							}

							LOGGER.logp(Level.FINER,
									THIS_CLAZZ.getSimpleName(), METHOD_NAME,
									AsyncSocketServerAcceptor.this.logic
											.getClass().getName());

							AsyncSocketServerAcceptor.this.logic.doLogic(
									acceptor,
									AsyncSocketServerAcceptor.this.inputQueue,
									AsyncSocketServerAcceptor.this.outputQueue);
							AsyncSocketServerAcceptor.this.readByteBuffer
									.clear();
						}
						if (result < 0) {
							acceptor.close();
							LOGGER.logp(Level.FINER,
									THIS_CLAZZ.getSimpleName(), METHOD_NAME,
									"데이터 읽기 오류로 채널을 닫습니다.");
							AsyncSocketServerAcceptor.this.logic.endCallBack();
							return;
						}
						AsyncSocketServerAcceptor.this.receive();
					}
					@Override
					public void failed(final Throwable exc,
							final AsyncSocketServerAcceptor acceptor) {
					}
				}

		);

	}

	@Override
	public void send() {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			MessageDTO messageDTO;
			if (this.getAsyncSocketChannel().isOpen()) {
				while ((messageDTO = this.outputQueue.poll()) != null) {
					LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(),
							METHOD_NAME, "sessionId: " + this.sessionId
									+ " / messageDTO: " + messageDTO);
					// + new String(messageDTO.getContent().array(),
					// Charset.forName("utf-8")));
					ByteBuffer srcByteBuffer = null;
					// ByteBuffer srcByteBuffer = messageDTO
					// .serializeObject(messageDTO);

					this.getAsyncSocketChannel()
							.write(srcByteBuffer, // messageDTO.getContent(),
									this,
									new CompletionHandler<Integer, AsyncSocketServerAcceptor>() {
										@Override
										public void completed(
												final Integer result,
												final AsyncSocketServerAcceptor acceptor) {
											if (result > 0) {
												try {
													LOGGER.logp(
															Level.FINER,
															THIS_CLAZZ
																	.getSimpleName(),
															METHOD_NAME,
															AsyncSocketServerAcceptor.this
																	.getAsyncSocketChannel()
																	+ "----------> writing Message: "
																	+ AsyncSocketServerAcceptor.this
																			.getAsyncSocketChannel()
																			.getRemoteAddress());
												} catch (IOException e) {
													// block
													e.printStackTrace();
												}
											}
										}
										@Override
										public void failed(
												final Throwable exc,
												final AsyncSocketServerAcceptor attachment) {
										}
									});
					messageDTO.getContent().flip();
				}

			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setAttribute(final String key, final Object value) {
		this.attrs.put(key, value);
	}

	@Override
	public Object getAttribute(final String key) {
		return this.attrs.get(key);
	}

	@Override
	public void addOutputQueue(final MessageDTO messageDTO) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				messageDTO.toString());
		try {
			this.outputQueue.put(messageDTO);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Long getSessionId() {
		return this.sessionId;
	}

	@Override
	public String toString() {
		return "AsyncSocketServerAcceptor [sessionId=" + this.sessionId
				+ ", asyncSocketChannel=" + this.getAsyncSocketChannel() + "]";
	}

	public LinkedBlockingQueue<MessageDTO> getOutputQueue() {
		return this.outputQueue;
	}

	public AsynchronousSocketChannel getAsyncSocketChannel() {
		return asyncSocketChannel;
	}

	public ByteBuffer getReadByteBuffer() {
		return readByteBuffer;
	}

	public void setReadByteBuffer(ByteBuffer readByteBuffer) {
		this.readByteBuffer = readByteBuffer;
	}

	public LinkedBlockingQueue<MessageDTO> getInputQueue() {
		return inputQueue;
	}

	public IBusinessLogicHandler getLogic() {
		return logic;
	}
}