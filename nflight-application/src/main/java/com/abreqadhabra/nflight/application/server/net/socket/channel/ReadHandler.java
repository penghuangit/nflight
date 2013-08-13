package com.abreqadhabra.nflight.application.server.net.socket.channel;

import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.net.socket.AsyncSocketServerAcceptor;
import com.abreqadhabra.nflight.application.server.net.socket.MessageDTO;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ReadHandler
		implements
			CompletionHandler<Integer, AsyncSocketServerAcceptor> {
	private static Class<ReadHandler> THIS_CLAZZ = ReadHandler.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private AsyncSocketServerAcceptor asyncSocketServerAcceptor;

	public ReadHandler(AsyncSocketServerAcceptor asyncSocketServerAcceptor) {
		this.asyncSocketServerAcceptor = asyncSocketServerAcceptor;
	}

	@Override
	public void completed(Integer result,
			AsyncSocketServerAcceptor acceptor) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			LOGGER.logp(
					Level.FINER,
					THIS_CLAZZ.getSimpleName(),
					METHOD_NAME,

					asyncSocketServerAcceptor.getAsyncSocketChannel()
							+ "----------> reading Message: "
							+ asyncSocketServerAcceptor.getAsyncSocketChannel()
									.getRemoteAddress());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (result > 0) {
			asyncSocketServerAcceptor.getReadByteBuffer().flip();

			// ?? 직렬화???
//			MessageDTO dto = asyncSocketServerAcceptor.messageDTO
//					.transfer(asyncSocketServerAcceptor.getReadByteBuffer());
//
//			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
//					dto.getClass().getName() + ":" + dto.toString());
//			try {
//				asyncSocketServerAcceptor.getInputQueue().put(dto);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					asyncSocketServerAcceptor.getLogic().getClass().getName());

			asyncSocketServerAcceptor.getLogic().doLogic(acceptor,
					asyncSocketServerAcceptor.getInputQueue(),
					asyncSocketServerAcceptor.getOutputQueue());
			asyncSocketServerAcceptor.getReadByteBuffer().clear();
		}
		if (result < 0) {
			acceptor.close();
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"데이터 읽기 오류로 채널을 닫습니다.");
			asyncSocketServerAcceptor.getLogic().endCallBack();
			return;
		}
		asyncSocketServerAcceptor.receive();
	}

	@Override
	public void failed(Throwable exc,
			AsyncSocketServerAcceptor acceptor) {
	}
}