package com.abreqadhabra.nflight.examples.common.exception;

import com.abreqadhabra.nflight.common.exception.NestedRuntimeException;

public class NestedRuntimeExceptionExample {
    public static void main(String[] args) {
	try {
	    a(null);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static void a(String strArg) throws Exception {
	try {
	    b(strArg);
	} catch (Exception e) {
	    throw new NestedRuntimeException("foo", e);
	}
    }

    public static void b(String strArg) throws Exception {
	try {
	    c(strArg);
	} catch (Exception e) {
	    throw new NestedRuntimeException("bar", e);
	}
    }

    public static void c(String strArg) throws Exception {
	// 파라미터가 null인 경우
	if (strArg == null) {
	    // 예외를 던집니다.
	    throw new NestedRuntimeException("strArg 파라미터는 null 값을 허용하지 않습니다.");
	}

    }
}