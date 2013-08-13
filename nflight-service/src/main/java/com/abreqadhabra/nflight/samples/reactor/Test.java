package com.abreqadhabra.nflight.samples.reactor;

import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException {
		Reactor reactor  = new Reactor(9900, false);
	    new Thread(reactor).start();
	}

}
