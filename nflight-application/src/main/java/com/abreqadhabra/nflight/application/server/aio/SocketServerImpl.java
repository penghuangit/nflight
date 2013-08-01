package com.abreqadhabra.nflight.application.server.aio;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class SocketServerImpl implements SocketServer {

	private ConcurrentHashMap<Long, Session> sessionMap = new ConcurrentHashMap<Long, Session>();
	private AsynchronousServerSocketChannel asynchronousServerSocketChannel;
	private Configure configure;
	private ThreadPoolExecutor threadPoolExecutor;
	private AtomicLong sessionId = new AtomicLong(0);
	private boolean running = false;

	public SocketServerImpl() {
		this.configure = new SocketServerConfiguraImpl();

		RejectedExecutionHandler executionHandler = new RejectedExecutionHandelerImpl();

		this.threadPoolExecutor = new ThreadPoolExecutor(12, 24, 10,
				TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
				executionHandler);
		// this.threadPoolExecutor = new ThreadPoolExecutor(
		// Integer.parseInt(this.configure.get("min_pool_size").trim()),
		// Integer.parseInt(this.configure.get("max_pool_size").trim()),
		// Long.parseLong(this.configure.get("keep_alive_time").trim()),
		// TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
	}
	
	
	@Override
	public void start() throws Exception {
		asynchronousServerSocketChannel = AsynchronousServerSocketChannel
				.open();
		asynchronousServerSocketChannel.bind(new InetSocketAddress(
				9999));//Integer.parseInt(configure.get("port").trim())));
		threadPoolExecutor.submit(new ListenRobot(
				asynchronousServerSocketChannel));
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Session getSession(long sessionId) {
		return sessionMap.get(sessionId);
	}

	class ListenRobot implements Runnable {
		private AsynchronousServerSocketChannel asynchronousServerSocketChannel;
		public ListenRobot(
				AsynchronousServerSocketChannel asynchronousServerSocketChannel) {
			this.asynchronousServerSocketChannel = asynchronousServerSocketChannel;
		}
		public void run() {
			while (!running) {
				Future<AsynchronousSocketChannel> future = asynchronousServerSocketChannel
						.accept();
				try {
					AsynchronousSocketChannel channel = future.get();
					Session session = new SocketServerSession(
							sessionId.incrementAndGet(), channel);
					sessionMap.put(session.getId(), session);
					session.init(configure);
					session.open();
					session.read();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
