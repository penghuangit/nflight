package com.abreqadhabra.nflight.application.server.service.socket.test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;




public class UDPDiscardServerWithChannels {


  public final static int DEFAULT_PORT = 5555;

  public final static int MAX_PACKET_SIZE = 65507;


  public static void main(String[] args) {


    int port = DEFAULT_PORT;

    try {

      port = Integer.parseInt(args[0]);

    }

    catch (Exception ex) {

    }

    

    try {

      DatagramChannel channel = DatagramChannel.open( );

      DatagramSocket socket = channel.socket( );

      SocketAddress address = new InetSocketAddress(port);

      socket.bind(address);

      ByteBuffer buffer = ByteBuffer.allocateDirect(MAX_PACKET_SIZE);

      while (true) {

        SocketAddress client = channel.receive(buffer);

        buffer.flip( );

        System.out.print(client + " says ");

        while (buffer.hasRemaining( )) System.out.write(buffer.get( ));

        System.out.println( );

        buffer.clear( );

      } // end while

    }  // end try

    catch (IOException ex) {

      System.err.println(ex);

    }  // end catch


  }  // end main


}
