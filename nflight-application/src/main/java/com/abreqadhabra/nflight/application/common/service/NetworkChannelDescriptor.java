package com.abreqadhabra.nflight.application.common.service;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.channels.NetworkChannel;
import java.util.Set;

public class NetworkChannelDescriptor implements Serializable {

	private static long serialVersionUID = 1L;

	private Long sessionId;
	private NetworkChannel channel;
	InetSocketAddress endpoint;

	public NetworkChannelDescriptor(Long sessionId, NetworkChannel channel)
			throws IOException {
		this.sessionId = sessionId;
		this.channel = channel;
		this.endpoint = (InetSocketAddress) this.channel.getLocalAddress();

	}

	public Long getSessionId() {
		return this.sessionId;
	}

	public boolean isOpen() {
		return this.channel.isOpen();
	}

	public NetworkChannel getChannel() {
		return this.channel;
	}

	public Set<SocketOption<?>> getSupportedOptions() {
		return this.channel.supportedOptions();
	}

	public InetSocketAddress getLocalAddress() {
		return this.endpoint;
	}

	public String getLocalHostName() {
		return this.endpoint == null ? null : this.endpoint
				.getHostString();
	}

	public String getLocalHostAddress() {
		return this.endpoint == null ? null : this.endpoint
				.getAddress().getHostAddress();
	}

	public int getLocalPort() {
		return this.endpoint == null ? 0 : this.endpoint.getPort();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (this.getSessionId() != null) {
			builder.append("getSessionId()=");
			builder.append(this.getSessionId());
			builder.append("\n");
		}
		builder.append("isOpen()=");
		builder.append(this.isOpen());
		builder.append("\n");
		if (this.getChannel() != null) {
			builder.append("getChannel()=");
			builder.append(this.getChannel());
			builder.append("\n");
		}
		if (this.getSupportedOptions() != null) {
			builder.append("getSupportedOptions()=");
			builder.append(this.getSupportedOptions());
			builder.append("\n");
		}
		if (this.getLocalAddress() != null) {
			builder.append("getLocalAddress()=");
			builder.append(this.getLocalAddress());
			builder.append("\n");
		}
		if (this.getLocalHostName() != null) {
			builder.append("getLocalHostName()=");
			builder.append(this.getLocalHostName());
			builder.append("\n");
		}
		if (this.getLocalHostAddress() != null) {
			builder.append("getLocalHostAddress()=");
			builder.append(this.getLocalHostAddress());
			builder.append("\n");
		}
		if (this.getLocalPort() != 0) {
			builder.append("getLocalPort()=");
			builder.append(this.getLocalPort());
		}
		return builder.toString();
	}
}