package com.abreqadhabra.nflight.application.server.concurrent.runnable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class StreamAcceptorClientHandler implements
		CompletionHandler<Void, Void> {


    final ByteBuffer helloBuffer = ByteBuffer.wrap("Hello !".getBytes());
    final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
    CharBuffer charBuffer = null;
    ByteBuffer randomBuffer;
    final Charset charset = Charset.defaultCharset();
    final CharsetDecoder decoder = charset.newDecoder();
	private AsynchronousSocketChannel asyncSocketChannel;


    
    public StreamAcceptorClientHandler(
			AsynchronousSocketChannel asyncSocketChannel) {
    	this.asyncSocketChannel = asyncSocketChannel;
	}

	@Override
    public void completed(Void result, Void attachment) {
        try {
            System.out.println("Successfully connected at: " + asyncSocketChannel.getRemoteAddress());

            //transmitting data
            asyncSocketChannel.write(helloBuffer).get();

            while (asyncSocketChannel.read(buffer).get() != -1) {

                buffer.flip();

                charBuffer = decoder.decode(buffer);
                System.out.println(charBuffer.toString());

                if (buffer.hasRemaining()) {
                    buffer.compact();
                } else {
                    buffer.clear();
                }

                int r = new Random().nextInt(100);
                if (r == 50) {
                    System.out.println("50 was generated! Close the asynchronous socket channel!");
                    break;
                } else {
                    randomBuffer = ByteBuffer.wrap("Random number:".concat(String.valueOf(r)).getBytes());
                    asyncSocketChannel.write(randomBuffer).get();
                }
            }
        } catch (IOException | InterruptedException | ExecutionException ex) {
            System.err.println(ex);
        } finally {
            try {
                asyncSocketChannel.close();
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        throw new UnsupportedOperationException("Connection cannot be established!");
    }
}
