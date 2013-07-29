package com.abreqadhabra.nflight.application.server;

import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Profile;
import com.abreqadhabra.nflight.application.server.concurrent.executors.RejectedExecutionHandelerImpl;
import com.abreqadhabra.nflight.application.server.concurrent.executors.ServiceExecutorForkJoin;
import com.abreqadhabra.nflight.application.server.concurrent.runnable.MultiRunnable;
import com.abreqadhabra.nflight.application.server.service.IService;
import com.abreqadhabra.nflight.application.server.transport.Connector;
import com.abreqadhabra.nflight.application.server.transport.socket.DatagramConnectorImpl;
import com.abreqadhabra.nflight.application.server.transport.socket.runnable.DatagramAcceptor;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

//Strategy Context
public class ServerImpl implements IServer {
	private static final Class<ServerImpl> THIS_CLAZZ = ServerImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	private Thread serviceThread;
	private String address;
	private int port;
	IService service;
	private ThreadPoolExecutor executor;

	public ServerImpl(final Profile profile) {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				LoggingHelper.describe(THIS_CLAZZ));

		this.init();
	}

	public ServerImpl(final String address, final int port) throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		this.address = address;
		this.port = port;

	}

	@Override
	public Thread getService() {
		return serviceThread;
	}

	private void init() {

		// ServiceExecutorThread threadExcutor = new ServiceExecutorThread();
		// threadExcutor.execute();

		final ServiceExecutorForkJoin forkJoinExcutor = new ServiceExecutorForkJoin();
		forkJoinExcutor.execute();
	}

	@Override
	public void shutdown() throws Exception {
		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		DatagramConnectorImpl connector = new DatagramConnectorImpl();
		DatagramChannel datagramChannel = connector.connect();
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"isConnected : " + datagramChannel.isConnected());

		datagramChannel.close();

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				"isOpen : " + datagramChannel.isOpen());
	}

	@Override
	public void startup() throws Exception {

		final String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		// new StreamAcceptor(address, port);

		final BlockingQueue<Runnable> worksQueue = new ArrayBlockingQueue<Runnable>(
				2);
		final RejectedExecutionHandler executionHandler = new RejectedExecutionHandelerImpl();
		executor = new ThreadPoolExecutor(12, 24, 10, TimeUnit.SECONDS,
				worksQueue, executionHandler);
		executor.allowCoreThreadTimeOut(true);

		final List<Runnable> taskGroup = new ArrayList<Runnable>();

		service = (IService) new DatagramAcceptor(address, port);

		taskGroup.add(0, (Runnable) service);

		executor.execute(new MultiRunnable(taskGroup));
		serviceThread = new Thread((Runnable) service);
		serviceThread.setName(service.getClass().getSimpleName());
		serviceThread.start();
		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				serviceThread.getClass().getName());

	}

	@Override
	public boolean status() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
