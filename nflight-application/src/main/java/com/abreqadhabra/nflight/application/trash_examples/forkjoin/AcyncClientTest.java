package com.abreqadhabra.nflight.application.trash_examples.forkjoin;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author Apress
 */
public class AcyncClientTest {

    public static void main(String[] args) throws UnknownHostException {

        int DEFAULT_PORT = 9999;
        String IP = InetAddress.getLocalHost().getHostAddress();

        //create asynchronous socket channel bound to the default group
        try (AsynchronousSocketChannel asynchronousSocketChannel = AsynchronousSocketChannel.open()) {

            if (asynchronousSocketChannel.isOpen()) {

                //set some options
                asynchronousSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
                asynchronousSocketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
                asynchronousSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

                //connect this channel's socket
                asynchronousSocketChannel.connect(new InetSocketAddress(IP, DEFAULT_PORT), null, new CompletionHandler<Void, Void>() {

                    ByteBuffer helloBuffer = ByteBuffer.wrap("Hello !".getBytes());
                    ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                    CharBuffer charBuffer = null;
                    ByteBuffer randomBuffer;
                    Charset charset = Charset.defaultCharset();
                    CharsetDecoder decoder = charset.newDecoder();

                    @Override
                    public void completed(Void result, Void attachment) {
                        try {
                            System.out.println("Successfully connected at: " + asynchronousSocketChannel.getRemoteAddress());

                            //transmitting data
                            asynchronousSocketChannel.write(helloBuffer).get();

                            while (asynchronousSocketChannel.read(buffer).get() != -1) {

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
                                    asynchronousSocketChannel.write(randomBuffer).get();
                                }
                            }
                        } catch (IOException | InterruptedException | ExecutionException ex) {
                            System.err.println(ex);
                        } finally {
                            try {
                                asynchronousSocketChannel.close();
                            } catch (IOException ex) {
                                System.err.println(ex);
                            }
                        }
                    }

                    @Override
                    public void failed(Throwable exc, Void attachment) {
                        throw new UnsupportedOperationException("Connection cannot be established!");
                    }
                });

                System.in.read();

            } else {
                System.out.println("The asynchronous socket channel cannot be opened!");
            }

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
