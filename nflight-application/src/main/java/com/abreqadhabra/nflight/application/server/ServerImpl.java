package com.abreqadhabra.nflight.application.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Profile;
import com.abreqadhabra.nflight.application.launcher.concurrent.executor.ThreadPoolExecutorServiceImpl;
import com.abreqadhabra.nflight.application.launcher.concurrent.executor.monitor.ThreadPoolMonitorServiceImpl;
import com.abreqadhabra.nflight.application.server.net.tcp.aio.MessageDTO;
import com.abreqadhabra.nflight.application.server.net.tcp.aio.MessageDTOImpl;
import com.abreqadhabra.nflight.application.server.net.tcp.aio.SocketServerImpl;
import com.abreqadhabra.nflight.application.server.net.tcp.aio.logic.BusinessLogicHandler;
import com.abreqadhabra.nflight.application.server.net.tcp.aio.logic.BusinessLogicHandlerImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

//Strategy Context
public class ServerImpl implements IServer {
	private static final Class<ServerImpl> THIS_CLAZZ = ServerImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public ServerImpl(final Profile profile) {

		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				LoggingHelper.describe(THIS_CLAZZ));

		try {

			ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutorServiceImpl()
					.createNewThreadPool();
			threadPoolExecutor.allowCoreThreadTimeOut(true);

			boolean isThreadPoolMonitoring = false;
			int delaySeconds = 5;
			if (isThreadPoolMonitoring) {
				// Created executor is set to ThreadPoolMonitorService...
				ThreadPoolMonitorServiceImpl tpms = new ThreadPoolMonitorServiceImpl(
						delaySeconds);
				tpms.setExecutor(threadPoolExecutor);

				// ThreadPoolMonitorService is started...
				Thread monitor = new Thread(tpms);
				monitor.start();
			}

			BusinessLogicHandler logicHandler = new BusinessLogicHandlerImpl();
			
			MessageDTO messageDTO = new MessageDTOImpl();
			SocketServerImpl socketServer = new SocketServerImpl(
					new InetSocketAddress(InetAddress.getLocalHost()
							.getHostAddress(), 9999), threadPoolExecutor, messageDTO, logicHandler);
			
			// Runnable socketServerTask = socketServer.getSocketServerTask();
			// threadPoolExecutor.submit(socketServerTask);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void startup() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean status() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void shutdown() throws Exception {
		// TODO Auto-generated method stub

	}

}
