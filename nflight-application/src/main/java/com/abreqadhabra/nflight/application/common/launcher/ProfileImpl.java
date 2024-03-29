package com.abreqadhabra.nflight.application.common.launcher;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class ProfileImpl implements Profile {
    private static Class<ProfileImpl> THIS_CLAZZ = ProfileImpl.class;
    private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

    public ProfileImpl(Properties props) {
	String METHOD_NAME = Thread.currentThread().getStackTrace()[1].getMethodName();
    }

    // 네트워크 주소와 포트 번호를 지정하여 InetSocketAddress 생성
    public static InetSocketAddress getInetSocketAddress(String host, int port) {
	InetSocketAddress _socketAddress = null;
	try {
	    if(host == null) {
		 _socketAddress = new InetSocketAddress(InetAddress.getLocalHost(), port);

	    }else {
		 _socketAddress = new InetSocketAddress(InetAddress.getByName(host), port);
	    }
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	}

	return _socketAddress;
    }

}
