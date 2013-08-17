package com.abreqadhabra.nflight.application.trash_server.net.async.handler;

import java.nio.channels.CompletionHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class AsyncServerSendHandler implements CompletionHandler<Integer, Void> {
	private static Class<AsyncServerSendHandler> THIS_CLAZZ = AsyncServerSendHandler.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getSimpleName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	@Override
	public void completed(Integer result, Void attachment) {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		if (result > 0) {

		

		}
	}

	@Override
	public void failed(Throwable exc, Void attachment) {
		// TODO Auto-generated method stub

	}

}
