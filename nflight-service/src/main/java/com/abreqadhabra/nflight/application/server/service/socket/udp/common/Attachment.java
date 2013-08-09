package com.abreqadhabra.nflight.application.server.service.socket.udp.common;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

public class Attachment {

	private int bufferCapacity = 1024*1024;
	private ByteBuffer requestByteBuffer;
	private ByteBuffer responseByteBuffer;
	private SocketAddress socketAddress;

	public Attachment() {
		requestByteBuffer = ByteBuffer.allocate(bufferCapacity);
	}

	public ByteBuffer getRequestByteBuffer() {
		return requestByteBuffer;
	}

	public void setRequestByteBuffer(ByteBuffer requestByteBuffer) {
		this.requestByteBuffer = requestByteBuffer;
	}

	public ByteBuffer getResponseByteBuffer() {
		return responseByteBuffer;
	}

	public void setResponseByteBuffer(ByteBuffer responseByteBuffer) {
		this.responseByteBuffer = responseByteBuffer;
	}

	public SocketAddress getSocketAddress() {
		return socketAddress;
	}

	public void setSocketAddress(SocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}

}