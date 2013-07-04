package com.abreqadhabra.nflight.server.core;

/**
 * This class allows retrieving configuration-dependent classes.
 * 
 */
public abstract class Profile {
	public static final String CMD = "--cmd";
	public static String GUI = "--gui";
	public static String SERVER = "--server";
	public static String HOST = "--host";
	public static String PORT = "--port";
	public static String RMI_SERVER ="rmi";
	public static String SOCKET_SERVER ="socket";
	public static String DATA_SERVER ="data";
}
