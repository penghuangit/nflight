package com.abreqadhabra.nf.examples.common.exception;

import com.abreqadhabra.nf.common.exception.NestedRuntimeException;


public class Test {
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
	    throw new NestedRuntimeException("foo", e);
	}
    }

    public static void b() throws Exception {
	try {
	    c();
	} catch (Exception e) {
	    throw new NestedRuntimeException("bar", e);
	}
    }

    public static void c() throws Exception {
	throw new Exception("baz");
    }
}

/*


com.abreqadhabra.nf.common.exception.NestedRuntimeException: foo
  nested exception is:
com.abreqadhabra.nf.common.exception.NestedRuntimeException: bar
  nested exception is:
java.lang.Exception: baz
	at com.abreqadhabra.nf.examples.common.exception.Test.c(Test.java:32)
	at com.abreqadhabra.nf.examples.common.exception.Test.b(Test.java:25)
	at com.abreqadhabra.nf.examples.common.exception.Test.a(Test.java:17)
	at com.abreqadhabra.nf.examples.common.exception.Test.main(Test.java:9)
	
	
java.lang.Exception: foo
	at com.abreqadhabra.nf.examples.common.exception.NestedExceptionTest.a(NestedExceptionTest.java:19)
	at com.abreqadhabra.nf.examples.common.exception.NestedExceptionTest.main(NestedExceptionTest.java:9)
Caused by: java.lang.Exception: bar
	at com.abreqadhabra.nf.examples.common.exception.NestedExceptionTest.b(NestedExceptionTest.java:27)
	at com.abreqadhabra.nf.examples.common.exception.NestedExceptionTest.a(NestedExceptionTest.java:17)
	... 1 more
Caused by: java.lang.Exception: baz
	at com.abreqadhabra.nf.examples.common.exception.NestedExceptionTest.c(NestedExceptionTest.java:32)
	at com.abreqadhabra.nf.examples.common.exception.NestedExceptionTest.b(NestedExceptionTest.java:25)
	... 2 more

*/