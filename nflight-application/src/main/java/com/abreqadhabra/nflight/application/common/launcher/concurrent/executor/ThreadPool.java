package com.abreqadhabra.nflight.application.common.launcher.concurrent.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.abreqadhabra.nflight.common.exception.NFlightException;

public interface ThreadPool {

	public ThreadPoolExecutor getThreadPoolExecutor(String poolName,
			boolean isThreadPoolMonitoring, int MonitoringDelaySeconds)
			throws NFlightException;
			
	public ThreadPoolExecutor createNewThreadPool();

	public int getCorePoolSize();

	public void setCorePoolSize(int corePoolSize);

	public int getMaximumPoolSize();

	public void setMaximumPoolSize(int maximumPoolSize);

	public long getKeepAliveTime();

	public void setKeepAliveTime(long keepAliveTime);

	public TimeUnit getTimeUnit();

	public void setTimeUnit(TimeUnit timeUnit);
	
	public int getQueueCapacity();

	public void setQueueCapacity(int queueCapacity);

	public ArrayBlockingQueue<Runnable> getArrayBlockingQueue();

	public void setArrayBlockingQueue(
			ArrayBlockingQueue<Runnable> arrayBlockingQueue);

	public RejectedExecutionHandler getRejectedExecutionHandler();

	public void setRejectedExecutionHandler(
			RejectedExecutionHandler rejectedExecutionHandler);



}