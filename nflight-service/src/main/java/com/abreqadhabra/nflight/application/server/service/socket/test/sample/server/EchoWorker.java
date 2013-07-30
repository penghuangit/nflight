package com.abreqadhabra.nflight.application.server.service.socket.test.sample.server;

import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

public class EchoWorker implements Runnable {
	private List<ServerDataEvent> queue = new LinkedList<ServerDataEvent>();

	public void processData(SocketServer server, SocketChannel socket,
			byte[] data, int count) {
		byte[] dataCopy = new byte[count];
		System.arraycopy(data, 0, dataCopy, 0, count);
		synchronized (this.queue) {
			this.queue.add(new ServerDataEvent(server, socket, dataCopy));
			this.queue.notify();
		}
	}

	@Override
	public void run() {
		ServerDataEvent dataEvent;

		while (true) {
			// Wait for data to become available
			synchronized (this.queue) {
				while (this.queue.isEmpty()) {
					try {
						this.queue.wait();
					} catch (InterruptedException e) {
					}
				}
				dataEvent = (ServerDataEvent) this.queue.remove(0);
			}

			// Return to sender
			dataEvent.server.send(dataEvent.socket, dataEvent.data);
		}
	}
}
