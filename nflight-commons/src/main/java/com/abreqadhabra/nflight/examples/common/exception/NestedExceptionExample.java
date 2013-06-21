package com.abreqadhabra.nflight.examples.common.exception;

import com.abreqadhabra.nflight.common.exception.NestedException;

public class NestedExceptionExample {
    public static void main(String[] args) {
	try {
	    a();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static void a() throws Exception {
	try {
	    b();
	} catch (Exception e) {
	    throw new NestedException("foo", e);
	}
    }

    public static void b() throws Exception {
	try {
	    c();
	} catch (Exception e) {
	    throw new NestedException("bar", e);
	}
    }

    public static void c() throws Exception {
	int a = 1, b = 0;
	int c = a / b;
	System.out.println(c);
    }
}

/*


com.abreqadhabra.nflight.common.exception.NestedException: foo
  nested exception is:
com.abreqadhabra.nflight.common.exception.NestedException: bar
  nested exception is:
java.lang.ArithmeticException: / by zero
	at com.abreqadhabra.nflight.examples.common.exception.NestedExceptionExample.c(NestedExceptionExample.java:40)
	at com.abreqadhabra.nflight.examples.common.exception.NestedExceptionExample.b(NestedExceptionExample.java:29)
	at com.abreqadhabra.nflight.examples.common.exception.NestedExceptionExample.a(NestedExceptionExample.java:21)
	at com.abreqadhabra.nflight.examples.common.exception.NestedExceptionExample.main(NestedExceptionExample.java:13)
	
	
	
java.lang.Exception: foo
	at com.abreqadhabra.nflight.examples.common.exception.NestedExceptionExample.a(NestedExceptionExample.java:19)
	at com.abreqadhabra.nflight.examples.common.exception.NestedExceptionExample.main(NestedExceptionExample.java:9)
Caused by: java.lang.Exception: bar
	at com.abreqadhabra.nflight.examples.common.exception.NestedExceptionExample.b(NestedExceptionExample.java:27)
	at com.abreqadhabra.nflight.examples.common.exception.NestedExceptionExample.a(NestedExceptionExample.java:17)
	... 1 more
Caused by: java.lang.ArithmeticException: / by zero
	at com.abreqadhabra.nflight.examples.common.exception.NestedExceptionExample.c(NestedExceptionExample.java:33)
	at com.abreqadhabra.nflight.examples.common.exception.NestedExceptionExample.b(NestedExceptionExample.java:25)
	... 2 more

*/