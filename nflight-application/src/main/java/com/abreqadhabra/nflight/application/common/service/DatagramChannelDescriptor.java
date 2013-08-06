package com.abreqadhabra.nflight.application.common.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

public class DatagramChannelDescriptor extends NetworkChannelDescriptor {

	private static final long serialVersionUID = 1L;

	boolean isConnected;

	private InetSocketAddress remoteAddress;

	public DatagramChannelDescriptor(Long sessionId, DatagramChannel channel)
			throws IOException {
		super(sessionId, channel);
		remoteAddress = (InetSocketAddress) channel.getRemoteAddress();

	}

	public boolean isConnected() {
		return ((DatagramChannel) this.getChannel()).isConnected();
	}

	public InetSocketAddress getRemoteAddress() {
		return this.remoteAddress;
	}

	public String getRemoteHostName() {
		return this.remoteAddress == null ? null : this.remoteAddress
				.getHostString();
	}

	public String getRemoteHostAddress() {
		return this.remoteAddress == null ? null : this.remoteAddress
				.getAddress().getHostAddress();
	}

	public int getRemotePort() {
		return this.remoteAddress == null ? 0 : this.remoteAddress.getPort();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(super.toString());
		builder.append("\n");
		builder.append("isConnected()=");
		builder.append(this.isConnected());
		builder.append("\n");
		if (this.getRemoteAddress() != null) {
			builder.append("getRemoteAddress()=");
			builder.append(this.getRemoteAddress());
			builder.append("\n");
		}
		if (this.getRemoteHostName() != null) {
			builder.append("getRemoteHostName()=");
			builder.append(this.getRemoteHostName());
			builder.append("\n");
		}
		if (this.getRemoteHostAddress() != null) {
			builder.append("getRemoteHostAddress()=");
			builder.append(this.getRemoteHostAddress());
			builder.append("\n");
		}
		if (this.getRemotePort() != 0) {
			builder.append("getRemotePort()=");
			builder.append(this.getRemotePort());
		}
		return builder.toString();
	}

}
