package com.abreqadhabra.nflight.application.server.service.socket.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 
 * @author Apress
 */
public class TestClient {

    public static void main(String[] args) {
        SocketChannel sc = null;
        try {
            sc = SocketChannel.open();
            sc.configureBlocking(false);
            // make sure to call sc.connect() or else 
            // calling sc.finishConnect() will throw 
            // java.nio.channels.NoConnectionPendingException
            sc.connect(new InetSocketAddress(5555));
            // if the socket has connected, sc.finishConnect() should 
            // return false
            while (!sc.finishConnect()) {
                // pretend to do something useful here
                System.out.println("Doing something useful...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Sending a request to HelloServer");

			write(sc, new Message("a"));
   
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sc != null) {
                try {
                    sc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



	

	// isWritable returned true
	public static void write(SocketChannel channel, Message message)
			throws IOException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);
		objectOutputStream.writeObject(message);
		objectOutputStream.flush();
		channel.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
		
	}

}
