package com.abreqadhabra.nflight.application.server.service.socket.test.sample.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class RspHandler {
	private byte[] rsp = null;

	public synchronized boolean handleResponse(byte[] rsp) {
		this.rsp = rsp;
		this.notify();
		return true;
	}

	public synchronized void waitForResponse() {
		while (this.rsp == null) {
			try {
				this.wait();
			} catch (InterruptedException e) {
			}
		}

	
		
		Object object = deserializeObject(this.rsp);
		if (object instanceof Object) {
			System.out.println(object.getClass().getName());
		} else {
			System.out.println(new String(this.rsp));
		}
	}
	
	
	public ByteBuffer serializeObject(Object object) {
		byte[] bytes = null;
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(
						byteArrayOutputStream);) {
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
			bytes = byteArrayOutputStream.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return ByteBuffer.wrap(bytes);
	}
	
	public Object deserializeObject(final byte[] bytes) {
		Object readObject = null;
		try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				ObjectInputStream ois = new ObjectInputStream(bais);) {
			readObject = ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		return readObject;
	}
}
