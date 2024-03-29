package com.abreqadhabra.nflight.application.common.launcher.concurrent.executor.monitor;

import java.util.concurrent.ThreadPoolExecutor;

public interface ThreadPoolMonitorService extends Runnable {

	void monitorThreadPool();

	public ThreadPoolExecutor getExecutor();

	public void setExecutor(ThreadPoolExecutor executor);


}