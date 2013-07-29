package com.abreqadhabra.nflight.application.server.service.socket;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface Acceptor {

	void read(SelectionKey key) throws IOException;

	void write(SelectionKey key) throws IOException;

}
