package com.abreqadhabra.nflight.application.service.network.socket;

import java.nio.channels.NetworkChannel;
import java.nio.channels.SocketChannel;

import com.abreqadhabra.nflight.application.service.Service;
import com.abreqadhabra.nflight.common.exception.NFlightException;

/**
 * The Interface SocketService.
 */
public interface SocketService extends Service{
	
	
	/**
	 * Bind.
	 * 
	 * @throws NFlightException
	 *             the n flight exception
	 */
	public void bind() throws NFlightException;
	

	
	/**
	 * Accept.
	 * 
	 * @param socketChannel
	 *            the socket channel
	 * @throws NFlightException
	 *             the n flight exception
	 */
	public void accept(NetworkChannel socketChannel) throws NFlightException;
	
	/**
	 * Send.
	 * 
	 * @param socket
	 *            the socket
	 */
	public void send(SocketChannel socket);
	
	/**
	 * Send.
	 * 
	 * @param socket
	 *            the socket
	 * @param message
	 *            the message
	 * @throws NFlightException
	 *             the n flight exception
	 */
	public void send(SocketChannel socket, Object message)
			throws NFlightException;
	
	/**
	 * Receive.
	 * 
	 * @param socketChannel
	 *            the socket channel
	 * @throws NFlightException
	 *             the n flight exception
	 */
	public void receive(NetworkChannel socketChannel) throws NFlightException;
}
