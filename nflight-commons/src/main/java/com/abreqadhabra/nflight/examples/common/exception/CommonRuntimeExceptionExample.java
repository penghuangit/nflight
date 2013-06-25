package com.abreqadhabra.nflight.examples.common.exception;

import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.exception.CommonRuntimeException;
import com.abreqadhabra.nflight.common.exception.UnexpectedRuntimeException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class CommonRuntimeExceptionExample {
    private static final Logger LOGGER = LoggingHelper
	    .getLogger(CommonRuntimeExceptionExample.class.getName());

    public static void main(String[] args) {
	try {
	    a();
	} catch (Exception e) {
	    if (e instanceof CommonRuntimeException) {
		CommonRuntimeException ce = (CommonRuntimeException) e;
		System.out.println("에러ID:" + ce.getErrorId());
		System.out.println("메시지ID:" + ce.getMessageId());
		System.out.println("Stack Trace:\n" + ce.getStackTrace(e));
	    }
		e.printStackTrace();

	}
    }

    public static void a() throws Exception {
	String transactionId = "TX0001";
	try {
	    b();
	} catch (Exception e) {
	    if (e instanceof UnexpectedRuntimeException) {
		throw new UnexpectedRuntimeException(e).addContextValue(
			"Transaction Id", transactionId);
	    }
	}
    }

    public static void b() throws Exception {
	String empno = "1234";
	String empName = "홍길동";
	String deptCode = "ABCD";
	String deptName = "IT기획팀";
	try {
	    c();
	} catch (Exception e) {
	    if (e instanceof CommonRuntimeException) {
		throw new CommonRuntimeException("b", e);
	    } else {
		LOGGER.severe("예기치 않은 오류가 발생했습니다.");
		UnexpectedRuntimeException ue = (UnexpectedRuntimeException) new UnexpectedRuntimeException(
			"b", e).addContextValue("empno", empno)
			.addContextValue("empName", empName);
		ue.setContextValue("deptCode", deptCode);
		ue.setContextValue("deptName", deptName);
		throw ue;
	    }
	}
    }

    public static void c() throws Exception {
	throw new Exception("c");
    }
}