package com.abreqadhabra.nflight.application.server.transport.socket.runnable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.server.transport.Connector;
import com.abreqadhabra.nflight.application.server.transport.socket.DatagramConnectorImpl;
import com.abreqadhabra.nflight.application.server.transport.socket.runnable.ASyncUDPSvr.Con;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class DatagramAcceptor{
	private static Class<DatagramAcceptor> THIS_CLAZZ = DatagramAcceptor.class;
	private Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

    static int BUF_SZ = 1024;

    class Con {
        ByteBuffer req;
        ByteBuffer resp;
        SocketAddress sa;

        public Con() {
            req = ByteBuffer.allocate(BUF_SZ);
        }
    }
    
	Connector connector;
	DatagramChannel datagramChannel;
	int MAX_PACKET_SIZE = 65507;

	public DatagramAcceptor()
			throws IOException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		Connector connector = new DatagramConnectorImpl();
		
		datagramChannel = connector.bind();

	}
	
	public DatagramAcceptor(String address, int port)
			throws IOException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		

	}


    
    private void startup() {
        try {

            Selector selector = Selector.open();
            
            if(selector.isOpen()) {
            datagramChannel.configureBlocking(false);
            SelectionKey clientKey = datagramChannel.register(selector, SelectionKey.OP_READ);
            
            while (true) {
                try {
                    selector.select();
                    Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                    while (selectedKeys.hasNext()) {
                        try {
                            SelectionKey key = (SelectionKey) selectedKeys.next();
                            selectedKeys.remove();

                            if (!key.isValid()) {
                              continue;
                            }

                            if (key.isReadable()) {
                                read(key);
                                key.interestOps(SelectionKey.OP_WRITE);
                            } else if (key.isWritable()) {
                                write(key);
                                key.interestOps(SelectionKey.OP_READ);
                            }
                        } catch (IOException e) {
                            System.err.println("glitch, continuing... " +(e.getMessage()!=null?e.getMessage():""));
                        }
                    }
                } catch (IOException e) {
                    System.err.println("glitch, continuing... " +(e.getMessage()!=null?e.getMessage():""));
                }
            }
            
            }else {
            	
            }
            
            
            
        } catch (IOException e) {
            System.err.println("network error: " + (e.getMessage()!=null?e.getMessage():""));
        }
    }
    
    private void read(SelectionKey key) throws IOException {
        DatagramChannel chan = (DatagramChannel)key.channel();
        Con con = (Con)key.attachment();
        con.sa = chan.receive(con.req);
        System.out.println(new String(con.req.array(), "UTF-8"));
        con.resp = Charset.forName( "UTF-8" ).newEncoder().encode(CharBuffer.wrap("send the same string"));
    }
    
    private void write(SelectionKey key) throws IOException {
        DatagramChannel chan = (DatagramChannel)key.channel();
        Con con = (Con)key.attachment();
        chan.send(con.resp, con.sa);
    }
    
	private void shutdown() {
		// TODO Auto-generated method stub
		
	}
    
    static public void main(String[] args) throws IOException {
    	DatagramAcceptor svr = new DatagramAcceptor();
        svr.startup();
        svr.shutdown();
    }


}