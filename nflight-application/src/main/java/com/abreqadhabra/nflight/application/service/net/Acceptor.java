package com.abreqadhabra.nflight.application.service.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

public interface Acceptor {
	void init(InetSocketAddress endpoint) throws IOException,
			InterruptedException, ExecutionException;
	ServerChannelFactory createServerChannelFactory();

}
