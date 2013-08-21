package com.abreqadhabra.nflight.application.service.thread;

public class ServiceThread extends Thread {

	public ServiceThread(Runnable target) {
		super(target);
	}

	public ServiceThread(ThreadGroup group, Runnable target, String name) {
		super(group, target, name);
	}
}
