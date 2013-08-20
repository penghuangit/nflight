package com.abreqadhabra.nflight.application.common.launcher.concurrent.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.Config;
import com.abreqadhabra.nflight.application.common.launcher.concurrent.executor.monitor.ThreadPoolMonitorServiceImpl;
import com.abreqadhabra.nflight.application.common.launcher.conf.LauncherConfiguration;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ThreadPoolImpl implements ThreadPool {
	private static Class<ThreadPoolImpl> THIS_CLAZZ = ThreadPoolImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private int corePoolSize;
	private int maximumPoolSize;
	private long keepAliveTime;
	private TimeUnit timeUnit;
	private int queueCapacity;
	private ArrayBlockingQueue<Runnable> arrayBlockingQueue;
	private ThreadFactory threadFactory;
	private RejectedExecutionHandler rejectedExecutionHandler;

	public ThreadPoolImpl() throws NFlightException {
		Config.load(THIS_CLAZZ,
				LauncherConfiguration.FILE_THREAD_POOL_PROPERTIES);
		this.corePoolSize = Integer
				.parseInt(Config
						.get(LauncherConfiguration.LAUNCHER_THREAD_POOL_CORE_POOL_SIZE));
		this.maximumPoolSize = Integer
				.parseInt(Config
						.get(LauncherConfiguration.LAUNCHER_THREAD_POOL_MAXIMUM_POOL_SIZE));
		this.keepAliveTime = Integer
				.parseInt(Config
						.get(LauncherConfiguration.LAUNCHER_THREAD_POOL_KEEP_ALIVE_TIME));
		this.queueCapacity = Integer
				.parseInt(Config
						.get(LauncherConfiguration.LAUNCHER_THREAD_POOL_QUEUE_CAPACITY));
		this.timeUnit = TimeUnit.SECONDS;
		this.arrayBlockingQueue = new ArrayBlockingQueue<Runnable>(
				this.getQueueCapacity());
		this.threadFactory = Executors.defaultThreadFactory();
		this.rejectedExecutionHandler = new RejectedExecutionHandlerImpl();
	}

	@Override
	public ThreadPoolExecutor getThreadPoolExecutor(String poolName,
			boolean isThreadPoolMonitoring, int MonitoringDelaySeconds)
			throws NFlightException {
		ThreadPoolExecutor threadPoolExecutor = this.createNewThreadPool();
		threadPoolExecutor.allowCoreThreadTimeOut(true);

		if (isThreadPoolMonitoring) {
			Thread t = Thread.currentThread();
			ThreadGroup threadGroup = t.getThreadGroup();
			// Created executor is set to ThreadPoolMonitorService...
			ThreadPoolMonitorServiceImpl tpms = new ThreadPoolMonitorServiceImpl(
					MonitoringDelaySeconds, threadGroup, poolName);
			tpms.setExecutor(threadPoolExecutor);
			// ThreadPoolMonitorService is started...
			Thread monitor = new Thread(tpms);
			monitor.start();
		}
		return threadPoolExecutor;
	}

	@Override
	public ThreadPoolExecutor createNewThreadPool() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		ThreadPoolExecutor executor = new ThreadPoolExecutor(
				this.getCorePoolSize(), this.getMaximumPoolSize(),
				this.getKeepAliveTime(), this.getTimeUnit(),
				this.getArrayBlockingQueue(), this.threadFactory,
				this.getRejectedExecutionHandler());

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				this.toString());

		return executor;
	}

	@Override
	public int getCorePoolSize() {
		return this.corePoolSize;
	}

	@Override
	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	@Override
	public int getMaximumPoolSize() {
		return this.maximumPoolSize;
	}

	@Override
	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	@Override
	public long getKeepAliveTime() {
		return this.keepAliveTime;
	}

	@Override
	public void setKeepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	@Override
	public TimeUnit getTimeUnit() {
		return this.timeUnit;
	}

	@Override
	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	@Override
	public int getQueueCapacity() {
		return this.queueCapacity;
	}

	@Override
	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}

	@Override
	public ArrayBlockingQueue<Runnable> getArrayBlockingQueue() {
		return this.arrayBlockingQueue;
	}

	@Override
	public void setArrayBlockingQueue(
			ArrayBlockingQueue<Runnable> arrayBlockingQueue) {
		this.arrayBlockingQueue = arrayBlockingQueue;
	}

	@Override
	public RejectedExecutionHandler getRejectedExecutionHandler() {
		return this.rejectedExecutionHandler;
	}

	@Override
	public void setRejectedExecutionHandler(
			RejectedExecutionHandler rejectedExecutionHandler) {
		this.rejectedExecutionHandler = rejectedExecutionHandler;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ThreadPoolExecutorServiceImpl [corePoolSize=")
				.append(this.corePoolSize).append(", maximumPoolSize=")
				.append(this.maximumPoolSize).append(", keepAliveTime=")
				.append(this.keepAliveTime).append(", timeUnit=")
				.append(this.timeUnit).append(", queueCapacity=")
				.append(this.queueCapacity)
				.append(", rejectedExecutionHandler=")
				.append(this.rejectedExecutionHandler)
				.append(", arrayBlockingQueue=")
				.append(this.arrayBlockingQueue).append("]");
		return builder.toString();
	}

}
