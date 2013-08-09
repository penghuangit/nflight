package com.abreqadhabra.nflight.application.server.service.socket.tcp.common;

import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

public class ChangeRequest {
	public static final String REGISTER = "REGISTER";
	public static final String CHANGE_OPS = "CHANGE_OPS";

	public SocketChannel socketChannel;
	public DatagramChannel datagramChannel;

	public String type;
	public int ops;

	static HashMap<Integer, String> opsNameMap = new HashMap<Integer, String>();

	static {
		opsNameMap.put(SelectionKey.OP_ACCEPT, "OP_ACCEPT");
		opsNameMap.put(SelectionKey.OP_ACCEPT, "OP_ACCEPT");
		opsNameMap.put(SelectionKey.OP_CONNECT, "OP_CONNECT");
		opsNameMap.put(SelectionKey.OP_READ, "OP_READ");
		opsNameMap.put(SelectionKey.OP_WRITE, "OP_WRITE");
	}

	public ChangeRequest(SocketChannel socketChannel, String type, int ops) {
		this.socketChannel = socketChannel;
		this.type = type;
		this.ops = ops;
	}

	public ChangeRequest(DatagramChannel datagramChannel, String type, int ops) {
		this.datagramChannel = datagramChannel;
		this.type = type;
		this.ops = ops;
	}

	public static String getOpsName(int ops) {
		return opsNameMap.get(ops);
	}

	@Override
	public String toString() {
		return "ChangeRequest [socketChannel=" + this.socketChannel
				+ ", datagramChannel=" + this.datagramChannel + ", type="
				+ this.type + ", ops=[" + getOpsName(this.ops) + "=" + this.ops
				+ "]]";
	}

}
