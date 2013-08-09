package com.abreqadhabra.nflight.application.server.service.socket.tcp.impl;

import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

import com.abreqadhabra.nflight.application.server.service.socket.SocketAcceptor;

public class AcceptorWorker implements Runnable {
	private List<DataEvent> queue = new LinkedList<DataEvent>();

	public void processData(SocketAcceptor acceptor, SocketChannel socket,
			byte[] data, int count) {
		byte[] dataCopy = new byte[count];
		System.arraycopy(data, 0, dataCopy, 0, count);
		synchronized (this.queue) {
			this.queue.add(new DataEvent(acceptor, socket, dataCopy));
			this.queue.notify();
		}
	}
	
	public void processData(SocketAcceptor acceptor, DatagramChannel datagram,
			byte[] data, int count) {
		byte[] dataCopy = new byte[count];
		System.arraycopy(data, 0, dataCopy, 0, count);
		synchronized (this.queue) {
			this.queue.add(new DataEvent(acceptor, datagram, dataCopy));
			this.queue.notify();
		}
	}
	

	@Override
	public void run() {
		DataEvent dataEvent;

		while (true) {
			// Wait for data to become available
			synchronized (this.queue) {
				while (this.queue.isEmpty()) {
					try {
						this.queue.wait();
					} catch (InterruptedException e) {
					}
				}
				dataEvent = (DataEvent) this.queue.remove(0);
			}

			
			dataEvent.acceptor.execute(dataEvent);
			
			// Return to sender
			//dataEvent.server.send(dataEvent.socket, dataEvent.data);
		}
	}
}
