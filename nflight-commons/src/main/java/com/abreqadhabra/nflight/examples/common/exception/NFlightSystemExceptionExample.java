package com.abreqadhabra.nflight.examples.common.exception;

import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.CommonException;
import com.abreqadhabra.nflight.common.exception.CommonRuntimeException;
import com.abreqadhabra.nflight.common.exception.NFlightSystemException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class NFlightSystemExceptionExample {

    private static final Logger LOGGER = LoggingHelper
	    .getLogger(NFlightSystemExceptionExample.class);

    public static void main(String[] args) {
	try {
	    a();
	} catch (Exception e) {

	    if (e instanceof CommonException) {

		CommonException ce = (CommonException) e;
		System.out.println("에러ID:" + ce.getErrorId());
		System.out.println("메시지ID:" + ce.getMessageId());
		e.printStackTrace();
	    }

	}
    }

    public static void a() throws Exception {
	try {
	    b();
	} catch (Exception e) {
	    if (e instanceof NFlightSystemException) {
		throw new NFlightSystemException("a", e);
	    }
	}
    }

    public static void b() throws Exception {
	try {
	    c();
	} catch (Exception e) {
	    if (e instanceof CommonRuntimeException) {
		throw new CommonRuntimeException("b", e);
	    } else {
		LOGGER.severe("예기치 않은 오류가 발생했습니다.");
		throw new NFlightSystemException(e);
	    }
	}
    }

    public static void c() throws Exception {
	throw new Exception("c");
    }
}