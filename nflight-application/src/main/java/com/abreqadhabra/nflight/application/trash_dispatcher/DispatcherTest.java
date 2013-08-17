package com.abreqadhabra.nflight.application.trash_dispatcher;


public class DispatcherTest {

	public static void main(String[] args) {
		Dispatcher dispatcher = new Dispatcher();
		try {
			ServiceDescriptor sd = new SocketServiceDescriptor("stream", "localhost", 9999);
			
			dispatcher.register(sd.getName(), sd);
			
			sd = dispatcher.locate("stream");
			
			System.out.println(sd);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
