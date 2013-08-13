package com.abreqadhabra.nflight.application.launcher.concurrent.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.launcher.Configure;
import com.abreqadhabra.nflight.application.launcher.ConfigureImpl;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ThreadPoolExecutorServiceImpl implements ThreadPoolExecutorService {
	private static Class<ThreadPoolExecutorServiceImpl> THIS_CLAZZ = ThreadPoolExecutorServiceImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	private int corePoolSize;
	private int maximumPoolSize;
	private long keepAliveTime;
	private TimeUnit timeUnit;
	private int queueCapacity;
	private ArrayBlockingQueue<Runnable> arrayBlockingQueue;
	private ThreadFactory threadFactory ;
	private RejectedExecutionHandler rejectedExecutionHandler;
	
	public ThreadPoolExecutorServiceImpl() {
		Configure configure = new ConfigureImpl(
				Configure.FILE_THREAD_POOL_PROPERTIES);
		this.corePoolSize = Integer.parseInt(configure
				.get("nflight.threadpool.corepoolsize"));
		this.maximumPoolSize = Integer.parseInt(configure
				.get("nflight.threadpool.maximumpoolsize"));
		this.keepAliveTime = Integer.parseInt(configure
				.get("nflight.threadpool.keepalivetime"));
		this.queueCapacity = Integer.parseInt(configure
				.get("nflight.threadpool.queuecapacity"));
		this.timeUnit = TimeUnit.SECONDS;
		this.arrayBlockingQueue = new ArrayBlockingQueue<Runnable>(
				this.getQueueCapacity());
		this.threadFactory = Executors.defaultThreadFactory();
		this.rejectedExecutionHandler = new RejectedExecutionHandlerImpl();
	}

	@Override
	public ThreadPoolExecutor createNewThreadPool() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
	        
		ThreadPoolExecutor executor = new ThreadPoolExecutor(
				this.getCorePoolSize(), this.getMaximumPoolSize(),
				this.getKeepAliveTime(), getTimeUnit(),
				this.getArrayBlockingQueue(),
				this.threadFactory,
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
