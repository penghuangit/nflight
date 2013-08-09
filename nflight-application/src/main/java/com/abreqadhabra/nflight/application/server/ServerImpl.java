package com.abreqadhabra.nflight.application.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.application.launcher.Profile;
import com.abreqadhabra.nflight.application.launcher.concurrent.executor.ThreadPoolExecutorServiceImpl;
import com.abreqadhabra.nflight.application.launcher.concurrent.executor.monitor.ThreadPoolMonitorServiceImpl;
import com.abreqadhabra.nflight.application.server.net.async.AsyncServerImpl;
import com.abreqadhabra.nflight.application.server.net.async.logic.BusinessLogicImpl;
import com.abreqadhabra.nflight.application.server.net.async.logic.IBusinessLogic;
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
//			if (isThreadPoolMonitoring) {
//				// Created executor is set to ThreadPoolMonitorService...
//				ThreadPoolMonitorServiceImpl tpms = new ThreadPoolMonitorServiceImpl(
//						delaySeconds);
//				tpms.setExecutor(threadPoolExecutor);
//
//				// ThreadPoolMonitorService is started...
//				Thread monitor = new Thread(tpms);
//				monitor.start();
//			}

			IBusinessLogic logic = new BusinessLogicImpl();
			AsyncServerImpl asyncStreamServer = new AsyncServerImpl(
					new ConfigureImpl(Configure.FILE_SOCKET_SERVER_PROPERTIES),
					threadPoolExecutor, new InetSocketAddress(InetAddress
							.getLocalHost().getHostAddress(), 9999),
							logic);
			
			asyncStreamServer.startup();
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
