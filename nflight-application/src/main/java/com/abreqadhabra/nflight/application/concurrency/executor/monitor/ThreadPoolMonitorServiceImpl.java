package com.abreqadhabra.nflight.application.concurrency.executor.monitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ThreadPoolMonitorServiceImpl implements ThreadPoolMonitorService {
	private static Class<ThreadPoolMonitorServiceImpl> THIS_CLAZZ = ThreadPoolMonitorServiceImpl.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	ThreadPoolExecutor executor;

	private int delaySeconds;
	private ThreadGroup threadGroup;
	private String poolName;
	public ThreadPoolMonitorServiceImpl(int delay, ThreadGroup threadGroup,
			String poolName) {
		this.delaySeconds = delay;
		this.poolName = poolName;
		this.threadGroup = threadGroup;
	}

	@Override
	public void run() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		if (LOGGER.isLoggable(Level.FINER)) {
			String currentThreadName = Thread.currentThread().getName();
			Thread.currentThread().setName(currentThreadName);
			LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					"current thread is " + Thread.currentThread().getName());
		}

		try {
			while (true) {
				this.monitorThreadPool();
				Thread.sleep(this.delaySeconds * 1000);
			}
		} catch (Exception e) {
			LOGGER.logp(Level.SEVERE, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
					e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void monitorThreadPool() {

		this.threadInfo();
		this.threadPoolInfo();

	}

	private void threadPoolInfo() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		String result = String
				.format("[%s] [%d/%d] TotalTaskCount: %d [Active: %d, Completed: %d] isShutdown: %s , isTerminated: %s",
						this.poolName, this.executor.getPoolSize(),
						this.executor.getCorePoolSize(),
						this.executor.getTaskCount(),
						this.executor.getActiveCount(),
						this.executor.getCompletedTaskCount(),
						this.executor.isShutdown(),
						this.executor.isTerminated());

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				result);
	}

	public void threadInfo() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

	//	this.threadGroup = this.threadGroup.getParent();
		StringBuffer sb = new StringBuffer("\n");

		String threadInfo = this.threadInfo(this.threadGroup, "", sb);

		LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,
				threadInfo);

		System.out.println("---------------------");
		this.threadGroup.list();
	}

	public String threadInfo(ThreadGroup threadGroup, String threadGroupName,
			StringBuffer sb) {
		Thread.currentThread().getStackTrace()[1].getMethodName();

		sb.append(threadGroupName + "ThreadGroup name = "
				+ threadGroup.getName() + "\n");
		sb.append(threadGroupName + "   Has parent thread group = "
				+ (threadGroup.getParent() != null) + "\n");

		int activeCount = threadGroup.activeCount();
		Thread[] activeThreads = new Thread[activeCount];
		threadGroup.enumerate(activeThreads, false);
		List<Thread> threadList = new ArrayList<Thread>(
				Arrays.asList(activeThreads));
		threadList.removeAll(Collections.singleton(null));
		sb.append(threadGroupName + "   # of active threads = "
				+ threadList.size() + "\n");
		for (Thread thread : threadList) {
			sb.append(threadGroupName + "   Thread name = " + thread.getName()
					+ "\n");
		}

		int activeGroupCount = threadGroup.activeGroupCount();
		ThreadGroup[] activeThreadGroups = new ThreadGroup[activeGroupCount];
		threadGroup.enumerate(activeThreadGroups, false);
		List<ThreadGroup> threadGroupList = new ArrayList<ThreadGroup>(
				Arrays.asList(activeThreadGroups));
		threadGroupList.removeAll(Collections.singleton(null));
		sb.append(threadGroupName + "   # of active sub thread groups = "
				+ threadGroupList.size() + "\n");
		for (ThreadGroup activeThreadGroup : threadGroupList) {
			this.threadInfo(activeThreadGroup, "   " /*
													 * +
													 * activeThreadGroup.getName
													 * ()
													 */, sb);
		}

		return sb.toString();
	}

	@Override
	public ThreadPoolExecutor getExecutor() {
		return this.executor;
	}

	@Override
	public void setExecutor(ThreadPoolExecutor executor) {
		this.executor = executor;
	}

	public long getDelaySeconds() {
		return this.delaySeconds;
	}

	public void setDelaySeconds(int delayseconds) {
		this.delaySeconds = delayseconds;
	}
}