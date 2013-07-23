package com.abreqadhabra.nflight.app.core.rmi;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SecureSocketFactory implements RMIClientSocketFactory,
		RMIServerSocketFactory, Serializable {

	private static final long serialVersionUID = 1L;

	transient SSLSocketFactory clientSocketFactory;
	transient SSLServerSocketFactory serverSocketFactory;

	public SecureSocketFactory() {
	}

	/**
	 * Creates the server socket, which will be used to instantiate a
	 * <code>UnicastRemoteObject</code>.
	 * 
	 * @param port
	 *            The port to listen on.
	 * @return The server socket.
	 */
	@Override
	public ServerSocket createServerSocket(final int port) throws IOException {
		if (this.serverSocketFactory == null) {
			try {
				SSLContext ctx = null;
				ctx = SSLContext.getInstance("TLS");
				ctx.init(null, null, null);

				this.serverSocketFactory = ctx.getServerSocketFactory();
			} catch (final Exception e) {
				throw new IOException("Error creating SSLServerSocketFactory. "
						+ e.toString());
			}
		}

		final SSLServerSocket sss = (SSLServerSocket) this.serverSocketFactory
				.createServerSocket(port);
		sss.setEnabledCipherSuites(new String[] { "SSL_DH_anon_WITH_RC4_128_MD5" });
		return sss;
	}

	/**
	 * Creates the client socket, which will be used to instantiate a
	 * <code>UnicastRemoteObject</code>.
	 * 
	 * @param host
	 *            The host to connect to.
	 * @param port
	 *            The port to connect to.
	 * @return The client socket.
	 */
	@Override
	public Socket createSocket(final String host, final int port)
			throws IOException {
		if (this.clientSocketFactory == null) {
			try {
				final SSLContext ctx = SSLContext.getInstance("TLS");
				ctx.init(null, null, null);
				this.clientSocketFactory = ctx.getSocketFactory();
			} catch (final Exception e) {
				throw new IOException("Error creating SSLSocketFactory. "
						+ e.toString());
			}
		}
		final SSLSocket sc = (SSLSocket) this.clientSocketFactory.createSocket(
				host, port);
		sc.setEnabledCipherSuites(new String[] { "SSL_DH_anon_WITH_RC4_128_MD5" });
		return sc;
	}

}
